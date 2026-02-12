import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './layout/navbar/navbar.component';
import { StudentListComponent } from './pages/student-list/student-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AddStudentComponent } from './pages/add-student/add-student.component';
import { StudentDetailComponent } from './pages/student-detail/student-detail.component';
import { UpdateStudentComponent } from './pages/update-student/update-student.component';
import { DepartmentListComponent } from './pages/department-list/department-list.component';
import { AddDepartmentComponent } from './pages/add-department/add-department.component';
import { UpdateDepartmentComponent } from './pages/update-department/update-department.component';
import { AssignDepartmentComponent } from './pages/assign-department/assign-department.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    StudentListComponent,
    AddStudentComponent,
    StudentDetailComponent,
    UpdateStudentComponent,
    DepartmentListComponent,
    AddDepartmentComponent,
    UpdateDepartmentComponent,
    AssignDepartmentComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
