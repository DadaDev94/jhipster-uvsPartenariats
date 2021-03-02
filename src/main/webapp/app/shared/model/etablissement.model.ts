import { IDepartement } from 'app/shared/model/departement.model';

export interface IEtablissement {
  id?: number;
  nomEtablissement?: string;
  locationId?: number;
  nomEtablissements?: IDepartement[];
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public nomEtablissement?: string,
    public locationId?: number,
    public nomEtablissements?: IDepartement[]
  ) {}
}
