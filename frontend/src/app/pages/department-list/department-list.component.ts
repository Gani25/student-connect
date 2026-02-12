import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DepartmentServiceService } from 'src/app/services/department/department-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-department-list',
  templateUrl: './department-list.component.html',
  styleUrls: ['./department-list.component.css'],
})
export class DepartmentListComponent implements OnInit {
  departments: any[] = [];

  loading = false;
  showData = false;

  error: { title: string; message: string } | null = null;

  constructor(
    private departmentService: DepartmentServiceService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.fetchDepartments();
  }

  fetchDepartments(): void {
    this.loading = true;
    this.showData = false;
    this.error = null;

    this.departmentService.fetchDepartments().subscribe({
      next: (resp) => {
        // console.log(resp);

        // Your API returns: resp.data = array
        this.departments = resp?.data || [];

        this.showData = true;
      },
      error: (err) => {
        this.error = {
          title: err?.error?.statusCode || 'Error',
          message: err?.error?.message || 'Unable to fetch departments',
        };
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  updateDepartment(dept: any): void {
    localStorage.setItem('department-id', dept.deptId);
    this.router.navigate(['/departments/update', dept.deptId]);
  }

  deleteDepartment(dept: any): void {
    Swal.fire({
      title: 'Are you sure?',
      text: `Department "${dept.deptName}" at "${dept.location}" will be permanently deleted.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (!result.isConfirmed) return;

      this.departmentService.deleteDepartment(dept.deptId).subscribe({
        next: (res) => {
          Swal.fire({
            icon: 'success',
            title: 'Deleted!',
            text: res?.message || 'Department deleted successfully',
            confirmButtonColor: '#0b557a',
          });

          // Stay on this page and refresh list
          this.fetchDepartments();
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text:
              err?.error?.message ||
              'Something went wrong while deleting department',
          });
        },
      });
    });
  }
}
