import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SignupComponent } from './components/signup/signup.component';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { EmployeeTableComponent } from './components/employee-table/employee-table.component';
import { EmployeeDetailComponent } from './components/employee-table/employee-detail/employee-detail.component';
import { EmployeeFormComponent } from './components/employee-table/employee-form/employee-form.component';
import { TicketTableComponent } from './components/ticket-table/ticket-table.component';
import { TicketDetailComponent } from './components/ticket-table/ticket-detail/ticket-detail.component';
import { TicketFormComponent } from './components/ticket-table/ticket-form/ticket-form.component';

const routes: Routes = [
  { path: '', redirectTo: '', pathMatch: 'full' },
  { path: 'signup', component: SignupComponent },
  { path: 'login', component: LoginComponent },
  { path: 'home' , component: HomeComponent },
  { path: 'employee' , component: EmployeeTableComponent },
  { path: 'employee/add' , component: EmployeeFormComponent },
  { path: 'ticket', component: TicketTableComponent},
  { path: 'ticket/add', component: TicketFormComponent},
  { path: 'employee/:id', component: EmployeeDetailComponent},
  { path: 'ticket/:id', component: TicketDetailComponent},
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {}
