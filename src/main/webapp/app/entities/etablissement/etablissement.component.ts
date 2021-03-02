import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtablissement } from 'app/shared/model/etablissement.model';
import { EtablissementService } from './etablissement.service';
import { EtablissementDeleteDialogComponent } from './etablissement-delete-dialog.component';

@Component({
  selector: 'jhi-etablissement',
  templateUrl: './etablissement.component.html',
})
export class EtablissementComponent implements OnInit, OnDestroy {
  etablissements?: IEtablissement[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected etablissementService: EtablissementService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.etablissementService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IEtablissement[]>) => (this.etablissements = res.body || []));
      return;
    }

    this.etablissementService.query().subscribe((res: HttpResponse<IEtablissement[]>) => (this.etablissements = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEtablissements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IEtablissement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEtablissements(): void {
    this.eventSubscriber = this.eventManager.subscribe('etablissementListModification', () => this.loadAll());
  }

  delete(etablissement: IEtablissement): void {
    const modalRef = this.modalService.open(EtablissementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.etablissement = etablissement;
  }
}
