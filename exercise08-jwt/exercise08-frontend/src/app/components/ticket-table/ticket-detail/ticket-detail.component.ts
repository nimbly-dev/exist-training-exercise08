import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { severity } from 'src/app/model/constants/severity';
import { status } from 'src/app/model/constants/status';
import { Employee } from 'src/app/model/employee/employee';
import { User } from 'src/app/model/auth/user';
import { EmployeeService } from 'src/app/services/employee.service';
import { TicketService } from 'src/app/services/ticket.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UpdateTicket } from 'src/app/model/ticket/updateTicketDto';
import { Ticket } from 'src/app/model/ticket/ticket';

@Component({
  selector: 'app-ticket-detail',
  templateUrl: './ticket-detail.component.html',
  styleUrls: ['./ticket-detail.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketDetailComponent implements OnInit {

  form!: FormGroup

  ticket?: any
  watchers?: Employee[] | any
  assignedEmployee?: any
  employeeList?: any

  selectedWatcher: Number = 0
  selectedWatchers: Number[] = []

  currentTicketWatchersId?: any

  severity?: any
  status?: any

  constructor(
    private storageService: TokenStorageService,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private router: Router,
    private ticketService: TicketService,
    private employeeService: EmployeeService
  )
  {
    this.currentTicketWatchersId = []
    this.severity = severity
    this.status = status
    this.form = this.formBuilder.group({
      ticketTitle: '',
      ticketDesc: '',
      severity: '',
      status: '',
      assignedEmployee: 0,
    });
  }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }
    //Must be in this order to avoid error, idk why
    this.getWatchers(this.route.snapshot.params.id)
    this.getEmployeeList()
    this.getAssignedEmployee(this.route.snapshot.params.id)
    this.getTicketDetail(this.route.snapshot.params.id)

  }

  getTicketDetail(id: Number){

    const promise = new Promise((resolve,reject)=>{
      this.ticketService.getTicketById(id)
        .then((response)=>{
          this.ticket = response
          this.form = this.formBuilder.group({
            ticketTitle: this.ticket.title,
            ticketDesc: this.ticket.description,
            severity: this.ticket.severity,
            status: this.ticket.status,
            assignedEmployee: this.assignedEmployee.id
          });
          resolve(response);
        },
          err=>{
            reject(err)
          })
    })

    return promise
  }

  getWatchers(id: Number){
    const promise = new Promise((resolve,reject)=>{
      this.ticketService.getWatchersById(id)
        .then((response: Employee[])=>{
          this.watchers = response

          response.forEach((watcher)=>{
            this.currentTicketWatchersId.push(watcher.id);
          })
          // this.watchers.forEach(watcher => {
          //   this.currentTicketWatchersId.push(watcher.id)
          // });

          resolve(response);
        },
          err=>{
            reject(err)
          })
    })

    return promise
  }

  getAssignedEmployee(id: Number){
    const promise = new Promise((resolve,reject)=>{
      this.ticketService.getAssignedEmployeeById(id)
        .then((response)=>{
          this.assignedEmployee = response
          resolve(response);
        },
          err=>{
            reject(err)
          })
    })

    return promise

  }

  getEmployeeList(){
    const promise = new Promise((resolve,reject)=>{
      this.employeeService.getEmployeeList()
        .then((response)=>{
          this.employeeList = response as Employee[]
          resolve(response)
        },err=>{
          reject(err)
        })
    })

    return promise

  }

  onSubmit(event : MouseEvent): void{
    if(event == "DELETE_TICKET" as unknown){
      this.ticketService.deleteTicketById(this.route.snapshot.params.id)
        .subscribe(()=>{
          window.alert('Ticket Now Deleted')
          this.router.navigate(['/ticket'])
        },(error)=>{
          console.log(error.error.message)
          window.alert(error.error.message)
        })
    }else{
      let ticketToBeUpdated : UpdateTicket = {
        title: this.form.get('ticketTitle')?.value,
        description: this.form.get('ticketDesc')?.value,
        severity: this.form.get('severity')?.value,
        status: this.form.get('status')?.value,
        assignedEmployeeId: this.form.get('assignedEmployee')?.value,
        watchersEmployeeId: this.currentTicketWatchersId
      }

      console.log(ticketToBeUpdated)
      this.ticketService.updateTicketById(ticketToBeUpdated,this.route.snapshot.params.id)//ticket/1
        .subscribe((response)=>{
          console.log(response)
          window.alert('Updated Ticket')
        },(error)=>{
          console.log(error.error.message)
          window.alert(error.error.message)
        })
    }
  }

  removeWatcher(employeeId: Number){
    this.ticketService.removeEmployeeWatcher(this.route.snapshot.params.id,employeeId)
      .subscribe((response)=>{
        console.log(response)
        window.alert('succesfully removed watcher')
        location.reload()
      },(error)=>{
        console.log(error.error.message)
        window.alert(error.error.message)
      })
  }

  addWatcherToWatchersArray(){
    if(this.currentTicketWatchersId.includes(Number(this.selectedWatcher))){
      window.alert('Already Included')
    }else{
      console.log(this.currentTicketWatchersId.push(Number(this.selectedWatcher)))
    }

  }

  onChange(id: Number){
    this.selectedWatcher = id
  }



}
