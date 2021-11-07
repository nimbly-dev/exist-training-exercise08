import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from '../model/employee';
import { Employee_URL } from '../model/string_endpoints/employee_urls';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  getEmployeeList(): Observable<Employee[]> {
    return this.http.get<Employee[]>(Employee_URL.listAllEmployees)
  }

  getEmployeeById(id: Number): Observable<Employee>{
    return this.http.get<Employee>(Employee_URL.findEmployeeById+''+id)
  }

  updateEmployeeById(id: Number, employee: any): Observable<any>{
    return this.http.put<any>(Employee_URL.updateEmployeeById+''+id, employee)
  }

  deleteEmployeeById(id: Number): Observable<any>{
    return this.http.delete<any>(Employee_URL.deleteEmployeeById+''+id)
  }

  addNewEmployee(employee: any): Observable<any>{
    return this.http.post<any>(Employee_URL.createEmployee,employee)
  }

  constructor(
    private http: HttpClient
  ) { }
}
