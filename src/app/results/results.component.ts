import { Component, OnInit, Input } from '@angular/core';
import { AngularFirestore } from 'angularfire2/firestore';
import { AngularFireDatabase } from 'angularfire2/database';
import { Observable } from 'rxjs/Observable';
import * as firebase from 'firebase/app';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})

export class ResultsComponent implements OnInit {
  show: boolean = false;
  mealItems: Observable<any[]>;

  @Input() searchInput: string;

  constructor(public db: AngularFirestore) {
    this.mealItems = db.collection('meal-items').valueChanges();
  }

  ngOnInit() {
  }
}
