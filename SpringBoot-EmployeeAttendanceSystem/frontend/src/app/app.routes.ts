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
        loadComponent: () => import('./home/home.component').then((module) => module.HomeComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];