import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { AppNotification } from '../../../core/platform/platform.models';

@Component({ selector: 'app-notificaciones', imports: [DatePipe], templateUrl: './notificaciones.component.html', styleUrl: './notificaciones.component.css' })
export class NotificacionesComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly items = signal<AppNotification[]>([]); readonly error = signal('');
  ngOnInit() { this.load(); } load() { this.api.notifications().subscribe({ next: (v) => this.items.set(v), error: () => this.error.set('No fue posible cargar las notificaciones.') }); }
  read(item: AppNotification) { if (item.read) return; this.api.readNotification(item.id).subscribe(() => this.load()); }
  readAll() { this.api.readAllNotifications().subscribe(() => this.load()); }
}
