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
  @Input() mealItemNameRating: string;
  @Input() searchInput: string;
  restaurants: Observable<any[]>;
  meals: Observable<any[]>;
  mealItems: Observable<any[]>;
  meals2: Observable<any[]>;

  constructor(public db: AngularFirestore) {
    this.mealItems = db.collection('meal-items').valueChanges();
    this.restaurants = db.collection('restaurants').valueChanges();
    this.meals = db.collection('restaurants').doc('Pasta-Pomodoro').collection('meals').valueChanges();
    this.meals2 = db.collection('restaurants').doc('The-Wing-Company').collection('meals').valueChanges();
  }

  ngOnInit() {
  }

}
