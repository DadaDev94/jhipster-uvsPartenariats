import { IEmploye } from 'app/shared/model/employe.model';
import { IAccord } from 'app/shared/model/accord.model';

export interface IDepartement {
  id?: number;
  nomDepartement?: string;
  nomDepartmentId?: number;
  employes?: IEmploye[];
  accords?: IAccord[];
  etablissementId?: number;
}

export class Departement implements IDepartement {
  constructor(
    public id?: number,
    public nomDepartement?: string,
    public nomDepartmentId?: number,
    public employes?: IEmploye[],
    public accords?: IAccord[],
    public etablissementId?: number
  ) {}
}
