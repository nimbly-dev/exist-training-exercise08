import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/employee/employee';
import { User } from 'src/app/model/auth/user';
import { EmployeeService } from 'src/app/services/employee.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-employee-table',
  templateUrl: './employee-table.component.html',
  styleUrls: ['./employee-table.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmployeeTableComponent implements OnInit {

  public employeeList?: any;
  constructor(
    private storageService: TokenStorageService,
    private router: Router,
    private employeeService: EmployeeService
  ) {

  }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }
    this.employeeList = this.getEmployeeList()
  }

  async getEmployeeList(){
    await this.employeeService.getEmployeeList()
      .toPromise().then((response)=>{
        this.employeeList = response as Employee[]
        console.log(this.employeeList)
      })
      .catch((error)=>{
        console.log(error.error.message)
      })
  }

}
