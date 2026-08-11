import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AppNotification, AuditEvent, Course, CourseInput, Enrollment, Health, ManagedUser, ReportSummary, Review, Reviews } from './platform.models';

@Injectable({ providedIn: 'root' })
export class PlatformApiService {
  private readonly http = inject(HttpClient);
  private readonly api = environment.apiUrl;

  courses(query = '', category = '') {
    return this.http.get<Course[]>(`${this.api}/courses`, { params: new HttpParams().set('query', query).set('category', category) });
  }
  categories() { return this.http.get<string[]>(`${this.api}/courses/categories`); }
  course(id: number) { return this.http.get<Course>(`${this.api}/courses/${id}`); }
  reviews(id: number) { return this.http.get<Reviews>(`${this.api}/courses/${id}/reviews`); }
  enroll(courseId: number) { return this.http.post<Enrollment>(`${this.api}/enrollments/courses/${courseId}`, {}); }
  enrollments() { return this.http.get<Enrollment[]>(`${this.api}/enrollments/me`); }
  cancelEnrollment(id: number) { return this.http.delete<Enrollment>(`${this.api}/enrollments/${id}`); }
  progress(id: number, progressPercent: number) { return this.http.patch<Enrollment>(`${this.api}/enrollments/${id}/progress`, { progressPercent }); }
  notifications() { return this.http.get<AppNotification[]>(`${this.api}/notifications`); }
  readNotification(id: number) { return this.http.patch<AppNotification>(`${this.api}/notifications/${id}/read`, {}); }
  readAllNotifications() { return this.http.patch<void>(`${this.api}/notifications/read-all`, {}); }
  favorites() { return this.http.get<Course[]>(`${this.api}/favorites`); }
  addFavorite(courseId: number) { return this.http.post<Course>(`${this.api}/favorites/${courseId}`, {}); }
  removeFavorite(courseId: number) { return this.http.delete<void>(`${this.api}/favorites/${courseId}`); }
  saveReview(courseId: number, rating: number, comment: string) { return this.http.put<Review>(`${this.api}/reviews/courses/${courseId}`, { rating, comment }); }
  adminCourses() { return this.http.get<Course[]>(`${this.api}/admin/courses`); }
  createCourse(input: CourseInput) { return this.http.post<Course>(`${this.api}/admin/courses`, input); }
  updateCourse(id: number, input: CourseInput) { return this.http.put<Course>(`${this.api}/admin/courses/${id}`, input); }
  deactivateCourse(id: number) { return this.http.delete<Course>(`${this.api}/admin/courses/${id}`); }
  reportSummary() { return this.http.get<ReportSummary>(`${this.api}/admin/reports/summary`); }
  downloadReport(kind: 'courses' | 'enrollments') { return this.http.get(`${this.api}/admin/reports/${kind}.csv`, { responseType: 'blob' }); }
  users() { return this.http.get<ManagedUser[]>(`${this.api}/admin/users`); }
  setUserEnabled(id: number, value: boolean) { return this.http.patch<ManagedUser>(`${this.api}/admin/users/${id}/enabled`, {}, { params: { value } }); }
  audit() { return this.http.get<AuditEvent[]>(`${this.api}/admin/audit`); }
  health() { return this.http.get<Health>(`${this.api}/public/health`); }
}
