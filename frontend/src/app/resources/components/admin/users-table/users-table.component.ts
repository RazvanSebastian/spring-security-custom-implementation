import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { ItemsSearchQueries, SecuredResourceModel, Sort, UserInfoModel } from 'src/app/resources/model';
import { ResourcesService } from 'src/app/resources/service/resources.service';

@Component({
  selector: 'app-users-table',
  templateUrl: './users-table.component.html',
  styleUrls: ['./users-table.component.css']
})
export class UsersTableComponent implements OnInit, OnDestroy {
  private unsubscribe$ = new Subject();

  readonly pageSizeSelectOptions = [5, 10, 15];
  readonly pageSortOptions = [Sort.ASC, Sort.DESC];
  readonly selectedResourceModalId = "selectedResourceModal";

  formGroup: FormGroup;

  content: UserInfoModel[];

  modalErrorMessage: string | null = null;

  pageIndex: number = 0;
  totalPages: number = 0;

  constructor(private resourcesService: ResourcesService) { }

  ngOnDestroy(): void {
    this.unsubscribe$.next(true);
    this.unsubscribe$.complete();
  }

  ngOnInit(): void {
    this.initializeFromGroup();
    this.initializeFormGroupListener();
    this.doRequest();
  }

  onPageChange(pageIndex: number) {
    this.pageIndex = pageIndex;
    this.doRequest();
  }

  private initializeFromGroup() {
    this.formGroup = new FormGroup({
      pageSize: new FormControl(this.pageSizeSelectOptions[0]),
      sortDirection: new FormControl(this.pageSortOptions[0]),
      searchedUserName: new FormControl("")
    })
  }

  private initializeFormGroupListener() {
    this.initializeFormControlListener("pageSize");
    this.initializeFormControlListener("sortDirection");
    this.initializeFormControlListener("searchedValue");
    this.initializeFormControlListener("searchedUserName");
  }

  private initializeFormControlListener(formControlName: string) {
    this.formGroup.get(formControlName)?.valueChanges.pipe(
      takeUntil(this.unsubscribe$),
      debounceTime(200),
      distinctUntilChanged((prev, curr) => prev === curr)
    ).subscribe(() => this.doRequest());
  }

  private doRequest() {
    const itemsSearchQueries: ItemsSearchQueries = this.formGroup.value;
    itemsSearchQueries.pageIndex = this.pageIndex;

    this.resourcesService.getUsers(this.formGroup.value).subscribe(result => {
      this.content = result.content;
      this.totalPages = result.totalPages;
      this.pageIndex = result.currentPage;
    });
  }

}
