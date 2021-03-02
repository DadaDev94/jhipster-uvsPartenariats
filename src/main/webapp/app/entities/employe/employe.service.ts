import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IEmploye } from 'app/shared/model/employe.model';

type EntityResponseType = HttpResponse<IEmploye>;
type EntityArrayResponseType = HttpResponse<IEmploye[]>;

@Injectable({ providedIn: 'root' })
export class EmployeService {
  public resourceUrl = SERVER_API_URL + 'api/employes';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/employes';

  constructor(protected http: HttpClient) {}

  create(employe: IEmploye): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employe);
    return this.http
      .post<IEmploye>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(employe: IEmploye): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(employe);
    return this.http
      .put<IEmploye>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEmploye>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploye[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEmploye[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(employe: IEmploye): IEmploye {
    const copy: IEmploye = Object.assign({}, employe, {
      hireDate: employe.hireDate && employe.hireDate.isValid() ? employe.hireDate.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.hireDate = res.body.hireDate ? moment(res.body.hireDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((employe: IEmploye) => {
        employe.hireDate = employe.hireDate ? moment(employe.hireDate) : undefined;
      });
    }
    return res;
  }
}
