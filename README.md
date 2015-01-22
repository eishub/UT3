UT3
===

NOT YET READY FOR USE - IN PROGRESS - DO NOT USE 

EIS interface that enables GOAL to connect with Unreal Tournament (both UT3 and UT 2004).

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


