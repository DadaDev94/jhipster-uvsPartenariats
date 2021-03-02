import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        loadChildren: () => import('./region/region.module').then(m => m.UvsPartenariatsRegionModule),
      },
      {
        path: 'country',
        loadChildren: () => import('./country/country.module').then(m => m.UvsPartenariatsCountryModule),
      },
      {
        path: 'location',
        loadChildren: () => import('./location/location.module').then(m => m.UvsPartenariatsLocationModule),
      },
      {
        path: 'etablissement',
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.UvsPartenariatsEtablissementModule),
      },
      {
        path: 'departement',
        loadChildren: () => import('./departement/departement.module').then(m => m.UvsPartenariatsDepartementModule),
      },
      {
        path: 'accord',
        loadChildren: () => import('./accord/accord.module').then(m => m.UvsPartenariatsAccordModule),
      },
      {
        path: 'employe',
        loadChildren: () => import('./employe/employe.module').then(m => m.UvsPartenariatsEmployeModule),
      },
      {
        path: 'role',
        loadChildren: () => import('./role/role.module').then(m => m.UvsPartenariatsRoleModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class UvsPartenariatsEntityModule {}
