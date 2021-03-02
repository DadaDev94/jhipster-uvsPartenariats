import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEmploye, Employe } from 'app/shared/model/employe.model';
import { EmployeService } from './employe.service';
import { EmployeComponent } from './employe.component';
import { EmployeDetailComponent } from './employe-detail.component';
import { EmployeUpdateComponent } from './employe-update.component';

@Injectable({ providedIn: 'root' })
export class EmployeResolve implements Resolve<IEmploye> {
  constructor(private service: EmployeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmploye> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((employe: HttpResponse<Employe>) => {
          if (employe.body) {
            return of(employe.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Employe());
  }
}

export const employeRoute: Routes = [
  {
    path: '',
    component: EmployeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'uvsPartenariatsApp.employe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeDetailComponent,
    resolve: {
      employe: EmployeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.employe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeUpdateComponent,
    resolve: {
      employe: EmployeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.employe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeUpdateComponent,
    resolve: {
      employe: EmployeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.employe.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
