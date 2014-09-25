This project installs eis to the repository to solve serveral issues:

1. 	Because GOAL is build with EIS included. It can't be included in any environments 
	that are loaded by goal. This is solved by declaring the dependency scope to be provided.
	
2.	While http://sourceforge.net/projects/apleis/ is using maven, the project is not portable 
	to any build server. As such we include it here as our own.
	
3. 	Because neither GOAL nor apleis use a clear version numbering, we include the version used 
	by goal.
	
	
Environments that need EIS can use this:
	
<dependency>
	<groupId>apleis</groupId>
	<artifactId>eis</artifactId>
	<version>0.3r120</version>
	<scope>provided</scope>
</dependency>

Other projects can use 
<dependency>
	<groupId>apleis</groupId>
	<artifactId>eis</artifactId>
	<version>0.3r120</version>
</dependency>

When a new version of EIS is released put it in and commit it to svn or run mvn install.