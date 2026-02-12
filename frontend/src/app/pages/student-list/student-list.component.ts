import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { StudentServiceService } from 'src/app/services/student/student-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-student-list',
  templateUrl: './student-list.component.html',
  styleUrls: ['./student-list.component.css'],
})
export class StudentListComponent implements OnInit {
  loading = false;
  showData = false;
  maleChecked = false;
  femaleChecked = false;
  otherChecked = false;

  error: { title: string; message: string } | null = null;

  students: any[] = [];

  counts = {
    total: 0,
    male: 0,
    female: 0,
  };

  state = {
    pageNo: 1,
    pageSize: 10,
    firstPage: true,
    lastPage: true,
    totalPages: 0,
    totalElements: 0,
    searchText: '',
    genders: [] as string[],
  };

  constructor(
    private studentService: StudentServiceService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.checkDeleteMessage();
    this.fetchStudents();
  }

  // check for message in local storage
  checkDeleteMessage(): void {
    const deleteMsg = localStorage.getItem('deleteMessage');

    if (deleteMsg) {
      Swal.fire({
        icon: deleteMsg.toLowerCase().includes('success') ? 'success' : 'info',
        title: deleteMsg.toLowerCase().includes('success')
          ? 'Deleted!'
          : 'Notice',
        text: deleteMsg,
        confirmButtonColor: '#0b557a',
      });
      localStorage.removeItem('student-roll-no');
      localStorage.removeItem('deleteMessage');
    }
  }

  // Filter Logic

  applyFilters(): void {
    this.state.pageNo = 1;
    this.fetchStudents();
  }

  resetFilters(): void {
    this.state.searchText = '';
    this.state.genders = [];
    this.state.pageNo = 1;
    this.state.pageSize = 10;

    this.fetchStudents();
  }

  toggleGender(gender: string, checked: boolean): void {
    if (checked) {
      this.state.genders.push(gender);
    } else {
      this.state.genders = this.state.genders.filter((g) => g !== gender);
    }
  }

  onPageSizeChange(size: number): void {
    this.state.pageSize = +size;
    this.state.pageNo = 1;
    this.fetchStudents();
  }

  changePage(newPage: number): void {
    if (newPage < 1 || newPage > this.state.totalPages) return;
    this.state.pageNo = newPage;
    this.fetchStudents();
  }

  // calling rest api of backend to fetch all students
  fetchStudents(): void {
    this.loading = true;
    this.showData = false;
    this.error = null;

    const body = {
      pageno: this.state.pageNo,
      pagesize: this.state.pageSize,
      sortcolumn: '',
      sortorder: '',
      filter: {
        searchText: this.state.searchText,
        gender: this.state.genders,
      },
    };

    this.studentService.fetchStudents(body).subscribe({
      next: (resp) => {
        // console.log(resp);
        const pageData = resp?.data || {};
        // console.log(pageData);

        this.students = pageData.content || [];

        this.state.firstPage = pageData.first;
        this.state.lastPage = pageData.last;
        this.state.totalElements = pageData.totalElements || 0;
        this.state.totalPages = pageData.totalPages || 1;
        this.state.pageNo = pageData.pageNo || this.state.pageNo;

        this.calculateCounts();
        this.showData = true;
        // console.log(this.state);
      },
      error: (err) => {
        // console.log(err);
        this.error = {
          title: err?.error?.statusCode || 'Error',
          message: err?.error?.message || 'Unable to fetch students',
        };
      },
      complete: () => {
        this.loading = false;
      },
    });
  }

  // supporting methods
  calculateCounts(): void {
    this.counts.total = this.state.totalElements;
    this.counts.male = this.students.filter(
      (s) => s.gender?.toLowerCase() === 'male',
    ).length;

    this.counts.female = this.students.filter(
      (s) => s.gender?.toLowerCase() === 'female',
    ).length;
  }

  openDetail(student: any): void {
    localStorage.setItem('student-roll-no', student.rollNo);
    this.router.navigate(['students/detail', student.rollNo]);
  }
}
