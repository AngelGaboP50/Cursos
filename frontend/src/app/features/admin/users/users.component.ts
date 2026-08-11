import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { ManagedUser } from '../../../core/platform/platform.models';

@Component({ selector: 'app-users', imports: [DatePipe], template: `<section class="wide-panel"><span class="eyebrow">EPIC09</span><h1>Administración de usuarios</h1><p class="lede">Consulta cuentas y controla su acceso sin modificar contraseñas ni eliminar historial.</p><div class="table-wrap"><table><thead><tr><th>Persona</th><th>Rol</th><th>Alta</th><th>Estado</th></tr></thead><tbody>@for (user of users(); track user.id) { <tr><td><strong>{{ user.name }}</strong><small>{{ user.email }}</small></td><td>{{ user.role }}</td><td>{{ user.createdAt | date:'mediumDate' }}</td><td><button class="mini" [class.danger]="user.enabled" (click)="toggle(user)">{{ user.enabled ? 'Deshabilitar' : 'Habilitar' }}</button></td></tr> }</tbody></table></div></section>` })
export class UsersComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly users = signal<ManagedUser[]>([]);
  ngOnInit() { this.load(); } load() { this.api.users().subscribe((v) => this.users.set(v)); }
  toggle(user: ManagedUser) { this.api.setUserEnabled(user.id, !user.enabled).subscribe(() => this.load()); }
}
