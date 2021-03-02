export interface ILocation {
  id?: number;
  address?: string;
  siteInternet?: string;
  siteLocal?: string;
  telephone?: number;
  countryId?: number;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public address?: string,
    public siteInternet?: string,
    public siteLocal?: string,
    public telephone?: number,
    public countryId?: number
  ) {}
}
