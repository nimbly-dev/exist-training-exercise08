import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { SignupComponent } from './components/signup/signup.component';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserService } from './services/user.service';
import { HomeComponent } from './components/home/home.component';
import { EmployeeTableComponent } from './components/employee-table/employee-table.component';
import { AuthInterceptor } from './helper/auth/auth.interceptor';
import { EmployeeService } from './services/employee.service';
import { EmployeeDetailComponent } from './components/employee-table/employee-detail/employee-detail.component';
import { EmployeeFormComponent } from './components/employee-table/employee-form/employee-form.component';
import { TicketTableComponent } from './components/ticket-table/ticket-table.component';
import { TicketDetailComponent } from './components/ticket-table/ticket-detail/ticket-detail.component';
import { TicketFormComponent } from './components/ticket-table/ticket-form/ticket-form.component';
import { CommonModule } from '@angular/common';
import { ErrorPageComponent } from './components/error-page/error-page.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    SignupComponent,
    HomeComponent,
    EmployeeTableComponent,
    EmployeeDetailComponent,
    EmployeeFormComponent,
    TicketTableComponent,
    TicketDetailComponent,
    TicketFormComponent,
    ErrorPageComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    EmployeeService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
