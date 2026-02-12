import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { StudentServiceService } from 'src/app/services/student/student-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-student',
  templateUrl: './add-student.component.html',
  styleUrls: ['./add-student.component.css'],
})
export class AddStudentComponent implements OnInit {
  addForm!: FormGroup;

  emailAvailable = false;
  checkingEmail = false;
  emailErrorMsg = '';

  constructor(
    private studentService: StudentServiceService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    this.buildForm();
  }

  // creating reactive form
  buildForm(): void {
    this.addForm = new FormGroup({
      firstName: new FormControl('', [Validators.required]),
      lastName: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required, Validators.email]),
      age: new FormControl(null, [
        Validators.required,
        Validators.min(0),
        Validators.max(200),
      ]),
      gender: new FormControl(''),
      // gender: new FormControl('', [Validators.required]),
      address: new FormControl('', [Validators.required]),
      percentage: new FormControl(null, [
        Validators.required,
        Validators.min(0),
        Validators.max(100),
      ]),
    });
  }

  // email available check on blur from form
  onEmailBlur(): void {
    const emailControl = this.addForm.get('email');
    if (!emailControl || emailControl.invalid) {
      this.emailAvailable = false;
      return;
    }

    const email = emailControl.value.trim();
    if (!email) return;

    this.checkingEmail = true;
    this.emailErrorMsg = '';
    this.emailAvailable = false;

    // check email available, for insert we will pass rollNo as 0
    this.studentService.checkEmailAvailable(email, 0).subscribe({
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
      error: (err) => {
        // console.log(err);
        this.checkingEmail = false;
        this.emailAvailable = false;
        this.emailErrorMsg = 'Error checking email. Try again.';
        emailControl.setErrors({ emailCheckFailed: true });
      },
    });
  }

  // on submit
  submit(): void {
    if (this.addForm.invalid || !this.emailAvailable) {
      this.addForm.markAllAsTouched();
      return;
    }

    const body = {
      firstName: this.addForm.value.firstName.trim(),
      lastName: this.addForm.value.lastName.trim(),
      email: this.addForm.value.email.trim(),
      age: Number(this.addForm.value.age),
      gender: this.addForm.value.gender,
      address: this.addForm.value.address.trim(),
      percentage: Number(this.addForm.value.percentage),
    };

    this.studentService.saveStudent(body).subscribe({
      next: (res) => {
        const rollNo = res?.data?.rollNo;
        const fullName = `${body.firstName} ${body.lastName}`;

        Swal.fire({
          icon: 'success',
          title: 'Success!',
          text: `Student ${fullName} saved successfully!`,
          confirmButtonColor: '#0b557a',
        }).then(() => {
          localStorage.setItem('student-roll-no', rollNo);
          this.router.navigate(['students/detail', rollNo]);
        });
      },
      error: (err) => {
        const msg =
          err?.error?.message || 'Something went wrong while saving student';

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
