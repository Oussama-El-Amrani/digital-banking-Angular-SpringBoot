import { Component, ElementRef, inject, HostListener, OnInit } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-header',
  imports: [CommonModule, NgOptimizedImage, RouterLink],
  templateUrl: './header.component.html'
})
export class HeaderComponent implements OnInit {

  elRef = inject(ElementRef);
  router = inject(Router);
  authService = inject(AuthService);

  isLoggedIn = false;
  isAdmin = false;
  isCustomer = false;
  username = '';

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      this.isLoggedIn = !!user;
      this.isAdmin = this.authService.isAdmin();
      this.isCustomer = this.authService.isCustomer();
      this.username = user?.username || '';
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }

  @HostListener('document:click', ['$event'])
  handleDropdown(event: Event) {
    // close any open dropdown
    const $clickedDropdown = (event.target as HTMLElement).closest('.js-dropdown');
    const $dropdowns = this.elRef.nativeElement.querySelectorAll('.js-dropdown');
    $dropdowns.forEach(($dropdown:HTMLElement) => {
      if ($clickedDropdown !== $dropdown && $dropdown.getAttribute('data-dropdown-keepopen') !== 'true') {
        $dropdown.ariaExpanded = 'false';
        $dropdown.nextElementSibling!.classList.add('hidden');
      }
    });
    // toggle selected if applicable
    if ($clickedDropdown) {
      $clickedDropdown.ariaExpanded = '' + ($clickedDropdown.ariaExpanded !== 'true');
      $clickedDropdown.nextElementSibling!.classList.toggle('hidden');
    }
  }

}
