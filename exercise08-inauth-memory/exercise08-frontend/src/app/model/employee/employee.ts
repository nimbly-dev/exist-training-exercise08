import { Ticket } from "../ticket/ticket";


export interface Employee{
  id?: bigint
  firstName?: string
  middleName?: string
  lastName?: string
  department?: string
  assignedTickets?: Ticket[]
  ticketsWatched?: Ticket[]
}
