import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAccord } from 'app/shared/model/accord.model';
import { AccordService } from './accord.service';

@Component({
  templateUrl: './accord-delete-dialog.component.html',
})
export class AccordDeleteDialogComponent {
  accord?: IAccord;

  constructor(protected accordService: AccordService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accordService.delete(id).subscribe(() => {
      this.eventManager.broadcast('accordListModification');
      this.activeModal.close();
    });
  }
}
