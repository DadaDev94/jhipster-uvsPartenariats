import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { UvsPartenariatsTestModule } from '../../../test.module';
import { EtablissementComponent } from 'app/entities/etablissement/etablissement.component';
import { EtablissementService } from 'app/entities/etablissement/etablissement.service';
import { Etablissement } from 'app/shared/model/etablissement.model';

describe('Component Tests', () => {
  describe('Etablissement Management Component', () => {
    let comp: EtablissementComponent;
    let fixture: ComponentFixture<EtablissementComponent>;
    let service: EtablissementService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UvsPartenariatsTestModule],
        declarations: [EtablissementComponent],
      })
        .overrideTemplate(EtablissementComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EtablissementComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EtablissementService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Etablissement(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.etablissements && comp.etablissements[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
