import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from 'src/app/model/employee';
import { Ticket } from 'src/app/model/ticket';
import { User } from 'src/app/model/user';
import { TicketService } from 'src/app/services/ticket.service';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-ticket-table',
  templateUrl: './ticket-table.component.html',
  styleUrls: ['./ticket-table.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TicketTableComponent implements OnInit {

  public ticketList?: any

  constructor(
    private storageService: TokenStorageService,
    private router: Router,
    private ticketService: TicketService
  ) { }

  ngOnInit(): void {
    var obj: User = this.storageService.getUser();
    if(obj.username == null){
      window.alert('You must be logged in to access this page')
      this.router.navigate(['/login'])
    }

    this.ticketList = this.getTicketList()
  }

  async getTicketList(){
    await this.ticketService.getTicketList()
      .toPromise().then((response)=>{
        this.ticketList = response as Ticket[]
        console.log(this.ticketList)
      })
      .catch((error)=>{
        console.log(error.error.message)
      })
  }




}
