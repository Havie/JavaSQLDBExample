
If you have not already done so, follow the Setup Instruction on Canvas 
https://uwsto.instructure.com/courses/362332/pages/software-setup?module_item_id=9214085

 Connect your project to the JavaFX user library that was created in the above setup
•	Select project from the Java View
•	Right click on properties
•	Select Java Build Path
•	Select ClassPath
•	Click Add Library
•	Select User Library
•	Select CS358JavaFX 
•	Click Finish
•	Apply and Close
•	Restart Eclipse

To add a new JavaFX GUI page to Prof Richardt existing project:
•	File->new->Other->New FXML Document
•	Open with Scene Builder & Edit

Update the run configuration to use the JavaFX libraries
•	Click on Run -> Run Configurations...  -> Java Application
•	Create a new launch configuration for your project.
•	Add these VM arguments:
--module-path C:\JavaFX\javafx-sdk-11.0.2\lib --add-modules javafx.controls,javafx.fxml


Install JDBC Connect to the Database In order to use JDBC to connect to the database
•	Download "connector-java-client-2.7.1.jar" from https://downloads.mariadb.org/connector-java
•	Select project from the Java View
•	Right click on properties
•	Select Java Build Path
•	Select ClassPath
	Add the jar to the classpath c:\folder\mariadb-java-client-2.7.2;

•	Do not share your admin password with other teams.


Source Control on GitHub
•	This repository is read-only.  
•	Each team will need to setup their own clone to work on for the project
•	Clones can be created from read-only repositories even if they are not templates.
