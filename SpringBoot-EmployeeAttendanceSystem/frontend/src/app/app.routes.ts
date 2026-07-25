import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login.component';
import { AuthGuard } from './core/auth.guard';
import { RoleGuard } from './core/role.guard';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        loadComponent: () => import('./home/home.component').then((module) => module.HomeComponent)
      },
      {
        path: 'admin',
        canActivate: [RoleGuard],
        data: { role: 'ADMIN' },
        loadComponent: () => import('./features/admin/admin.component').then((module) => module.AdminComponent)
      },
      {
        path: 'attendance',
        canActivate: [AuthGuard],
        loadComponent: () => import('./features/attendance/attendance.component').then((module) => module.AttendanceComponent)
      },
      {
        path: 'leave',
        canActivate: [AuthGuard],
        loadComponent: () => import('./features/leave/leave.component').then((module) => module.LeaveComponent)
      },
      {
        path: 'overtime',
        canActivate: [AuthGuard],
        loadComponent: () => import('./features/overtime/overtime.component').then((module) => module.OvertimeComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];