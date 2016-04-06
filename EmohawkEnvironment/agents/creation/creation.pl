% Percepts
:- dynamic person/7, place/3, self/7, destination/1, navigation/1.
% Beliefs
:- dynamic displayedEmoticons1/0, displayedEmoticons2/0, displayedEmoticons3/0, turnedToOther/0, visitedCinema/0, visitedPark/0, waitedForOther/0.      

 % My name
myName(Name):- self(_,Name,_,_,_,_,_).
		
% Current location
myLocation(Location) :- self(_,_,Location,_,_,_,_).
			
% Aliases for quick lookup.
restaurant(Location,Range) :- place(restaurant,Location,Range).
cinema(Location,Range) :- place(cinema,Location,Range).
park(Location,Range) :- place(park,Location,Range).
sphere(Location,Range) :- place(sphere,Location,Range).
	
% The location for the meeting in the park
parkMeetingJan(Location) :- place(park,ParkLocation,Range),locationAdd(ParkLocation,location(100,0,0),Location) .
parkMeetingPier(Location) :- place(park,ParkLocation,Range),locationAdd(ParkLocation,location(-100,0,0),Location) .
		
% The location for this agent.
parkMeeting(Location) :-  myName('Jan'),parkMeetingJan(Location).
parkMeeting(Location) :-  myName('Pier'),parkMeetingPier(Location).
		
% The location where the other guy will stand
parkMeetingOther(Location) :- myName('Jan'),parkMeetingPier(Location).
parkMeetingOther(Location) :- myName('Pier'),parkMeetingJan(Location).

% The location for the meeting in the cinema.
cinemaMeetingJan(Location) :- place(cinema,CinemaLocation,Range),locationAdd(CinemaLocation,location(100,0,0),Location) .
cinemaMeetingPier(Location) :- place(cinema,CinemaLocation,Range),locationAdd(CinemaLocation,location(-100,0,0),Location) .
		
%Location where this agent will stand.
cinemaMeeting(Location) :-  myName('Jan'),cinemaMeetingJan(Location).
cinemaMeeting(Location) :-  myName('Pier'),cinemaMeetingPier(Location).

% Info about the other guy
personAt(Name,Location) :- person(_,Name,PersonLocation,_,_,_,_), distanceLt(Location,PersonLocation,80).
otherAtParkMeeting :- myName('Jan'), parkMeetingPier(Location), personAt('Pier',Location).
otherAtParkMeeting :- myName('Pier'), parkMeetingJan(Location), personAt('Jan',Location).
		
%Adds first two locations, returns result in third 
locationAdd(location(X1,Y1,Z1),location(X2,Y2,Z2), location(X,Y,Z)) :-  X is X1 + X2, Y is Y1 + Y2, Z is Z1 + Z2.
% Measures distance between two locations.
distance(location(X1,Y1,Z1),location(X2,Y2,Z2),Distance) :- Distance is sqrt((X1 - X2) ** 2 + (Y1 - Y2) ** 2 +(Z1 - Z2) ** 2 ).			
% Holds if the distance between both locations is less then the MaxDistance.
distanceLt(Location1,Location2,MaxDistance) :- distance(Location1,Location2,Distance), Distance < MaxDistance.
% Holds if the agent is within 200 units of the location.
atLocation(Location) :- myLocation(MyLocation), distanceLt(MyLocation,Location,200).
% Holds if the agent is within the radius of the place or 80 units from the given location.
at(Place) :- place(Place,Location,Range),myLocation(MyLocation), distanceLt(Location,MyLocation,Range).

noEmoticonsActive :- self(_,_,_,_,none,none,none).