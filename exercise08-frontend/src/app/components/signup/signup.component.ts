import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SignupDto } from 'src/app/model/auth/signupDto';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SignupComponent implements OnInit {
  errorMssg: string = ""
  form: FormGroup;
  Roles: Array<any> =[
    {id: 'ROLE_USER', value:'user'},
    {id: 'ROLE_ADMIN', value:'admin'},
  ];

  selectedRoles: string[] = []

  constructor(
    private formBuilder: FormBuilder
    ,private userService: UserService
  ) {
      this.form = this.formBuilder.group({
        username: '',
        password: '',
        email: '',
      });
  }

  onChange(role: string,event: Event): void{
    const isChecked = (event.target as HTMLInputElement).checked

    if(isChecked == true){
      console.log("selected")
      this.selectedRoles.push(role)

    }else{
      console.log("deselcted")
      const index = this.selectedRoles.findIndex(x=> x === role)
      this.selectedRoles.splice(index,1)
    }

    console.log(this.selectedRoles)

  }

  onSubmit(): void{
    console.log(this.form.value);
    // Process the signup form here
    let user: SignupDto = {
      username: this.form.get('username')?.value,
      password: this.form.get('password')?.value,
      email: this.form.get('email')?.value,
      role: this.selectedRoles
    };
    this.userService.registerUser(user)
      .subscribe((response)=>{
        console.log('responses')
        console.log('User registered succesful')
      },
      (err)=>{
        console.error("Error caught")
        this.errorMssg = err.error.message
        console.log(this.errorMssg)
      }
      )
  }


  ngOnInit(): void { }

}
