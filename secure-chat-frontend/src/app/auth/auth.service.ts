import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {


  private loggedIn: boolean = false;
  private loggedUser: any;
  public username!: string;
  userId!: number;
  userInfomation: any;

  token:any="";

  constructor(private http:HttpClient) { }

  setLoggedIn(loggedIn :boolean){
    this.loggedIn = loggedIn;
  }

  isLoggedIn():boolean{
    return this.loggedIn;
  }

  setLoggedUser(loggedUser: any){
    this.loggedUser = loggedUser;
  }

  getLoggedUser(): any {
    return this.loggedUser;
  }

  findUser(username: any, password: any) {
    const body = {username,password};
    return this.http.post("https://localhost:9000/api/login",body);
  }

  getUserByName(name:any){
    this.token = localStorage.getItem("token");
    console.log(this.token);
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.get("https://localhost:9000/users/" + name, {headers});
  }

}
