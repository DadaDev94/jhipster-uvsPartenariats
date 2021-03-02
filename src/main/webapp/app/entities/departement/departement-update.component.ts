import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IDepartement, Departement } from 'app/shared/model/departement.model';
import { DepartementService } from './departement.service';
import { IEtablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/etablissement.service';
import { IAccord } from 'app/shared/model/accord.model';
import { AccordService } from 'app/entities/accord/accord.service';

type SelectableEntity = IEtablissement | IAccord;

@Component({
  selector: 'jhi-departement-update',
  templateUrl: './departement-update.component.html',
})
export class DepartementUpdateComponent implements OnInit {
  isSaving = false;
  nomdepartments: IEtablissement[] = [];
  accords: IAccord[] = [];
  etablissements: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    nomDepartement: [null, [Validators.required]],
    nomDepartmentId: [],
    accords: [],
    etablissementId: [],
  });

  constructor(
    protected departementService: DepartementService,
    protected etablissementService: EtablissementService,
    protected accordService: AccordService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ departement }) => {
      this.updateForm(departement);

      this.etablissementService
        .query({ filter: 'departement-is-null' })
        .pipe(
          map((res: HttpResponse<IEtablissement[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IEtablissement[]) => {
          if (!departement.nomDepartmentId) {
            this.nomdepartments = resBody;
          } else {
            this.etablissementService
              .find(departement.nomDepartmentId)
              .pipe(
                map((subRes: HttpResponse<IEtablissement>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IEtablissement[]) => (this.nomdepartments = concatRes));
          }
        });

      this.accordService.query().subscribe((res: HttpResponse<IAccord[]>) => (this.accords = res.body || []));

      this.etablissementService.query().subscribe((res: HttpResponse<IEtablissement[]>) => (this.etablissements = res.body || []));
    });
  }

  updateForm(departement: IDepartement): void {
    this.editForm.patchValue({
      id: departement.id,
      nomDepartement: departement.nomDepartement,
      nomDepartmentId: departement.nomDepartmentId,
      accords: departement.accords,
      etablissementId: departement.etablissementId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const departement = this.createFromForm();
    if (departement.id !== undefined) {
      this.subscribeToSaveResponse(this.departementService.update(departement));
    } else {
      this.subscribeToSaveResponse(this.departementService.create(departement));
    }
  }

  private createFromForm(): IDepartement {
    return {
      ...new Departement(),
      id: this.editForm.get(['id'])!.value,
      nomDepartement: this.editForm.get(['nomDepartement'])!.value,
      nomDepartmentId: this.editForm.get(['nomDepartmentId'])!.value,
      accords: this.editForm.get(['accords'])!.value,
      etablissementId: this.editForm.get(['etablissementId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepartement>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  getSelected(selectedVals: IAccord[], option: IAccord): IAccord {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
