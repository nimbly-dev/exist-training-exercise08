import { Employee } from "../employee/employee";

export interface Ticket{
  id?: bigint
  title?: string
  description?: string
  severity?: string
  status?: string
  assignedEmployee?: Employee
  ticketsWatched?: Employee[]
}
