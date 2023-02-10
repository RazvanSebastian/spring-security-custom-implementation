import { CommonModule, DatePipe } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { AdminResourcesComponent } from "./components/admin/admin-resources.component";
import { ItemsTableComponent } from "./components/admin/items-table/items-table.component";
import { UsersTableComponent } from "./components/admin/users-table/users-table.component";
import { UserResourcesComponent } from "./components/user/user-resources.component";
import { NgxPaginationModule } from 'ngx-pagination';
import { SharedModule } from "../shared/shared.module";

@NgModule({
    declarations: [
        UserResourcesComponent,
        AdminResourcesComponent,
        ItemsTableComponent,
        UsersTableComponent
    ],
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
        ReactiveFormsModule,
        NgxPaginationModule,
        SharedModule
    ],
    providers: [DatePipe]
})
export class ResourcesModule { }