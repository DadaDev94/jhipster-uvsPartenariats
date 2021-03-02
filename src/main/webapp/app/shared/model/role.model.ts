export interface IRole {
  id?: number;
  posteOccup?: string;
  fonction?: string;
  employeId?: number;
}

export class Role implements IRole {
  constructor(public id?: number, public posteOccup?: string, public fonction?: string, public employeId?: number) {}
}
