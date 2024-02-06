import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RegisterService } from './register.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  username:any;
  password:any;
  confirmPassword:any;

  constructor(private service:RegisterService,private router:Router){

  }

  onSubmit(){
    if(this.confirmPassword === this.password){
      this.service.register(this.username,this.password).subscribe((data:any)=>{
        this.router.navigateByUrl("");
      });
    }else{
      this.username="";
      this.password="";
      this.confirmPassword="";
    }
  }

}
