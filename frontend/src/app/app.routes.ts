import { Routes } from '@angular/router';
import { authGuard } from './core/auth/auth.guard';
import { roleGuard } from './core/auth/role.guard';
import { AccountComponent } from './features/account/account.component';
import { DashboardAdminComponent } from './features/admin/dashboard-admin/dashboard-admin.component';
import { ExportarComponent } from './features/admin/exportar/exportar.component';
import { GestionarCursosComponent } from './features/admin/gestionar-cursos/gestionar-cursos.component';
import { AuditComponent } from './features/admin/audit/audit.component';
import { UsersComponent } from './features/admin/users/users.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { CursoDetalleComponent } from './features/user/curso-detalle/curso-detalle.component';
import { FavoritesComponent } from './features/user/favorites/favorites.component';
import { HomeComponent } from './features/user/home/home.component';
import { MisCursosComponent } from './features/user/mis-cursos/mis-cursos.component';
import { NotificacionesComponent } from './features/user/notificaciones/notificaciones.component';

const protectedRoute = [authGuard];
const adminRoute = [authGuard, roleGuard];

export const routes: Routes = [
  { path: 'login', component: LoginComponent, title: 'Iniciar sesión | Cursos' },
  { path: 'register', component: RegisterComponent, title: 'Crear cuenta | Cursos' },
  { path: '', component: HomeComponent, title: 'Catálogo | Cursos' },
  { path: 'courses/:id', component: CursoDetalleComponent, title: 'Detalle del curso | Cursos' },
  { path: 'my-courses', component: MisCursosComponent, canActivate: protectedRoute, title: 'Mis cursos | Cursos' },
  { path: 'notifications', component: NotificacionesComponent, canActivate: protectedRoute, title: 'Notificaciones | Cursos' },
  { path: 'favorites', component: FavoritesComponent, canActivate: protectedRoute, title: 'Favoritos | Cursos' },
  { path: 'account', component: AccountComponent, canActivate: protectedRoute, title: 'Mi cuenta | Cursos' },
  { path: 'admin', component: DashboardAdminComponent, canActivate: adminRoute, data: { role: 'ADMIN' }, title: 'Administración | Cursos' },
  { path: 'admin/courses', component: GestionarCursosComponent, canActivate: adminRoute, data: { role: 'ADMIN' }, title: 'Gestionar cursos | Cursos' },
  { path: 'admin/reports', component: ExportarComponent, canActivate: adminRoute, data: { role: 'ADMIN' }, title: 'Reportes | Cursos' },
  { path: 'admin/users', component: UsersComponent, canActivate: adminRoute, data: { role: 'ADMIN' }, title: 'Usuarios | Cursos' },
  { path: 'admin/audit', component: AuditComponent, canActivate: adminRoute, data: { role: 'ADMIN' }, title: 'Auditoría | Cursos' },
  { path: '**', redirectTo: '' },
];
