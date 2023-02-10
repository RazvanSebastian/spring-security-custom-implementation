import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import * as bootstrap from 'bootstrap';
import { debounceTime, distinctUntilChanged, Subject, takeUntil } from 'rxjs';
import { ItemsSearchQueries, SecuredResourceModel, Sort } from 'src/app/resources/model';
import { ResourcesService } from 'src/app/resources/service/resources.service';

@Component({
  selector: 'app-items-table',
  templateUrl: './items-table.component.html',
  styleUrls: ['./items-table.component.css']
})
export class ItemsTableComponent implements OnInit, OnDestroy, AfterViewInit {
  private unsubscribe$ = new Subject();
  private modal: bootstrap.Modal | null = null;
  private selectedResource: SecuredResourceModel;

  readonly pageSizeSelectOptions = [5, 10, 15];
  readonly pageSortOptions = [Sort.ASC, Sort.DESC];
  readonly selectedResourceModalId = "selectedResourceModal";

  formGroup: FormGroup;

  content: SecuredResourceModel[];

  modalErrorMessage: string | null = null;

  pageIndex: number = 0;
  totalPages: number = 0;

  constructor(private resourcesService: ResourcesService) { }

  ngAfterViewInit(): void {
    const modalElement = document.getElementById(this.selectedResourceModalId);
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement)
    }
  }

  ngOnInit(): void {
    this.initializeFromGroup();
    this.initializeFormGroupListener();
    this.doRequest();
  }

  ngOnDestroy(): void {
    this.unsubscribe$.next(true);
    this.unsubscribe$.complete();
  }

  onPageChange(pageIndex: number) {
    this.pageIndex = pageIndex;
    this.doRequest();
  }

  onSelectedItem(resource: SecuredResourceModel) {
    this.modalErrorMessage = null;
    this.modal?.show();
    this.selectedResource = resource;
  }

  onDeleteSelectedResource() {
    this.resourcesService.deleteResource(this.selectedResource.id).subscribe({
      next: () => {
        this.modal?.hide();
        this.doRequest();
      },
      error: () => this.modalErrorMessage = "Something went wrong"
    })
  }

  private initializeFromGroup() {
    this.formGroup = new FormGroup({
      pageSize: new FormControl(this.pageSizeSelectOptions[0]),
      sortDirection: new FormControl(this.pageSortOptions[0]),
      searchedValue: new FormControl(""),
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

    this.resourcesService.getAllResource(this.formGroup.value).subscribe(result => {
      this.content = result.content;
      this.totalPages = result.totalPages;
      this.pageIndex = result.currentPage;
    });
  }

}

