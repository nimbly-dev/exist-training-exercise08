import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from './services/token-storage.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'exercise08-frontend';
  // isLoggedIn = false;
  // private roles: string[] = [];

  // constructor(private tokenStorageService: TokenStorageService){ }

  // ngOnInit(): void {
  //   this.isLoggedIn = !!this.tokenStorageService.getToken();

  //   if(this.isLoggedIn){
  //     const user = this.tokenStorageService.getUser();
  //     this.roles = user.roles


  //   }
  // }


}
