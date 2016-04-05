MAKING INSTALLER
=================


The installer does not build automatically when you do mvn install/package/deploy.
This is because the installer is too big to be pushed to github. 

To build the installer, open a command line on OSX. You need to have UT2004 installed in /Volumes/apps/UT2004.

 * ```tcsh```
 * ```setenv pogamut.ut2004.home /Volumes/apps/UT2004```
 * switch to root of the GOAL UT project
 * ```mvn install```
 * ```cd Installer``` 
 * ```mvn package -Dusername=xxxxx -Dpassword=xxxxx``` (username and password for SVN goal access)
