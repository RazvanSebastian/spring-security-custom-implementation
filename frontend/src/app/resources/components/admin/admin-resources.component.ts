import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SecuredResourceModel } from '../../model';
import { ResourcesService } from '../../service/resources.service';

@Component({
  selector: 'app-admin-resources',
  templateUrl: './admin-resources.component.html',
  styleUrls: ['./admin-resources.component.css']
})
export class AdminResourcesComponent implements OnInit {

  messageOnGetResource: string;
  isSuccessOnGetResource: boolean;
  resources: SecuredResourceModel[];

  messageOnPostResource: string;
  isSuccessOnPostResource: boolean;
  resourceForm: FormGroup;

  constructor(private resourcesService: ResourcesService, private datePipe: DatePipe) { }

  ngOnInit(): void {
    this.initializeResources();
  }

  initializeResources() {

  }

}
