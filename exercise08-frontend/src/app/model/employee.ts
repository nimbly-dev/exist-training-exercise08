import { Ticket } from "./ticket";

export interface Employee{
  id?: bigint
  firstName?: string
  middleName?: string
  lastName?: string
  department?: string
  assignedTickets?: Ticket[]
  ticketsWatched?: Ticket[]
}
