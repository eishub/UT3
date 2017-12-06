UT3
===

EIS interface that enables GOAL to connect with Unreal Tournament (both UT3 and UT 2004).


How to use
============
Please refer to http://ii.tudelft.nl/trac/goal/wiki/Projects/UT3 for detailed instructions and downloads.

To use this environment, you must have Unreal Tournament 2004 (UT2004) or Unreal Tournament 3 (UT3) installed on your machine.
 * UT2004 is available for Linux, Mac OSX and Windows.
 * UT3 is available only for Windows.
 
Follow the UT Environment manual to install and use this environment.

This manual also details the actions and percepts that are available to the entities.

A Visualizer is also available. It shows an overview map with the locations of all the bots. 


BUILDING
=======

To build this, 

To build the installer, open a command line on OSX. You need to have UT2004 installed in /Volumes/apps/UT2004.

 * ```tcsh```
 * ```setenv pogamut.ut2004.home /Volumes/apps/UT2004```
 * switch to root of the GOAL UT project
 * ```mvn install```


On more recent systems, this does not work because "." is not allowed in environment variable keys. To work around this, you need to do this now

 * ```env "pogamut.ut2004.home=/Volumes/apps/UT2004" bash```
 * switch to root of the GOAL UT project
 * ```mvn install```



 * The ```UT2004.ini``` in your UT installation must be fixed as follows to disable UplinkToGamespy:

```
[IpDrv.MasterServerUplink]
DoUplink=False
UplinkToGamespy=False
SendStats=False
ServerBehindNAT=False
```

With that you can just use ```mvn install``` in the root of the project to build all. 

Or use ```mvn deploy``` to push new release to the repository on github/eishub/mvn-repo.

MAKING INSTALLER
=================

Please refer to Installer README.md
