import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { AuditEvent, Health } from '../../../core/platform/platform.models';

@Component({ selector: 'app-audit', imports: [DatePipe], template: `<section class="wide-panel"><span class="eyebrow">EPIC10</span><h1>Auditoría y salud</h1><p class="lede">Trazabilidad de cambios importantes y prueba de conectividad a la base de datos.</p>@if (health(); as h) { <div class="health"><strong>{{ h.status }}</strong><span>Aplicación</span><strong>{{ h.database }}</strong><span>PostgreSQL</span><small>{{ h.timestamp | date:'medium' }}</small></div> }<div class="table-wrap"><table><thead><tr><th>Fecha</th><th>Actor</th><th>Acción</th><th>Entidad</th><th>Detalle</th></tr></thead><tbody>@for (e of events(); track e.id) { <tr><td>{{ e.createdAt | date:'short' }}</td><td>{{ e.actorEmail }}</td><td>{{ e.action }}</td><td>{{ e.entityType }} #{{ e.entityId }}</td><td>{{ e.details }}</td></tr> } @empty { <tr><td colspan="5">Todavía no hay eventos.</td></tr> }</tbody></table></div></section>` })
export class AuditComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly events = signal<AuditEvent[]>([]); readonly health = signal<Health | null>(null);
  ngOnInit() { this.api.audit().subscribe((v) => this.events.set(v)); this.api.health().subscribe((v) => this.health.set(v)); }
}
