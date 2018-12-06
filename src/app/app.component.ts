import { Component } from '@angular/core';
import { SearchService } from './search.service';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],

})
export class AppComponent {
  title = 'What To Eat';
  show: boolean = false;
  value = '';
  onEnter(value: string) { this.value = value; }
}
