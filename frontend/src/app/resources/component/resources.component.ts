import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ResourcesService } from '../service/resources.service';

@Component({
  selector: 'app-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['./resources.component.css']
})
export class ResourcesComponent implements OnInit {

  messageOnGetResource: string;
  isSuccessOnGetResource: boolean;

  messageOnPostResource: string;
  isSuccessOnPostResource: boolean;
  resourceForm: FormGroup;

  constructor(private resourcesService: ResourcesService, private datePipe: DatePipe, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.resourceForm = this.formBuilder.group({
      'resource': ['', [Validators.required]]
    })
  }

  onGetResource() {
    this.resourcesService.getResource()
      .subscribe({
        next: () => {
          this.messageOnGetResource = `Received resource at ${this.datePipe.transform(new Date(), 'medium')}`;
          this.isSuccessOnGetResource = true
        },
        error: (errorResponse) => {
          this.messageOnGetResource = JSON.parse(errorResponse.error).message;
          this.isSuccessOnGetResource = false;
        }
      })
  }

  onSaveResource() {
    this.resourcesService.postResource(this.resourceForm.value.resource).subscribe({
      next: () => {
        this.resourceForm.reset();
        this.isSuccessOnPostResource = true;
        this.messageOnPostResource = `Saved resource at ${this.datePipe.transform(new Date(), 'medium')}`;
      },
      error: (errorResponse) => {
        this.messageOnPostResource = errorResponse.error.message;
        this.isSuccessOnPostResource = false;
      }
    });
  }

}
