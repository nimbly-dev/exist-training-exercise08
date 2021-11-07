import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HomeComponent implements OnInit {

  user: any

  constructor(
    private router: Router ,
    private storageService: TokenStorageService
  ) {
  }

  ngOnInit(): void {
      var obj: User = this.storageService.getUser();
      if(obj.username == null){
        window.alert('You must be logged in to access this page')
        this.router.navigate(['/login'])
      }
      this.user = obj
  }


}
