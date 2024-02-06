import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { MpServiceService } from './mp-service.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit{

  selectedMessage:any="";
  messages:any = [];
  users:any = [];
  newMessage:string = "";
  receiverId?:number;

  selectedTabIndex:any;

  constructor(private service:MpServiceService, private authService: AuthService,private router:Router){

  }

  ngOnInit(){
    this.fetchInformation();
  }

  fetchInformation(){
    this.messages = [];
    this.service.findAllMessagesOfUser(this.authService.userId).subscribe((messages:any)=>{
      console.log(messages);
      
      this.service.findAllUsers().subscribe((users:any)=> {
        this.users = users;
        messages.forEach((element:any) => {
          console.log(element);
            this.service.findUser(element.idSender).subscribe((data:any) =>{
              console.log(data);
              const message = {
                id: element.id,
                content: element.content,
                sender: data.username
              }
              this.messages.push(message);
              this.messages.sort((a:any, b:any) => {
                return b.id - a.id;
              });
            })
        });
      })
    });
  }

  onSubmit(){
    if(this.newMessage !== ""){
      const numberOfParts = Math.floor(Math.random() * 5) + 1; 
      const messageParts = this.splitStringRandomly(this.newMessage,numberOfParts);
      const messageId = this.generateUniqueMessageId();
      //send last part firstly
      // const bodyLastPart = {
      //   id: messageId,
      //   text: messageParts[messageParts.length-1],
      //   currentPart: messageParts.length,
      //   totalParts: messageParts.length,
      //   senderId: this.authService.userId,
      //   receiverId: this.receiverId
      // }
      // this.service.sendLastPartOfMessage(bodyLastPart).subscribe((data:any)=>{

      // });
      for(let i = 0;i< messageParts.length;i++){
        const body = {
          id: messageId,
          text: messageParts[i],
          currentPart: i+1,
          totalParts: messageParts.length,
          senderId: this.authService.userId,
          receiverId: this.receiverId
        }
        if(i%3===0){
          this.service.sendMessagePartToServer1(body).subscribe((data:any)=>{
            
          });
        }else if(i%3 === 1){
          this.service.sendMessagePartToServer2(body).subscribe((data:any)=>{
            
          });
        }else{
          this.service.sendMessagePartToServer3(body).subscribe((data:any)=>{
            
          });
        }
      }
        
      this.newMessage = " ";
      
      
    }
    
  }

  generateUniqueMessageId(): number {
    const min = -2147483648;
    const max = 2147483647;

    const randomInt = Math.floor(Math.random() * (max - min + 1)) + min;

    return randomInt;
  }

  splitStringRandomly(inputString: string, numberOfParts: number): string[] {
    
    const partLength = Math.floor(inputString.length / numberOfParts);
    const parts: string[] = [];
  
    let startIndex = 0;
    for (let i = 0; i < numberOfParts; i++) {
      const endIndex = startIndex + partLength;
      const part = inputString.substring(startIndex, endIndex);
      parts.push(part);
      startIndex = endIndex;
    }

    if (startIndex < inputString.length) {
      const remainingPart = inputString.substring(startIndex);
      parts.push(remainingPart);
    }
  
    return parts;
  }

  findUser(id:any){
    this.service.findUser(id).subscribe((data:any)=>{
      return data.username;
    })
  }

  showMessage(message:any){
    this.selectedMessage = message;
  }

  getUsername(){
    return this.authService.username;
  }

  logout(){
    this.authService.setLoggedUser(null);
    this.authService.setLoggedIn(false);
    this.authService.userId=0;
    this.authService.userInfomation=null;
    this.authService.username="";
    this.router.navigateByUrl("");
    localStorage.clear();
  }
}
