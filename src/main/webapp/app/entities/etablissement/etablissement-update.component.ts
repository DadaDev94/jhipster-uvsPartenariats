import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IEtablissement, Etablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from './etablissement.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  locations: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    nomEtablissement: [null, [Validators.required]],
    locationId: [],
  });

  constructor(
    protected etablissementService: EtablissementService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);

      this.locationService
        .query({ filter: 'etablissement-is-null' })
        .pipe(
          map((res: HttpResponse<ILocation[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ILocation[]) => {
          if (!etablissement.locationId) {
            this.locations = resBody;
          } else {
            this.locationService
              .find(etablissement.locationId)
              .pipe(
                map((subRes: HttpResponse<ILocation>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ILocation[]) => (this.locations = concatRes));
          }
        });
    });
  }

  updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtablissement: etablissement.nomEtablissement,
      locationId: etablissement.locationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  private createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      locationId: this.editForm.get(['locationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  trackById(index: number, item: ILocation): any {
    return item.id;
  }
}
