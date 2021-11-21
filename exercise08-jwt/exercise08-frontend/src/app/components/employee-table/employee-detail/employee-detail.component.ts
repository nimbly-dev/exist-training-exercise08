import { Component, OnInit, ChangeDetectionStrategy, Input, Pipe } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { departments } from 'src/app/model/constants/departments';
import { Employee } from 'src/app/model/employee/employee';
import { UpdateEmployeeDto } from 'src/app/model/employee/updateEmployeeDto';
import { User } from 'src/app/model/auth/user';
import { EmployeeService } from 'src/app/services/employee.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { TicketService } from 'src/app/services/ticket.service';
import { Ticket } from 'src/app/model/ticket/ticket';
import { AssignTicketToEmployeeDto } from 'src/app/model/employee/assignTicketToEmployeeDto';

@Component({
  selector: 'app-employee-detail',
  templateUrl: './employee-detail.component.html',
  styleUrls: ['./employee-detail.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class EmployeeDetailComponent implements OnInit {


  form!: FormGroup;

  employee?: any
  ticketList?: any
  departments?: any

  selectedAssignTicketId: Number = 0

  constructor(
    private storageService: TokenStorageService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
    private employeeService: EmployeeService,
    private ticketService: TicketService
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
   this.getTicketList()
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

  getTicketList(){

    const promise = new Promise((resolve,reject)=>{
      this.employeeService.getEmployeeList()
        .then((response)=>{
          this.ticketList = response as Ticket[]
          resolve(response)
        },err=>{
          reject(err)
        })
    })

    return promise

    // await this.ticketService.getTicketList()
    //   .toPromise().then((response)=>{
    //     this.ticketList = response as Ticket[]
    //   })
    //   .catch(error=>{
    //     console.log(error.error.message)
    //   })
  }

  assignTicketToEmployee(){
    let assignNewTicketToEmployee: AssignTicketToEmployeeDto = {
      employeeId: this.route.snapshot.params.id,
      ticketIdAssigned: this.selectedAssignTicketId
    }
    this.employeeService.addAssignedTicketToEmployee(assignNewTicketToEmployee)
      .subscribe((response)=>{
        console.log('Added')
        console.log(response)
      },(error)=>{
        console.log(error)
        window.alert(error.error.message)
      })
    // this.employeeService
  }

  removeWatcher(id: Number){
    this.employeeService.removeTicketWatched(this.route.snapshot.params.id,id)
      .subscribe((response)=>{
        window.alert('Ticket Watched Removed')
        console.log(response)
      },(error)=>{
        console.log(error)
      })
  }

  removeAssignedTicket(id: Number){
    this.employeeService.removeAssignedTicketById(this.route.snapshot.params.id,id)
      .subscribe((response)=>{
        window.alert('Assigned Ticket Removed')
      },(error)=>{
        console.log(error)
      })
  }

  onChangeAssignedTicketInput(id: Number){
    this.selectedAssignTicketId = id
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

}
