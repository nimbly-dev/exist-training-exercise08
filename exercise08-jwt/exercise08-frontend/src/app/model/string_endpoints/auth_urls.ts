import { environment } from "src/environments/environment"

export class AuthURL {
  public static signup =  environment.apiUrl+"/api/auth/signup"
  public static signin = environment.apiUrl+"/api/auth/signin"
}
