import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
    selector: 'app-pagination',
    templateUrl: './pagination.component.html',
    styleUrls: ['./pagination.component.css']
})
export class PaginationComponent {
    array: boolean[];
    selected: number = 0;

    private _totalPages: number;

    @Input()
    set totalPages(totalPages: number) {
        this._totalPages = totalPages;
        this.array = Array(this.totalPages).fill(false);
        this.array[0] = true;
        this.selected = 0;
    }

    get totalPages() {
        return this._totalPages;
    }

    @Output()
    pageChange = new EventEmitter<number>();

    selectPage(index: number) {
        this.array[this.selected] = false;
        this.array[index] = true;
        this.selected = index;
        this.pageChange.emit(this.selected);
    }

    previous() {
        if (this.selected > 0) {
            this.array[this.selected] = false;
            this.array[this.selected - 1] = true;
            this.selected--;
            this.pageChange.emit(this.selected);
        }
    }

    next() {
        if (this.selected < this.totalPages - 1) {
            this.array[this.selected] = false;
            this.array[this.selected + 1] = true;
            this.selected++;
            this.pageChange.emit(this.selected);
        }
    }
}
