import { Injectable } from '@angular/core';
import { User } from '../model/user';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  constructor() { }

  signOut(): void{
    window.sessionStorage.clear();
  }

  public saveToken(token: string): void{
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    let token =  sessionStorage.getItem(TOKEN_KEY) || '{}'
    return token;
  }

  public saveUser(user: User): void{
    window.sessionStorage.removeItem(USER_KEY)
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser(){
    let user: User =  JSON.parse(sessionStorage.getItem(USER_KEY) || '{}');
    return user;
  }


}
