export interface UpdateTicket {
  title?: string
  description?: string
  severity?: string
  status?: string
  assignedEmployeeId?: Number
  watchersEmployeeId?: Number[]
}
