import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEmploye } from 'app/shared/model/employe.model';
import { EmployeService } from './employe.service';

@Component({
  templateUrl: './employe-delete-dialog.component.html',
})
export class EmployeDeleteDialogComponent {
  employe?: IEmploye;

  constructor(protected employeService: EmployeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeListModification');
      this.activeModal.close();
    });
  }
}
