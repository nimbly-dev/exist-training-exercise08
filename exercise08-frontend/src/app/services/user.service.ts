import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthURL } from '../model/string_endpoints/auth_urls';
import { SignupDto } from '../model/auth/signupDto';
import { User } from '../model/auth/user';
import { LoginDto } from '../model/auth/loginDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  registerUser(user: SignupDto) : Observable<any>{
    return this.http.post<SignupDto>(AuthURL.signup, user)
  }

  loginUser(user: LoginDto): Observable<any>{
    return this.http.post<any>(AuthURL.signin, user)
  }
  constructor(private http: HttpClient) { }
}
