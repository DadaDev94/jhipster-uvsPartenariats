import { Moment } from 'moment';
import { IRole } from 'app/shared/model/role.model';

export interface IEmploye {
  id?: number;
  nom?: string;
  prenom?: string;
  email?: string;
  telephone?: string;
  hireDate?: Moment;
  roles?: IRole[];
  managerId?: number;
  departementId?: number;
  accordId?: number;
}

export class Employe implements IEmploye {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public email?: string,
    public telephone?: string,
    public hireDate?: Moment,
    public roles?: IRole[],
    public managerId?: number,
    public departementId?: number,
    public accordId?: number
  ) {}
}
