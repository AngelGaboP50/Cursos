import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/auth/auth.service';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Course, Reviews } from '../../../core/platform/platform.models';

@Component({ selector: 'app-curso-detalle', imports: [CurrencyPipe, DatePipe, FormsModule, RouterLink], templateUrl: './curso-detalle.component.html', styleUrl: './curso-detalle.component.css' })
export class CursoDetalleComponent implements OnInit {
  readonly auth = inject(AuthService);
  private readonly api = inject(PlatformApiService);
  private readonly route = inject(ActivatedRoute);
  readonly course = signal<Course | null>(null);
  readonly reviewData = signal<Reviews | null>(null);
  readonly message = signal('');
  readonly error = signal('');
  rating = 5; comment = '';
  private id = 0;

  ngOnInit() { this.id = Number(this.route.snapshot.paramMap.get('id')); this.load(); }
  load() { this.api.course(this.id).subscribe({ next: (v) => this.course.set(v), error: () => this.error.set('Curso no encontrado.') }); this.api.reviews(this.id).subscribe((v) => this.reviewData.set(v)); }
  enroll() { this.api.enroll(this.id).subscribe({ next: () => this.message.set('Inscripción confirmada. Revisa Mis cursos.'), error: (e) => this.error.set(e.error?.message || 'No se pudo realizar la inscripción.') }); }
  favorite() { this.api.addFavorite(this.id).subscribe({ next: () => this.message.set('Curso agregado a favoritos.'), error: (e) => this.error.set(e.error?.message || 'No se pudo agregar.') }); }
  review() { this.api.saveReview(this.id, this.rating, this.comment).subscribe({ next: () => { this.message.set('Reseña guardada.'); this.comment = ''; this.load(); }, error: (e) => this.error.set(e.error?.message || 'No se pudo guardar la reseña.') }); }
}
