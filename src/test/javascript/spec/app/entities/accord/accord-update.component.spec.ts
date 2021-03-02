import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { UvsPartenariatsTestModule } from '../../../test.module';
import { AccordUpdateComponent } from 'app/entities/accord/accord-update.component';
import { AccordService } from 'app/entities/accord/accord.service';
import { Accord } from 'app/shared/model/accord.model';

describe('Component Tests', () => {
  describe('Accord Management Update Component', () => {
    let comp: AccordUpdateComponent;
    let fixture: ComponentFixture<AccordUpdateComponent>;
    let service: AccordService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UvsPartenariatsTestModule],
        declarations: [AccordUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AccordUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AccordUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AccordService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Accord(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Accord();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
