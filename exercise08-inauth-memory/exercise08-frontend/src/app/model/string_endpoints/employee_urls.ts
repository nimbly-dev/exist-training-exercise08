import { environment } from "src/environments/environment"

export class Employee_URL {
  public static createEmployee =  environment.apiUrl+"/api/employee/create-employee"
  public static findEmployeeById = environment.apiUrl+"/api/employee/get-employee-by-id?id="
  public static listAllEmployees = environment.apiUrl+"/api/employee/get-employee-list"
  public static deleteEmployeeById = environment.apiUrl+"/api/employee/delete-employee-by-id?id="
  public static updateEmployeeById = environment.apiUrl+"/api/employee/update-employee-by-id?id="
  public static removeTicketWatchedByID = environment.apiUrl+"/api/employee/remove-ticket-watched-by-id?"
  public static removeAssignedTicketById = environment.apiUrl+"/api/employee/remove-assigned-ticket-by-id?"
  public static assignTicketToEmployeeById = environment.apiUrl+"/api/employee/add-assigned-ticket"
}
