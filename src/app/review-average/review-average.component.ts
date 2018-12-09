import { Component, OnInit, Input } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { AngularFireDatabase } from 'angularfire2/database';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import * as firebase from 'firebase/app';

interface MealItem {
  description: string;
  mealName: string;
  restaurantName: string;
  review: string;
  sentimentScore: number;
  timestamp: string;
  user: string;
  zipcode: string;
}

@Component({
  selector: 'app-review-average',
  templateUrl: './review-average.component.html',
  styleUrls: ['./review-average.component.css']
})

export class ReviewAverageComponent implements OnInit {
  mealItemsCollection: AngularFirestoreCollection<MealItem>;
  mealItems: Observable<MealItem[]>;
  sentimentScoreArray: Array<String>;
  avgRating: Observable<any>;

  @Input() mealItemName: string;

  constructor(public db: AngularFirestore) { }

  ngOnInit() {
      this.mealItemsCollection = this.db.collection('meal-items');
      this.mealItems = this.mealItemsCollection.valueChanges();
  }

/*
  calculateAvg(sentimentScores: Array): number{
    this.sentimentScoreArray.push(sentimentScores);

    this.avgRating = this.sentimentScoresArray.map(arr => {
      const ratings = arr.map(v => v.value)
      return ratings.length ? ratings.reduce((total, val) => total + val) / arr.length : 'not reviewed'
    })
  }
*/

}
