# Source codes #
The source codes are self-explanatory, after understanding the project structure ("structure.md") reading and understanding
the codes does not require additional comments. The codes and follow the principals listed below:

* Codes can be found at "CODE-BASE/src/..."
* Codes are organised inside meaningful packages(namespaces) and classes. the names are chosen to perfectly explain what
  they do. Follow the packages to reach classes.  
* Classes are either services, dtos, controllers, business models or system related, which is visible by the names.
* Indentation and spaces are always respected and standard to make the codes readable.
* Classes are small to be easily readable.
* Methods only do one thing that is clearly shown by their names to be readable and testable. 
* No comment policy: all namings (methods, properties, local variables, parameters, etc.) are meaningful and show their
  purpose to make reading and maintaining the codes easy. Sections of methods are separated by empty lines or on few
  occasions a one line title comment to make understanding and readability reasonable.   
* Java clean code and standard coding conventions and more has been implemented. (see "mt-java-conventions.md")
* Advanced Object Oriented principles and patterns are implemented.
* Separation has been implemented based on "data-entity" classes, "business-models", "webservice-controllers", "services"
  and common classes.     
 

## compile and deploy ##
The whole system is compiled and built into one file named "ROOT.war", it is enough to put this file into tomcat container
to deploy or redeploy the system.

The system is created as service/micro-service based and can be deployed on one server or on many servers. The "ROOT.war"
file contains all the services and depending on the config file they can be enabled or disabled on different servers. 