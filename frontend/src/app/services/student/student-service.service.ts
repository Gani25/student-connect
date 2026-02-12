import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class StudentServiceService {
  private readonly API_BASE = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  // fetch all student
  fetchStudents(body: any): Observable<any> {
    return this.http.post(`${this.API_BASE}/students`, body);
  }
  // check email available or not

  checkEmailAvailable(email: string, rollNo: number = 0): Observable<any> {
    return this.http.get(
      `${this.API_BASE}/student/email-available/${email}/${rollNo}`,
    );
  }

  // save student form
  saveStudent(payload: any): Observable<any> {
    return this.http.post(`${this.API_BASE}/student`, payload, {
      headers: { 'Content-Type': 'application/json' },
    });
  }

  // get Student By Roll No -> For Student Detail
  getStudentByRollNo(rollNo: number): Observable<any> {
    return this.http.get(`${this.API_BASE}/student/${rollNo}`);
  }

  //  Delete Student By Roll No
  deleteStudent(rollNo: number): Observable<any> {
    return this.http.delete(`${this.API_BASE}/student?rollNo=${rollNo}`);
  }

  // update by roll no
  updateStudent(rollNo: number, body: any): Observable<any> {
    return this.http.put(`${this.API_BASE}/student?rollNo=${rollNo}`, body);
  }

  assignDepartment(rollNo: number, deptId: number) {
    return this.http.put<any>(
      `${this.API_BASE}/students/${rollNo}/department/${deptId}`,
      {},
    );
  }
}
