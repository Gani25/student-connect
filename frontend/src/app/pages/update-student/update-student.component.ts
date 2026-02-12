import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StudentServiceService } from 'src/app/services/student/student-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-update-student',
  templateUrl: './update-student.component.html',
  styleUrls: ['./update-student.component.css'],
})
export class UpdateStudentComponent implements OnInit {
  updateForm!: FormGroup;
  rollNo!: number;

  emailAvailable = true;
  checkingEmail = false;
  emailErrorMsg = '';

  showErrorBox = false;
  errorTitle = '';
  errorMsg = '';

  constructor(
    private studentService: StudentServiceService,
    private route: ActivatedRoute,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const routeRollNo = Number(this.route.snapshot.paramMap.get('rollNo'));
    const storageRollNo = Number(localStorage.getItem('student-roll-no'));

    if (storageRollNo && routeRollNo && storageRollNo !== routeRollNo) {
      this.rollNo = storageRollNo;
    } else {
      this.rollNo = routeRollNo;
    }
    this.buildForm();
    this.loadStudent();
  }

  // create reactive form
  buildForm(): void {
    this.updateForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      age: new FormControl(null, [
        Validators.required,
        Validators.min(0),
        Validators.max(200),
      ]),
      gender: new FormControl(''),
      address: new FormControl('', [Validators.required]),
      percentage: new FormControl(null, [
        Validators.required,
        Validators.min(0),
        Validators.max(100),
      ]),
    });
  }

  // load student data into form
  loadStudent(): void {
    if (!this.rollNo) {
      this.showError('Invalid Roll No', 'Roll number missing in URL.');
      return;
    }

    this.studentService.getStudentByRollNo(this.rollNo).subscribe({
      next: (res) => {
        const s = res.data;

        this.updateForm.patchValue({
          firstName: s.firstName,
          lastName: s.lastName,
          email: s.email,
          age: s.age,
          gender: s.gender || '',
          address: s.address,
          percentage: s.percentage,
        });

        this.emailAvailable = true;
      },
      error: (err) => {
        const msg = err?.error?.message || 'Unable to load student.';
        this.showError('Error', msg);
      },
    });
  }

  // email available check
  onEmailBlur(): void {
    const emailControl = this.updateForm.get('email');
    if (!emailControl || emailControl.invalid) {
      this.emailAvailable = false;
      return;
    }

    const email = emailControl.value.trim();
    if (!email) return;

    this.checkingEmail = true;
    this.emailErrorMsg = '';
    this.emailAvailable = false;

    // pass rollNo for update
    this.studentService.checkEmailAvailable(email, this.rollNo).subscribe({
      next: (res) => {
        this.checkingEmail = false;

        if (!res.available) {
          this.emailAvailable = false;
          this.emailErrorMsg = res.message || 'Email already exists';
          emailControl.setErrors({ emailTaken: true });
        } else {
          this.emailAvailable = true;
          this.emailErrorMsg = '';
          emailControl.setErrors(null);
        }
      },
      error: () => {
        this.checkingEmail = false;
        this.emailAvailable = false;
        this.emailErrorMsg = 'Error checking email. Try again.';
        emailControl.setErrors({ emailCheckFailed: true });
      },
    });
  }

  // submit update
  submit(): void {
    if (this.updateForm.invalid || !this.emailAvailable) {
      this.updateForm.markAllAsTouched();
      return;
    }

    const body = {
      firstName: this.updateForm.value.firstName.trim(),
      lastName: this.updateForm.value.lastName.trim(),
      email: this.updateForm.value.email.trim(),
      age: Number(this.updateForm.value.age),
      gender: this.updateForm.value.gender,
      address: this.updateForm.value.address.trim(),
      percentage: Number(this.updateForm.value.percentage),
    };

    this.studentService.updateStudent(this.rollNo, body).subscribe({
      next: () => {
        const fullName = `${body.firstName} ${body.lastName}`;

        Swal.fire({
          icon: 'success',
          title: 'Updated!',
          text: `Student ${fullName} updated successfully!`,
          confirmButtonColor: '#0b557a',
        }).then(() => {
          localStorage.setItem('student-roll-no', String(this.rollNo));
          this.router.navigate(['/detail', this.rollNo]);
        });
      },
      error: (err) => {
        const msg = err?.error?.message || 'Something went wrong.';
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: msg,
        });
      },
    });
  }

  hasError(controlName: string, errorName: string): boolean {
    const control = this.updateForm.get(controlName);
    return !!(control && control.touched && control.hasError(errorName));
  }

  private showError(title: string, msg: string): void {
    this.showErrorBox = true;
    this.errorTitle = title;
    this.errorMsg = msg;
  }
}
