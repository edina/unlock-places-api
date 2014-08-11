## Unlock Places API

Unlock Places is a place search web service. Its API helps developers to find locations for place names. 
You can use it to convert postal codes into coordinates, to look up electoral boundaries, and to find shapes that overlap or intersect one another. 

The API is written in Java, utilising the Spring framework, and can be deployed as a war file running in Tomcat.

For further information about Unlock Places, please see the Unlock website: http://edina.ac.uk/unlock/places/

## Dependencies

* Tomcat
* Java
* Postgres
* Postgis

## Installation

### Create Unlock Places Database

*TODO: Instructions on how to recreate Unlock database*

Once the database has been created, the database.properties file in src/main/resources/sql/ should be amended with the appropriate database details and added to the lib directory of Tomcat.


### Build Unlock War File

In order to build the war file, you can use Maven:

```mvn -Plocal -DskipTests=false package```

Note that you should either modify src/main/filters/local.properties or create your own properties file (which will need to be added to the profiles section of pom.xml)

If you want to run the tests, ensure that src/test/resources/config/database.properties has been modified to point to your instance of the Unlock database.

Once built, copy the war file to the Tomcat webapps directory and start Tomcat.

To check the Unlock Places API is running as expected, go to http://localhost:8080/unlock-api/ws/search?name=edinburgh, assuming you are running it on port 8080.

