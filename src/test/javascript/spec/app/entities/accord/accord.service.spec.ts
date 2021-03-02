import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AccordService } from 'app/entities/accord/accord.service';
import { IAccord, Accord } from 'app/shared/model/accord.model';
import { TypeAccord } from 'app/shared/model/enumerations/type-accord.model';
import { StatutAccord } from 'app/shared/model/enumerations/statut-accord.model';

describe('Service Tests', () => {
  describe('Accord Service', () => {
    let injector: TestBed;
    let service: AccordService;
    let httpMock: HttpTestingController;
    let elemDefault: IAccord;
    let expectedResult: IAccord | IAccord[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AccordService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Accord(0, 0, 'AAAAAAA', 'AAAAAAA', TypeAccord.Signer, StatutAccord.National, currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateAccord: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Accord', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateAccord: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAccord: currentDate,
          },
          returnedFromService
        );

        service.create(new Accord()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Accord', () => {
        const returnedFromService = Object.assign(
          {
            idAccord: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            statut: 'BBBBBB',
            dateAccord: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAccord: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Accord', () => {
        const returnedFromService = Object.assign(
          {
            idAccord: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            type: 'BBBBBB',
            statut: 'BBBBBB',
            dateAccord: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateAccord: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Accord', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
