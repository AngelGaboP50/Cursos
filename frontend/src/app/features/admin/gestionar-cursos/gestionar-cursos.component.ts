import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Course, CourseInput, CourseStatus } from '../../../core/platform/platform.models';

@Component({ selector: 'app-gestionar-cursos', imports: [FormsModule], templateUrl: './gestionar-cursos.component.html', styleUrl: './gestionar-cursos.component.css' })
export class GestionarCursosComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly courses = signal<Course[]>([]); readonly message = signal(''); readonly error = signal('');
  editingId: number | null = null; form: CourseInput = this.blank();
  ngOnInit() { this.load(); } load() { this.api.adminCourses().subscribe((v) => this.courses.set(v)); }
  edit(c: Course) { this.editingId = c.id; this.form = { title: c.title, description: c.description, category: c.category, level: c.level, price: c.price, status: c.status, startDate: c.startDate, endDate: c.endDate, imageUrl: c.imageUrl }; }
  reset() { this.editingId = null; this.form = this.blank(); }
  save() { const request = this.editingId ? this.api.updateCourse(this.editingId, this.form) : this.api.createCourse(this.form); request.subscribe({ next: () => { this.message.set('Curso guardado correctamente.'); this.reset(); this.load(); }, error: (e) => this.error.set(e.error?.message || 'No fue posible guardar el curso.') }); }
  deactivate(c: Course) { if (!confirm(`¿Desactivar ${c.title}?`)) return; this.api.deactivateCourse(c.id).subscribe(() => this.load()); }
  private blank(): CourseInput { return { title: '', description: '', category: 'Programación', level: 'INICIAL', price: 0, status: 'DRAFT' as CourseStatus, startDate: null, endDate: null, imageUrl: null }; }
}
