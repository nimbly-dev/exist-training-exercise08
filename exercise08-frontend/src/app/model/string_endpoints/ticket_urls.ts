import { environment } from "src/environments/environment";

export class Ticket_URL {
  public static getTicketList = environment.apiUrl+"/api/ticket/get-ticket-list"
  public static getTicketById = environment.apiUrl+"/api/ticket/get-ticket-by-id?id="
  public static createNewTicket = environment.apiUrl+"/api/ticket/create-ticket"
  public static deleteTicketById = environment.apiUrl+"/api/ticket/delete-ticket-by-id?id="
  public static updateTicketById = environment.apiUrl+"/api/ticket/update-ticket-by-id?id="
  public static getAssignedEmployeeById = environment.apiUrl+"/api/ticket/get-assigned-employee-by-id?id="
  public static getWatchersById = environment.apiUrl+"/api/ticket/get-watchers-by-id?id="
}
