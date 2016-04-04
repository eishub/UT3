		% navigation(Status,Destination). Initially, we are going nowhere.
		navigation(none, none).
		
		% DO NOT REMOVE OR MODIFY the pickupWeapons predicate nor the maxLevelOfArmour predicate!
		% We use this to keep track of the list of weapons that the bot picked up so far. 
		pickedupWeapons([]).
		% We use this to keep track of the value of armour. 
		maxLevelOfArmour(0).