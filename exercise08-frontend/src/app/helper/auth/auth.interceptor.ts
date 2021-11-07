import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private storageService: TokenStorageService
  ) {}

  //Add authorization header from every request of the client
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let token = this.storageService.getToken();
    request = request.clone({
      setHeaders:{
        Authorization: `Bearer ${token}`
      }
    });
    return next.handle(request)
  }

}
