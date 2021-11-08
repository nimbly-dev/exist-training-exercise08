import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { departments } from 'src/app/model/constants/departments';
import { AddEmployeeDto } from 'src/app/model/employee/addEmployeeDto';
import { User } from 'src/app/model/auth/user';
import { EmployeeService } from 'src/app/services/employee.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmployeeFormComponent implements OnInit {
  departments: any;
  form: FormGroup;


  constructor(
    private storageService: TokenStorageService,
    private router: Router,
    private employeeService: EmployeeService,
    private formBuilder: FormBuilder
  ) {
    this.departments = departments
    this.form = this.formBuilder.group({
      firstName: '',
      middleName: '',
      lastName: '',
      department: ''
    })
  }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }
  }

  onSubmit(): void{
    let newEmployee : AddEmployeeDto = {
      firstName: this.form.get("firstName")?.value,
      middleName: this.form.get('middleName')?.value,
      lastName: this.form.get('lastName')?.value,
      department: this.form.get('department')?.value
    }
    console.log(newEmployee)
    this.employeeService.addNewEmployee(newEmployee)
      .subscribe((response)=>{
        console.log(response)
      },(error)=>{
        console.log(error.error.messsage)
      })
  }

}
