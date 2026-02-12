import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DepartmentServiceService {
  private readonly API_BASE = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  fetchDepartments(): Observable<any> {
    return this.http.get<any>(`${this.API_BASE}/department`);
  }

  deleteDepartment(deptId: number): Observable<any> {
    return this.http.delete<any>(
      `${this.API_BASE}/department?deptId=${deptId}`,
    );
  }
  saveDepartment(body: any): Observable<any> {
    return this.http.post<any>(`${this.API_BASE}/department`, body);
  }

  getDepartmentById(deptId: number) {
    return this.http.get<any>(
      `${this.API_BASE}/department/find?deptId=${deptId}`,
    );
  }

  updateDepartment(deptId: number, body: any) {
    return this.http.put<any>(
      `${this.API_BASE}/department?deptId=${deptId}`,
      body,
    );
  }
}
