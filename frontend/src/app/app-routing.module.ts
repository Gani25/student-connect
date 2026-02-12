import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentListComponent } from './pages/student-list/student-list.component';
import { AddStudentComponent } from './pages/add-student/add-student.component';
import { StudentDetailComponent } from './pages/student-detail/student-detail.component';
import { UpdateStudentComponent } from './pages/update-student/update-student.component';
import { DepartmentListComponent } from './pages/department-list/department-list.component';
import { AddDepartmentComponent } from './pages/add-department/add-department.component';
import { UpdateDepartmentComponent } from './pages/update-department/update-department.component';
import { AssignDepartmentComponent } from './pages/assign-department/assign-department.component';

const routes: Routes = [
  { path: '', component: StudentListComponent },
  { path: 'students/add', component: AddStudentComponent },
  { path: 'students/detail/:rollNo', component: StudentDetailComponent },
  { path: 'students/update/:rollNo', component: UpdateStudentComponent },
  { path: 'students/detail', component: StudentDetailComponent },
  {
    path: 'students/assign-department/:rollNo',
    component: AssignDepartmentComponent,
  },
  { path: 'departments', component: DepartmentListComponent },
  { path: 'departments/add', component: AddDepartmentComponent },
  { path: 'departments/update/:deptId', component: UpdateDepartmentComponent },

  // wildcard routes
  // { path: '**', component: UpdateDepartmentComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
