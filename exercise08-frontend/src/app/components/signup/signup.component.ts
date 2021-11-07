import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SignupDto } from 'src/app/model/signupDto';
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

  constructor(
    private formBuilder: FormBuilder
    ,private userService: UserService
  ) {
      this.form = this.formBuilder.group({
        username: ['', Validators.required, Validators.minLength(3)],
        password: ['', Validators.required, Validators.minLength(6),
                    Validators.maxLength(40)],
        email: ['', Validators.required, Validators.email],
        roles: this.formBuilder.array([]),
      });
  }

  onChange(event: Event): void{
    // const evtMssg = event? ' Event target is ' + (event.target as HTMLInputElement).value: ''
    const roles: FormArray = this.form.get('roles') as FormArray

    if((event?.target as HTMLInputElement).checked){
      roles.push(new FormControl((event?.target as HTMLInputElement).value));
    }else{
        console.log("deselected")
        const index = roles.controls.findIndex(x => x.value === (event?.target as HTMLInputElement).value);
        roles.removeAt(index);
    }
    // console.log(roles);
  }

  onSubmit(): void{
    console.log(this.form.value);
    // Process the signup form here
    let user: SignupDto = {
      username: this.form.get('username')?.value,
      password: this.form.get('password')?.value,
      email: this.form.get('email')?.value,
      roles: this.form.get("roles")?.value
    };
    this.userService.registerUser(user)
      .subscribe((response)=>{
        console.log('User registered succesful')
      },
      (err)=>{
        console.error("Error caught")
        // console.log(err.error.message)
        this.errorMssg = err.error.message
        console.log(this.errorMssg)
      }
      )
    //   ((resp: User)=>{
    //   console.log(resp);
    // });
  }


  ngOnInit(): void { }

}
