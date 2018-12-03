import { Component, OnInit } from '@angular/core';
import { AngularFirestore } from 'angularfire2/firestore';
import { AngularFireDatabase } from 'angularfire2/database';
import { Observable } from 'rxjs/Observable';
import * as firebase from 'firebase/app';

@Component({
  selector: 'app-dish',
  templateUrl: './dish.component.html',
  styleUrls: ['./dish.component.css']
})
export class DishComponent implements OnInit {

  mealItems: Observable<any[]>;
  constructor(public db: AngularFirestore) {
    this.mealItems = db.collection('meal-items').valueChanges();
  }

  ngOnInit() {
  }

}
