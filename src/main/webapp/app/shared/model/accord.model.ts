import { Moment } from 'moment';
import { IEmploye } from 'app/shared/model/employe.model';
import { IDepartement } from 'app/shared/model/departement.model';
import { TypeAccord } from 'app/shared/model/enumerations/type-accord.model';
import { StatutAccord } from 'app/shared/model/enumerations/statut-accord.model';

export interface IAccord {
  id?: number;
  idAccord?: number;
  title?: string;
  description?: string;
  type?: TypeAccord;
  statut?: StatutAccord;
  dateAccord?: Moment;
  bies?: IEmploye[];
  departements?: IDepartement[];
}

export class Accord implements IAccord {
  constructor(
    public id?: number,
    public idAccord?: number,
    public title?: string,
    public description?: string,
    public type?: TypeAccord,
    public statut?: StatutAccord,
    public dateAccord?: Moment,
    public bies?: IEmploye[],
    public departements?: IDepartement[]
  ) {}
}
