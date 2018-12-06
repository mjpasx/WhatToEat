import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }   from '@angular/forms';
import { AppComponent } from './app.component';
import { AngularFireModule } from 'angularfire2';
import { FirebaseConfig } from '../environments/firebase.config';
import { AngularFirestoreModule } from 'angularfire2/firestore';
import { AngularFireDatabaseModule } from 'angularfire2/database';
import { ResultsComponent } from './results/results.component';
import { DishComponent } from './dish/dish.component';
import { ReactiveFormsModule } from '@angular/forms';


export const environment = {
    apiKey: "AIzaSyA0Ga4JeU4wO3h_vsW5xWMH_qmjlDQOcxM",
    authDomain: "what-to-eat-219113.firebaseapp.com",
    databaseURL: "https://what-to-eat-219113.firebaseio.com",
    projectId: "what-to-eat-219113",
    storageBucket: "",
    messagingSenderId: "953603850914"
};

  @NgModule({
  declarations: [AppComponent, ResultsComponent, DishComponent],
  imports: [
    BrowserModule,
    FormsModule,
    AngularFireModule.initializeApp(FirebaseConfig.firebase),
    AngularFirestoreModule,
    AngularFireDatabaseModule,
    ReactiveFormsModule

  ],
  providers: [],
  bootstrap: [AppComponent]
  })
export class AppModule { }
