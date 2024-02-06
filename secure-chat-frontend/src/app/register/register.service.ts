import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {
  

  constructor(private http:HttpClient) { }

  register(username: any, password: any) {
    const body = {username,password};
    return this.http.post("https://localhost:9000/api/register",body, {responseType: 'text'});
  }
}
