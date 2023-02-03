import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SecuredResourceModel } from '../model';
import { ResourcesService } from '../service/resources.service';

@Component({
  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {

  messageOnGetResource: string;
  isSuccessOnGetResource: boolean;
  resources: SecuredResourceModel[];

  messageOnPostResource: string;
  isSuccessOnPostResource: boolean;
  resourceForm: FormGroup;

  constructor(private resourcesService: ResourcesService, private datePipe: DatePipe, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.initializeResources();
    this.resourceForm = this.formBuilder.group({
      'resource': ['', [Validators.required]]
    })
  }

  initializeResources() {
    this.resourcesService.getUserResource()
      .subscribe({
        next: (resources) => {
          this.isSuccessOnGetResource = true;
          this.resources = resources;
        },
        error: (errorResponse) => {
          this.messageOnGetResource = JSON.parse(errorResponse.error).message;
          this.isSuccessOnGetResource = false;
        }
      })
  }

  onSaveResource() {
    this.resourcesService.saveUserResource(this.resourceForm.value.resource).subscribe({
      next: (newResource) => {
        this.resourceForm.reset();
        this.isSuccessOnPostResource = true;
        this.messageOnPostResource = `Saved resource at ${this.datePipe.transform(new Date(), 'medium')}`;
        this.resources = [...this.resources, newResource];
      },
      error: (errorResponse) => {
        this.messageOnPostResource = JSON.parse(errorResponse.error).message;
        this.isSuccessOnPostResource = false;
      }
    });
  }

}
