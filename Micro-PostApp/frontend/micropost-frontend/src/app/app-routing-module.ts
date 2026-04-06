import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { FeedComponent } from './features/feed/feed';
import { RegisterComponent } from './features/auth/register/register';
import { VerifyEmailComponent } from './features/auth/verify-email/verify-email';
import { authGuard } from './core/guards/auth-guard';
import { GuestGuard } from './core/services/guest-guard';
import { VerifyPendingComponent } from './features/auth/verify-pending/verify-pending';
import { ProfileComponent } from './features/profile/profile';

const routes: Routes = [
  { path: 'login', component: LoginComponent, canActivate: [GuestGuard] },
  { path: 'register', component: RegisterComponent, canActivate: [GuestGuard] },
  { path: 'verify-email', component: VerifyEmailComponent, canActivate: [GuestGuard] },
  { path: 'feed', component: FeedComponent, canActivate: [authGuard] },
  { path: 'verify-pending', component: VerifyPendingComponent },
  {path:'profile', component: ProfileComponent},
  {path: '', redirectTo:'feed',pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
