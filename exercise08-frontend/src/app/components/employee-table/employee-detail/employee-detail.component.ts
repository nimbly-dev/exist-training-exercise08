import { Component, OnInit, ChangeDetectionStrategy, Input, Pipe } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { departments } from 'src/app/model/constants/departments';
import { Employee } from 'src/app/model/employee';
import { UpdateEmployeeDto } from 'src/app/model/updateEmployeeDto';
import { User } from 'src/app/model/user';
import { EmployeeService } from 'src/app/services/employee.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmployeeDetailComponent implements OnInit {


  form!: FormGroup;

  employee?: any
  departments?: any

  constructor(
    private storageService: TokenStorageService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
    private employeeService: EmployeeService
  ) {
    this.departments = departments
    this.form = this.formBuilder.group({
      firstname: '',
      middlename: '',
      lastname: '',
      department: ''
    })
  }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }
   this.getEmployeeDetail(this.route.snapshot.params.id)
  }

  async getEmployeeDetail(id: Number){
    await this.employeeService.getEmployeeById(id)
      .toPromise().then((response)=>{
        this.employee = response
        this.form = this.formBuilder.group({
          firstname: this.employee.firstName,
          middlename: this.employee.middleName,
          lastname: this.employee.lastName,
          department: this.employee.department
        })
      })
      .catch(error=>{
        console.log(error.error.message)
      })
  }

  onSubmit(event : MouseEvent): void{
    let employeeToBeUpdated : UpdateEmployeeDto = {
      firstName: this.form.get("firstname")?.value,
      middleName: this.form.get('middlename')?.value,
      lastName: this.form.get('lastname')?.value,
      department: this.form.get('department')?.value
    }

    if(event == "UPDATE_EMPLOYEE" as unknown){
      this.employeeService.updateEmployeeById(this.route.snapshot.params.id,employeeToBeUpdated)
        .subscribe((response)=>{
          console.log(response)
        },(error)=>{
          console.log(error.error.message)
          window.alert(error.error.message)
        })
    }else{
      console.log("deleted")
      this.employeeService.deleteEmployeeById(this.route.snapshot.params.id)
        .subscribe(()=>{
          window.alert('Employee deleted now returning to employee table page')
          this.router.navigate(['/employee'])
        },(error)=>{
          console.log(error.error.message)
          window.alert(error.error.message)
        })
    }
  }


  // deleteTutorial(): void {
  //   this.tutorialService.delete(this.currentTutorial.id)
  //     .subscribe(
  //       response => {
  //         console.log(response);
  //         this.router.navigate(['/tutorials']);
  //       },
  //       error => {
  //         console.log(error);
  //       });
  // }
}
