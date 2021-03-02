import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepartement } from 'app/shared/model/departement.model';
import { DepartementService } from './departement.service';
import { DepartementDeleteDialogComponent } from './departement-delete-dialog.component';

@Component({
  selector: 'jhi-departement',
  templateUrl: './departement.component.html',
})
export class DepartementComponent implements OnInit, OnDestroy {
  departements?: IDepartement[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected departementService: DepartementService,
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
      this.departementService
        .search({
          query: this.currentSearch,
        })
        .subscribe((res: HttpResponse<IDepartement[]>) => (this.departements = res.body || []));
      return;
    }

    this.departementService.query().subscribe((res: HttpResponse<IDepartement[]>) => (this.departements = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDepartements();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDepartement): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDepartements(): void {
    this.eventSubscriber = this.eventManager.subscribe('departementListModification', () => this.loadAll());
  }

  delete(departement: IDepartement): void {
    const modalRef = this.modalService.open(DepartementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.departement = departement;
  }
}
