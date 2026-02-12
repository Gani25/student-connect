import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DepartmentServiceService } from 'src/app/services/department/department-service.service';
import { StudentServiceService } from 'src/app/services/student/student-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-assign-department',
  templateUrl: './assign-department.component.html',
  styleUrls: ['./assign-department.component.css'],
})
export class AssignDepartmentComponent implements OnInit {
  form!: FormGroup;

  rollNo!: number;
  studentName = '';

  departments: any[] = [];

  loading = false;
  error: { title: string; message: string } | null = null;

  constructor(
    private studentService: StudentServiceService,
    private departmentService: DepartmentServiceService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.resolveRollNo();
    this.buildForm();
    this.loadStudent();
    this.loadDepartments();
  }

  // Resolve Roll No
  resolveRollNo(): void {
    const paramRoll = this.route.snapshot.paramMap.get('id');
    const localRoll = localStorage.getItem('student-roll-no');

    if (paramRoll && localRoll && paramRoll !== localRoll) {
      this.rollNo = Number(localRoll);
    } else {
      this.rollNo = Number(paramRoll || localRoll);
    }

    if (!this.rollNo) {
      this.error = {
        title: 'Invalid Roll No',
        message: 'Roll number missing in URL.',
      };
      this.loading = false;
      return;
    }
  }

  // Build Form
  buildForm(): void {
    this.form = new FormGroup({
      deptId: new FormControl('', [Validators.required]),
    });
  }

  // Load Student Info
  loadStudent(): void {
    this.studentService.getStudentByRollNo(this.rollNo).subscribe({
      next: (res) => {
        const s = res?.data;
        this.studentName = `${s.firstName} ${s.lastName}`;
      },
      error: () => {
        this.error = {
          title: 'Error',
          message: 'Unable to load student details.',
        };
      },
    });
  }

  // Load All Departments
  loadDepartments(): void {
    this.departmentService.fetchDepartments().subscribe({
      next: (res) => {
        this.departments = res?.data || [];
      },
      error: () => {
        this.error = {
          title: 'Error',
          message: 'Unable to load departments list.',
        };
      },
    });
  }

  // Assign New Department To Student
  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const deptId = Number(this.form.value.deptId);

    this.loading = true;

    this.studentService.assignDepartment(this.rollNo, deptId).subscribe({
      next: () => {
        this.loading = false;

        Swal.fire({
          icon: 'success',
          title: 'Assigned!',
          text: `Department assigned to student successfully.`,
          confirmButtonColor: '#0b557a',
        }).then(() => {
          this.router.navigate(['/students/detail', this.rollNo]);
        });
      },
      error: (err) => {
        this.loading = false;

        const msg =
          err?.error?.message ||
          'Something went wrong while assigning department.';

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: msg,
        });
      },
    });
  }

  // Validation Helper
  hasError(controlName: string, errorName: string): boolean {
    const control = this.form.get(controlName);
    return !!(control && control.touched && control.hasError(errorName));
  }
}
