import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewTicket } from '../model/ticket/addNewTicketDto';
import { Employee } from '../model/employee/employee';
import { Ticket_URL } from '../model/string_endpoints/ticket_urls';
import { Ticket } from '../model/ticket/ticket';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  getTicketList(): Promise<any>{
    return this.http.get<Ticket[]>(Ticket_URL.getTicketList).toPromise()
  }

  getWatchersById(id: Number): Promise<any>{
    return this.http.get<Employee[]>(Ticket_URL.getWatchersById+""+id).toPromise()
  }

  getAssignedEmployeeById(id: Number): Promise<Employee>{
    return this.http.get<Employee>(Ticket_URL.getAssignedEmployeeById+""+id).toPromise()
  }

  getTicketById(id: Number): Promise<any>{
    return this.http.get<any>(Ticket_URL.getTicketById+""+id).toPromise()
  }

  addNewTicket(ticket: NewTicket): Observable<any>{
    return this.http.post<any>(Ticket_URL.createNewTicket,ticket)
  }

  deleteTicketById(id: Number): Observable<any>{
    return this.http.delete<any>(Ticket_URL.deleteTicketById+''+id)
  }

  updateTicketById(ticketToUpdate: any, id: Number): Observable<any>{
    return this.http.put<any>(Ticket_URL.updateTicketById+''+id,ticketToUpdate)
  }

  removeEmployeeWatcher(ticketId: Number, employeeIdWatcherToBeDeleted: Number){
    return this.http.delete(Ticket_URL.removeEmployeeWatcher
      +"ticketId="+ticketId+"&employeeIdWatcherToBeDeleted="+employeeIdWatcherToBeDeleted)
  }


  constructor(
    private http: HttpClient
  ) { }
}
