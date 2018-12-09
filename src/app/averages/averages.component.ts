import { Component, OnInit, Input } from '@angular/core';
import { AngularFirestore } from 'angularfire2/firestore';
import { AngularFireDatabase } from 'angularfire2/database';
import { Observable } from 'rxjs/Observable';
import * as firebase from 'firebase/app';

@Component({
  selector: 'app-averages',
  templateUrl: './averages.component.html',
  styleUrls: ['./averages.component.css']
})
export class AveragesComponent implements OnInit {
  @Input() mealItemName: string;
  restaurants: Observable<any[]>;

  constructor(public db: AngularFirestore) {
    this.restaurants = db.collection('restaurants').valueChanges();
  }

  ngOnInit() {
  }

}
