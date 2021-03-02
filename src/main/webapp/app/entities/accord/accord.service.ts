import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IAccord } from 'app/shared/model/accord.model';

type EntityResponseType = HttpResponse<IAccord>;
type EntityArrayResponseType = HttpResponse<IAccord[]>;

@Injectable({ providedIn: 'root' })
export class AccordService {
  public resourceUrl = SERVER_API_URL + 'api/accords';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/accords';

  constructor(protected http: HttpClient) {}

  create(accord: IAccord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accord);
    return this.http
      .post<IAccord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(accord: IAccord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accord);
    return this.http
      .put<IAccord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAccord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAccord[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(accord: IAccord): IAccord {
    const copy: IAccord = Object.assign({}, accord, {
      dateAccord: accord.dateAccord && accord.dateAccord.isValid() ? accord.dateAccord.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAccord = res.body.dateAccord ? moment(res.body.dateAccord) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((accord: IAccord) => {
        accord.dateAccord = accord.dateAccord ? moment(accord.dateAccord) : undefined;
      });
    }
    return res;
  }
}
