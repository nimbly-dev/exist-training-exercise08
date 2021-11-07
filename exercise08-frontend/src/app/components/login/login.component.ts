import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginDto } from 'src/app/model/loginDto';
import { User } from 'src/app/model/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LoginComponent implements OnInit {
  form: FormGroup;

  constructor(
    private router: Router ,
    private formBuilder: FormBuilder,
    private userService: UserService,
    private storageService: TokenStorageService) {
    this.form = this.formBuilder.group({
      username: '',
      password: ''
    })
  }

  onSubmit(){
    console.log("clicked submit")
    let user: LoginDto ={
      username : this.form.get("username")?.value,
      password : this.form.get("password")?.value
    }

    this.userService.loginUser(user)
      .subscribe((response)=>{
        console.log("Successful login")
        let loggedUser: User = {
          username: response.username,
          bearer: response.bearer,
          email: response.email,
          roles: response.roles
        }

        this.storageService.saveToken(response.token)
        this.storageService.saveUser(loggedUser)
        this.router.navigate(['/home'])
      },
      (error)=>{
        console.error("Error caught")
        console.log(error.error)
      })
  }

  ngOnInit(): void {
  }

}
