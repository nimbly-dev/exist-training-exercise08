import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EmployeeService } from 'src/app/services/employee.service';
import { TicketService } from 'src/app/services/ticket.service';
import { Employee } from 'src/app/model/employee';
import { status } from 'src/app/model/constants/status';
import { severity } from 'src/app/model/constants/severity';
import { NewTicket } from 'src/app/model/addNewTicketDto';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-ticket-form',
  templateUrl: './ticket-form.component.html',
  styleUrls: ['./ticket-form.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketFormComponent implements OnInit {

  form: FormGroup

  public employeeList?: any;
  public status?: any;
  public severity?: any

  // public assignedEmployee: Number
  public selectedWatcher: Number = 0
  public selectedWatchers: Number[] = []
  // public watcherToBeAdded: Number = 0
  // public displayWatchers: Employee[]

  constructor(
    private storageService: TokenStorageService,
    private router: Router,
    private ticketService: TicketService,
    private employeeService: EmployeeService,
    private formBuilder: FormBuilder
  ) {
    // this.assignedEmployee = 0
    // this.watcherToBeAdded = 0
    // this.displayWatchers = []
    this.form = this.formBuilder.group({
      title: '',
      description: '',
      severity: '',
      status: '',
      assignedEmployee: 0
      // watchers: [] Can't implement to angular html templating
      // ticketsWatched: Employee[],
    })
  }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }


    this.employeeList = this.getEmployeeList()
    this.status = status
    this.severity = severity
  }

  async getEmployeeList(){
    await this.employeeService.getEmployeeList()
      .toPromise().then((response)=>{
        this.employeeList = response as Employee[]
        // console.log(this.employeeList)
      })
      .catch((error)=>{
        console.log(error.error.message)
      })
  }

  addWatcherToWatchersArray(){
    console.log(this.selectedWatcher)
    if(this.selectedWatchers.includes(this.selectedWatcher)){
      console.log('Already added')
      window.alert('Already Added')
    }else{
      this.selectedWatchers.push(this.selectedWatcher)
    }
    console.log(this.selectedWatchers)
  }

  onChange(id: Number){
    this.selectedWatcher = id
  }

  onSubmit(): void{
    let newTicket : NewTicket = {
      title: this.form.get("title")?.value,
      description: this.form.get("description")?.value,
      severity: this.form.get("severity")?.value,
      status: this.form.get("status")?.value,
      assignedEmployeeId: this.form.get("assignedEmployee")?.value,
      watchersEmployeeId: this.selectedWatchers
    }

    console.log(newTicket)

    this.ticketService.addNewTicket(newTicket)
      .subscribe((response)=>{
        console.log(response)
      },(error)=>{
        console.log(error)
        window.alert(error.error.message)
      })
  }


}
