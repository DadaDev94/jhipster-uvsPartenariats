import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IEmploye, Employe } from 'app/shared/model/employe.model';
import { EmployeService } from './employe.service';
import { IAccord } from 'app/shared/model/accord.model';
import { AccordService } from 'app/entities/accord/accord.service';
import { IDepartement } from 'app/shared/model/departement.model';
import { DepartementService } from 'app/entities/departement/departement.service';

type SelectableEntity = IAccord | IDepartement;

@Component({
  selector: 'jhi-employe-update',
  templateUrl: './employe-update.component.html',
})
export class EmployeUpdateComponent implements OnInit {
  isSaving = false;
  accords: IAccord[] = [];
  departements: IDepartement[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    email: [],
    telephone: [],
    hireDate: [],
    managerId: [],
    departementId: [],
    accordId: [],
  });

  constructor(
    protected employeService: EmployeService,
    protected accordService: AccordService,
    protected departementService: DepartementService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employe }) => {
      if (!employe.id) {
        const today = moment().startOf('day');
        employe.hireDate = today;
      }

      this.updateForm(employe);

      this.accordService.query().subscribe((res: HttpResponse<IAccord[]>) => (this.accords = res.body || []));

      this.departementService.query().subscribe((res: HttpResponse<IDepartement[]>) => (this.departements = res.body || []));
    });
  }

  updateForm(employe: IEmploye): void {
    this.editForm.patchValue({
      id: employe.id,
      nom: employe.nom,
      prenom: employe.prenom,
      email: employe.email,
      telephone: employe.telephone,
      hireDate: employe.hireDate ? employe.hireDate.format(DATE_TIME_FORMAT) : null,
      managerId: employe.managerId,
      departementId: employe.departementId,
      accordId: employe.accordId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employe = this.createFromForm();
    if (employe.id !== undefined) {
      this.subscribeToSaveResponse(this.employeService.update(employe));
    } else {
      this.subscribeToSaveResponse(this.employeService.create(employe));
    }
  }

  private createFromForm(): IEmploye {
    return {
      ...new Employe(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      hireDate: this.editForm.get(['hireDate'])!.value ? moment(this.editForm.get(['hireDate'])!.value, DATE_TIME_FORMAT) : undefined,
      managerId: this.editForm.get(['managerId'])!.value,
      departementId: this.editForm.get(['departementId'])!.value,
      accordId: this.editForm.get(['accordId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmploye>>): void {
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
}
