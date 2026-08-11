import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/auth/auth.service';

@Component({ selector: 'app-account', imports: [FormsModule], templateUrl: './account.component.html', styleUrl: './account.component.css' })
export class AccountComponent implements OnInit {
  readonly auth = inject(AuthService); readonly loading = signal(true); readonly errorMessage = signal(''); readonly message = signal(''); name = '';
  ngOnInit() { this.auth.loadCurrentUser().subscribe({ next: (user) => { this.name = user.name; this.loading.set(false); }, error: () => { this.loading.set(false); this.errorMessage.set('La sesión no pudo validarse.'); } }); }
  save() { this.auth.updateProfile(this.name).subscribe({ next: () => this.message.set('Perfil actualizado.'), error: (e) => this.errorMessage.set(e.error?.message || 'No fue posible actualizar.') }); }
}
