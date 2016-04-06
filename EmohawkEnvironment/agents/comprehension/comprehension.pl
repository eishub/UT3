% Percepts
:- dynamic place/3, self/7, destination/1, navigation/1.
% Beliefs
:- dynamic triedWork/0, orderedOne/0, orderedDoubleEspresso/0, jumped/0.
% Goals 
:- dynamic coffee/0, work/0.

% The name of this agent.
myName(Name):- self(_,Name,_,_,_,_,_).
		
% Current location
myLocation(Location) :- self(_,_,Location,_,_,_,_).
		
% Aliases for quick lookup.
home(Location,Range) :- place(barbara_home,Location,Range).
restaurant(Location,Range) :- place(restaurant,Location,Range).
cinema(Location,Range) :- place(cinema,Location,Range).
park(Location,Range) :- place(park,Location,Range).
sphere(Location,Range) :- place(sphere,Location,Range).
		
%Adds first two locations, returns result in third 
locationAdd(location(X1,Y1,Z1),location(X2,Y2,Z2), location(X,Y,Z)) :-  X is X1 + X2, Y is Y1 + Y2, Z is Z1 + Z2.
% Measures distance between two locations.
distance(location(X1,Y1,Z1),location(X2,Y2,Z2),Distance) :- Distance is sqrt((X1 - X2) ** 2 + (Y1 - Y2) ** 2 +(Z1 - Z2) ** 2 ).			
% Holds if the distance between both locations is less then the MaxDistance.
distanceLt(Location1,Location2,MaxDistance) :- distance(Location1,Location2,Distance), Distance < MaxDistance.
% Holds if the agent is within 200 units of the location.
atLocation(Location) :- myLocation(MyLocation), distanceLt(MyLocation,Location,200).
% Alias for barbara_home to make the code easier to read.
at(home) :- at(barbara_home). 