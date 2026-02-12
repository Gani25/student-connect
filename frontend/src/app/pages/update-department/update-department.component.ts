import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DepartmentServiceService } from 'src/app/services/department/department-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-department',
  templateUrl: './update-department.component.html',
  styleUrls: ['./update-department.component.css'],
})
export class UpdateDepartmentComponent implements OnInit {
  updateForm!: FormGroup;

  deptId!: number;

  loading = false;
  error: { title: string; message: string } | null = null;

  constructor(
    private departmentService: DepartmentServiceService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.resolveDeptId();
    this.buildForm();
    this.loadDepartment();
  }

  // Get Correct Dept Id
  resolveDeptId(): void {
    const routeId = this.route.snapshot.paramMap.get('deptId');
    const storedId = localStorage.getItem('department-id');

    if (routeId && storedId && routeId !== storedId) {
      this.deptId = Number(storedId);
    } else {
      this.deptId = Number(routeId || storedId);
    }

    if (!this.deptId) {
      this.error = {
        title: 'Invalid Department',
        message: 'Department ID not found in URL or local storage.',
      };
      return;
    }
  }

  // Build Update Form
  buildForm(): void {
    this.updateForm = new FormGroup({
      deptName: new FormControl('', [Validators.required]),
      location: new FormControl('', [Validators.required]),
    });
  }

  // Filling Update Form With Old Values
  loadDepartment(): void {
    if (!this.deptId) return;

    this.loading = true;

    this.departmentService.getDepartmentById(this.deptId).subscribe({
      next: (res) => {
        const dept = res?.data;

        this.updateForm.patchValue({
          deptName: dept.deptName,
          location: dept.location,
        });

        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = {
          title: err?.error?.httpStatus || 'Error',
          message: err?.error?.message || 'Unable to load department details.',
        };
      },
    });
  }

  // Update Button Submit
  submit(): void {
    if (this.updateForm.invalid) {
      this.updateForm.markAllAsTouched();
      return;
    }

    const body = {
      deptName: this.updateForm.value.deptName.trim(),
      location: this.updateForm.value.location.trim(),
    };

    this.departmentService.updateDepartment(this.deptId, body).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: 'Updated!',
          text: 'Department updated successfully!',
          confirmButtonColor: '#0b557a',
        }).then(() => {
          this.router.navigate(['/departments']);
        });
      },
      error: (err) => {
        const msg =
          err?.error?.message ||
          'Something went wrong while updating department';

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: msg,
        });
      },
    });
  }

  // Validation Helper TO show on form
  hasError(controlName: string, errorName: string): boolean {
    const control = this.updateForm.get(controlName);
    return !!(control && control.touched && control.hasError(errorName));
  }
}
