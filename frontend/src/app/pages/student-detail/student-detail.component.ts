import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentServiceService } from 'src/app/services/student/student-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-student-detail',
  templateUrl: './student-detail.component.html',
  styleUrls: ['./student-detail.component.css'],
})
export class StudentDetailComponent implements OnInit {
  rollNo!: number;

  loading = true;
  error: { title: string; message: string } | null = null;

  student: any = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private studentService: StudentServiceService,
  ) {}

  ngOnInit(): void {
    this.resolveRollNo();
    this.loadStudent();
  }

  resolveRollNo(): void {
    const paramRoll = this.route.snapshot.paramMap.get('rollNo');
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

  // get student by roll No
  loadStudent(): void {
    this.studentService.getStudentByRollNo(this.rollNo).subscribe({
      next: (res: any) => {
        this.student = res.data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = {
          title: err?.error?.httpStatus || 'Error',
          message: err?.error?.message || 'Unable to fetch student',
        };
      },
    });
  }

  // open update form
  goToUpdate(): void {
    localStorage.setItem('student-roll-no', String(this.rollNo));
    this.router.navigate(['students/update', this.rollNo]);
  }

  // Delete
  deleteStudent(): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'This student will be permanently deleted.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Yes, delete',
      cancelButtonText: 'Cancel',
    }).then((result) => {
      if (!result.isConfirmed) return;

      this.studentService.deleteStudent(this.rollNo).subscribe({
        next: (res: any) => {
          const msg = res?.message || 'Student deleted.';
          localStorage.setItem('deleteMessage', msg);
          this.router.navigate(['/']);
        },
        error: (err) => {
          const msg =
            err?.error?.message || 'Something went wrong while deleting.';
          localStorage.setItem('deleteMessage', msg);
          this.router.navigate(['/']);
        },
      });
    });
  }

  assignDepartmentToStudent(): void {
    localStorage.setItem('student-roll-no', String(this.rollNo));
    this.router.navigate(['/students/assign-department', this.rollNo]);
  }

  getAvatar(): string {
    if (!this.student) return '';
    return (this.student.firstName[0] + this.student.lastName[0]).toUpperCase();
  }
}
