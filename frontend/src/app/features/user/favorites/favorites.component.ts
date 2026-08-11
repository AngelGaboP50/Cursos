import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Course } from '../../../core/platform/platform.models';

@Component({ selector: 'app-favorites', imports: [RouterLink], template: `<section class="wide-panel"><span class="eyebrow">EPIC08</span><h1>Mis favoritos</h1><p class="lede">Tu lista personal de cursos para revisar después.</p><div class="course-grid">@for (course of courses(); track course.id) { <article class="course-card compact"><div class="card-body"><span class="eyebrow">{{ course.category }}</span><h2>{{ course.title }}</h2><p>{{ course.description }}</p><div class="actions"><a class="button secondary" [routerLink]="['/courses', course.id]">Ver detalle</a><button class="button danger" (click)="remove(course.id)">Quitar</button></div></div></article> } @empty { <p class="empty">No has guardado favoritos.</p> }</div></section>` })
export class FavoritesComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly courses = signal<Course[]>([]);
  ngOnInit() { this.load(); } load() { this.api.favorites().subscribe((v) => this.courses.set(v)); }
  remove(id: number) { this.api.removeFavorite(id).subscribe(() => this.load()); }
}
