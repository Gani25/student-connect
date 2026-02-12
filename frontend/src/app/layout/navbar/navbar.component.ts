import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ThemeService } from 'src/app/services/theme/theme.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent {
  constructor(
    public theme: ThemeService,
    private router: Router,
  ) {}

  toggleTheme(): void {
    this.theme.toggleTheme();
  }

  goToNewStudent(): void {
    localStorage.removeItem('student-roll-no');
    localStorage.removeItem('department-id');
    this.router.navigate(['/students/add']);
  }
  goToNewDepartment(): void {
    localStorage.removeItem('student-roll-no');
    localStorage.removeItem('department-id');
    this.router.navigate(['departments/add']);
  }
  goToHome(): void {
    localStorage.removeItem('student-roll-no');
    localStorage.removeItem('department-id');
    this.router.navigate(['/']);
  }
  goToDepartment(): void {
    localStorage.removeItem('student-roll-no');
    localStorage.removeItem('department-id');
    this.router.navigate(['/departments']);
  }
}
