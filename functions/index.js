const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref("Filmler/{id}").onWrite((change, context) => {

	var text = change.after.child('film_adi').val();
	var image =change.after.child('image').val();
	var tur =change.after.child('film_tur').val();
	
	var payload ={
		data:{
			title:text,
			image:image,
			tur:tur,
		}
	};

	//return admin.messaging().sendToTopic("Casual",payload);
});