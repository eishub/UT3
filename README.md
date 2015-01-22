UT3
===

EIS interface that enables GOAL to connect with Unreal Tournament (both UT3 and UT 2004).


How to use
============
To use this environment, you must have Unreal Tournament 2004 (UT2004) or Unreal Tournament 3 (UT3) installed on your machine.
 * UT2004 is available for Linux, Mac OSX and Windows.
 * UT3 is available only for Windows.
 
Follow the UT Environment manual to install and use this environment.

This manual also details the actions and percepts that are available to the entities.

A Visualizer is also available. It shows an overview map with the locations of all the bots. 


BUILDING
=======

To build this, you must have
 * UT2004 installed
 * set one environment variable to point to your UT2004 installation, eg ```pogamut.ut2004.home=/Volumes/apps/UT2004```
 * The ```UT2004.ini``` in your UT installation must be fixed as follows to disable UplinkToGamespy:

```
[IpDrv.MasterServerUplink]
DoUplink=False
UplinkToGamespy=False
SendStats=False
ServerBehindNAT=False
```

With that you can just use ```mvn install``` in the root of the project to build all. 