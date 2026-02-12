import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DepartmentServiceService } from 'src/app/services/department/department-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-department',
  templateUrl: './add-department.component.html',
  styleUrls: ['./add-department.component.css'],
})
export class AddDepartmentComponent implements OnInit {
  addForm!: FormGroup;

  constructor(
    private departmentService: DepartmentServiceService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.buildForm();
  }

  // Build reactive form
  buildForm(): void {
    this.addForm = new FormGroup({
      deptName: new FormControl('', [Validators.required]),
      location: new FormControl('', [Validators.required]),
    });
  }

  submit(): void {
    if (this.addForm.invalid) {
      this.addForm.markAllAsTouched();
      return;
    }

    const body = {
      deptName: this.addForm.value.deptName.trim(),
      location: this.addForm.value.location.trim(),
    };

    this.departmentService.saveDepartment(body).subscribe({
      next: (res) => {
        const deptId = res?.data?.deptId;
        const deptName = body.deptName;
        const location = body.location;

        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: `Department ${deptName} at ${location} added successfully!`,
          confirmButtonColor: '#0b557a',
        }).then(() => {
          this.router.navigate(['/departments']);
        });
      },
      error: (err) => {
        const msg =
          err?.error?.message || 'Something went wrong while saving department';

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: msg,
        });
      },
    });
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.addForm.get(controlName);
    return !!(control && control.touched && control.hasError(errorName));
  }
}
