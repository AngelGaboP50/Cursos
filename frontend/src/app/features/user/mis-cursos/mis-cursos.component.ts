import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Enrollment } from '../../../core/platform/platform.models';

@Component({ selector: 'app-mis-cursos', imports: [DatePipe], templateUrl: './mis-cursos.component.html', styleUrl: './mis-cursos.component.css' })
export class MisCursosComponent implements OnInit {
  private readonly api = inject(PlatformApiService);
  readonly items = signal<Enrollment[]>([]); readonly message = signal(''); readonly error = signal('');
  ngOnInit() { this.load(); }
  load() { this.api.enrollments().subscribe({ next: (v) => this.items.set(v), error: () => this.error.set('No fue posible consultar tus cursos.') }); }
  progress(item: Enrollment, event: Event) { const value = Number((event.target as HTMLInputElement).value); this.api.progress(item.id, value).subscribe({ next: () => { this.message.set('Progreso actualizado.'); this.load(); }, error: (e) => this.error.set(e.error?.message || 'No fue posible actualizar.') }); }
  cancel(item: Enrollment) { if (!confirm(`¿Cancelar la inscripción a ${item.course.title}?`)) return; this.api.cancelEnrollment(item.id).subscribe({ next: () => this.load(), error: (e) => this.error.set(e.error?.message || 'No fue posible cancelar.') }); }
}
