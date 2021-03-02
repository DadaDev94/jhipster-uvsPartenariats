import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UvsPartenariatsSharedModule } from 'app/shared/shared.module';
import { AccordComponent } from './accord.component';
import { AccordDetailComponent } from './accord-detail.component';
import { AccordUpdateComponent } from './accord-update.component';
import { AccordDeleteDialogComponent } from './accord-delete-dialog.component';
import { accordRoute } from './accord.route';

@NgModule({
  imports: [UvsPartenariatsSharedModule, RouterModule.forChild(accordRoute)],
  declarations: [AccordComponent, AccordDetailComponent, AccordUpdateComponent, AccordDeleteDialogComponent],
  entryComponents: [AccordDeleteDialogComponent],
})
export class UvsPartenariatsAccordModule {}
