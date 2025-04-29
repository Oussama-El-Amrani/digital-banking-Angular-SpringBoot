import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from 'environments/environment';
import { RouterModule } from '@angular/router';


@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  environment = environment;
}
