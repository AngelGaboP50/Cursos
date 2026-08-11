import { AuthUser } from '../auth/auth.models';

export type CourseStatus = 'DRAFT' | 'PUBLISHED' | 'INACTIVE';

export interface Course {
  id: number; title: string; description: string; category: string; level: string;
  price: number; status: CourseStatus; startDate: string | null; endDate: string | null; imageUrl: string | null;
}

export type CourseInput = Omit<Course, 'id'>;

export interface Enrollment {
  id: number; course: Course; status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  progressPercent: number; enrollmentDate: string; updatedAt: string;
}

export interface AppNotification { id: number; title: string; message: string; read: boolean; createdAt: string; }
export interface Review { id: number; userId: number; userName: string; rating: number; comment: string; updatedAt: string; }
export interface Reviews { average: number; count: number; reviews: Review[]; }
export interface ReportSummary { users: number; courses: number; publishedCourses: number; enrollments: number; activeEnrollments: number; completedEnrollments: number; notifications: number; reviews: number; }
export interface AuditEvent { id: number; actorEmail: string; action: string; entityType: string; entityId: string | null; details: string; createdAt: string; }
export interface Health { status: string; database: string; timestamp: string; }
export type ManagedUser = AuthUser;
