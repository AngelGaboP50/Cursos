import { Component, inject, OnInit, signal } from '@angular/core';
import { PlatformApiService } from '../../../core/platform/platform-api.service';
import { ReportSummary } from '../../../core/platform/platform.models';

@Component({ selector: 'app-exportar', templateUrl: './exportar.component.html', styleUrl: './exportar.component.css' })
export class ExportarComponent implements OnInit {
  private readonly api = inject(PlatformApiService); readonly summary = signal<ReportSummary | null>(null);
  ngOnInit() { this.api.reportSummary().subscribe((v) => this.summary.set(v)); }
  download(kind: 'courses' | 'enrollments') { this.api.downloadReport(kind).subscribe((blob) => { const url = URL.createObjectURL(blob); const a = document.createElement('a'); a.href = url; a.download = kind === 'courses' ? 'cursos.csv' : 'inscripciones.csv'; a.click(); URL.revokeObjectURL(url); }); }
}
