import { Employee } from "./employee";

export interface NewTicket{
  title?: string
  description?: string
  severity?: string
  status?: string
  assignedEmployeeId?: Number
  watchersEmployeeId?: Number[]
}
