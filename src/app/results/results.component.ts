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
/*
    //retrieve docs with the right sentimentScore
    this.sentimentSCore = db.collection("meal-items").where("sentimentScore", "==", true)
      .get()
      .then(function(querySnapshot) {
          querySnapshot.forEach(function(doc) {
              console.log(doc.id, " => ", doc.data());
          });
      })
      .catch(function(error) {
          console.log("Error getting documents: ", error);
      });

 (onCreate)="calculateAvg()
  calculateAvg() {
    var sum = 0;
    for( var i = 0; i < elmt.length; i++ ){
        sum += parseInt( elmt[i], 10 ); //don't forget to add the base
    }
    var avg = sum/elmt.length;
  }
*/
  ngOnInit() {
  }
}
