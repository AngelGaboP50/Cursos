import { CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { Course } from '../../../core/platform/platform.models';

@Component({ selector: 'app-home', imports: [FormsModule, RouterLink, CurrencyPipe], templateUrl: './home.component.html', styleUrl: './home.component.css' })
export class HomeComponent implements OnInit {
  private readonly api = inject(PlatformApiService);
  readonly courses = signal<Course[]>([]);
  readonly categories = signal<string[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  query = '';
  category = '';

  ngOnInit() { this.api.categories().subscribe((v) => this.categories.set(v)); this.search(); }
  search() {
    this.loading.set(true); this.error.set('');
    this.api.courses(this.query, this.category).subscribe({
      next: (v) => { this.courses.set(v); this.loading.set(false); },
      error: () => { this.error.set('No fue posible cargar el catálogo.'); this.loading.set(false); },
    });
  }
}
