import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewTicket } from '../model/addNewTicketDto';
import { Employee } from '../model/employee';
import { Ticket_URL } from '../model/string_endpoints/ticket_urls';
import { Ticket } from '../model/ticket';

@Injectable({
  providedIn: 'root'
})
export class TicketService {

  getTicketList(): Observable<Ticket[]>{
    return this.http.get<Ticket[]>(Ticket_URL.getTicketList)
  }

  getWatchersById(id: Number): Observable<Employee[]>{
    return this.http.get<Employee[]>(Ticket_URL.getWatchersById+""+id)
  }

  getAssignedEmployeeById(id: Number): Observable<Employee>{
    return this.http.get<Employee>(Ticket_URL.getAssignedEmployeeById+""+id)
  }

  getTicketById(id: Number): Observable<any>{
    return this.http.get<any>(Ticket_URL.getTicketById+""+id)
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

  constructor(
    private http: HttpClient
  ) { }
}
