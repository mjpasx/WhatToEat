(function() {

  // Initialize Firebase
  var config = {
    apiKey: "AIzaSyA0Ga4JeU4wO3h_vsW5xWMH_qmjlDQOcxM",
    authDomain: "what-to-eat-219113.firebaseapp.com",
    databaseURL: "https://what-to-eat-219113.firebaseio.com",
    projectId: "what-to-eat-219113",
    storageBucket: "",
    messagingSenderId: "953603850914"
  };

  firebase.initializeApp(config);

  //angular fire set up
  angular
    .module('app', ['firebase'])
    .controller('MyCtrl', function($firebaseObject) {
      const rootRef = firebase.database().ref().child('angular');
      const ref = rootRef.child('object');
      this.object = $firebaseObject(ref);
    });

}());
