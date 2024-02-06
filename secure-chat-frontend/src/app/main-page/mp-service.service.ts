import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class MpServiceService {

  token:any="";
 
  

  constructor(private http:HttpClient) { }


  findUser(id:any){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.get("https://localhost:9000/users/id/" + id ,{headers});
  }

  findAllMessagesOfUser(id:number){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.get("https://localhost:9000/messages/" + id , {headers});
  }

  
  findAllUsers(){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.get("https://localhost:9000/users" , {headers});
  }

  sendMessagePartToServer1(body:any){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.post("https://localhost:9000/messages", body,  {headers, responseType: 'text'});
  }

  sendMessagePartToServer2(body:any){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.post("https://localhost:9001/messages", body, {headers, responseType: 'text'});
  }

  sendMessagePartToServer3(body:any){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.post("https://localhost:9002/messages", body, {headers, responseType: 'text'});
  }

  sendLastPartOfMessage(body:any){
    this.token = localStorage.getItem("token");
    const headers = new HttpHeaders().set('Authorization', `Bearer ${this.token}` );
    return this.http.post("https://localhost:9000/messages/last-part", body,  {headers, responseType: 'text'});
  }
}
