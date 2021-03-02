import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAccord, Accord } from 'app/shared/model/accord.model';
import { AccordService } from './accord.service';

@Component({
  selector: 'jhi-accord-update',
  templateUrl: './accord-update.component.html',
})
export class AccordUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idAccord: [],
    title: [],
    description: [],
    type: [],
    statut: [],
    dateAccord: [],
  });

  constructor(protected accordService: AccordService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accord }) => {
      if (!accord.id) {
        const today = moment().startOf('day');
        accord.dateAccord = today;
      }

      this.updateForm(accord);
    });
  }

  updateForm(accord: IAccord): void {
    this.editForm.patchValue({
      id: accord.id,
      idAccord: accord.idAccord,
      title: accord.title,
      description: accord.description,
      type: accord.type,
      statut: accord.statut,
      dateAccord: accord.dateAccord ? accord.dateAccord.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accord = this.createFromForm();
    if (accord.id !== undefined) {
      this.subscribeToSaveResponse(this.accordService.update(accord));
    } else {
      this.subscribeToSaveResponse(this.accordService.create(accord));
    }
  }

  private createFromForm(): IAccord {
    return {
      ...new Accord(),
      id: this.editForm.get(['id'])!.value,
      idAccord: this.editForm.get(['idAccord'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      dateAccord: this.editForm.get(['dateAccord'])!.value ? moment(this.editForm.get(['dateAccord'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccord>>): void {
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
}
