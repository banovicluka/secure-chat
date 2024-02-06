import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username:any;
  password:any;

  constructor(private authService:AuthService, private router: Router){

  }

  onSubmit(){
    this.authService.findUser(this.username,this.password).subscribe((data:any)=>{
      this.authService.setLoggedUser(data);
      localStorage.setItem("token", data.token);
      console.log(data.token);
      this.authService.username = data.username;
      this.authService.getUserByName(data.username).subscribe((user:any)=>{
        this.authService.userId = user.id;
        this.authService.userInfomation = user;
        this.authService.setLoggedIn(true);
        this.router.navigateByUrl('/main-page');
      })
    })
  }

}
