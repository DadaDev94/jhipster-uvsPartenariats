import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UvsPartenariatsTestModule } from '../../../test.module';
import { AccordDetailComponent } from 'app/entities/accord/accord-detail.component';
import { Accord } from 'app/shared/model/accord.model';

describe('Component Tests', () => {
  describe('Accord Management Detail Component', () => {
    let comp: AccordDetailComponent;
    let fixture: ComponentFixture<AccordDetailComponent>;
    const route = ({ data: of({ accord: new Accord(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UvsPartenariatsTestModule],
        declarations: [AccordDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AccordDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AccordDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load accord on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.accord).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
