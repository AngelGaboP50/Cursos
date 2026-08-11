import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Health, ReportSummary } from '../../../core/platform/platform.models';

@Component({ selector: 'app-dashboard-admin', imports: [RouterLink], templateUrl: './dashboard-admin.component.html', styleUrl: './dashboard-admin.component.css' })
export class DashboardAdminComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly summary = signal<ReportSummary | null>(null); readonly health = signal<Health | null>(null); readonly error = signal('');
  ngOnInit() { this.api.reportSummary().subscribe({ next: (v) => this.summary.set(v), error: () => this.error.set('No fue posible cargar los indicadores.') }); this.api.health().subscribe((v) => this.health.set(v)); }
}
