import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAccord, Accord } from 'app/shared/model/accord.model';
import { AccordService } from './accord.service';
import { AccordComponent } from './accord.component';
import { AccordDetailComponent } from './accord-detail.component';
import { AccordUpdateComponent } from './accord-update.component';

@Injectable({ providedIn: 'root' })
export class AccordResolve implements Resolve<IAccord> {
  constructor(private service: AccordService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAccord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((accord: HttpResponse<Accord>) => {
          if (accord.body) {
            return of(accord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Accord());
  }
}

export const accordRoute: Routes = [
  {
    path: '',
    component: AccordComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'uvsPartenariatsApp.accord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccordDetailComponent,
    resolve: {
      accord: AccordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.accord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccordUpdateComponent,
    resolve: {
      accord: AccordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.accord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccordUpdateComponent,
    resolve: {
      accord: AccordResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'uvsPartenariatsApp.accord.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
