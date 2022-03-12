# Structure #
Other than the required services that need to be installed on the server (see "/deploy/deployment.md") the whole source
codes, default configuration files, documentations, initial-import-data and what ever is needed to compile and run the 
system is listed and explained in this documentation.

Understanding this documentation is fundamental to understand how the codes and system works. 

Any file or directory which is in the codebase that is not listed below is considered temprory and can be removed,
for example when compiling the build files are put inside a directory called "target".

## Directories and files ##
* **resources** contains files that are not Java source codes
    * **data** contains init data imports for some of the data entities
    * **document** contains all the backend documentations
    * **arta** PHP framework, few actions like relational database synchronization are done using this framework
    * **config.properties** this file is the default main system config file, to override the settings in this file,
      copy this file into "/opt/tc-tools/config.properties" and change the settings there. (see "settings.md")
    * **tune.properties** this file is the default additional system config file, to override the settings in this file,
      copy this file into "/opt/tc-tools/tune.properties" and change the settings there. (see "settings.md")
    * **log4j2.xml** this file is the log system config file, to change log stages and log files update this file before
      building the project (see "log.md")
* **src** contains Java source codes, after the common name-space:
    * **common** contains classes which are not a part of the business but common to the project
        * **Application** System service starter and shutdown listener, the steps that must run when the service starts
          up are defined here. the steps that must run when the service shuts down are defined here.
        * **DtoInfo** DTO manifest, each DTO must be registered in this manifest to be visible
        * **Param** contains static values common to other modules
        * **Config** interface to the system configuration file
        * **Tune** interface to the system configuration file
        * **util** utility libraries contains common to the project
            * **Docx** contains methods for working with microsoft word files and format
            * **Excel** contains methods for working with microsoft excel files and format
            * **ExportCommon** contains methods common to the reports and export data 
            * **HtmlParser** HTML parser used for data conversion
            * **ModelUtil** contains methods common for repeated business model actions
            * **SendMessage** SMS send library
    * **web** contains controller classes. each controller is related to only one business model, the controller maps
      webservice URLs to controller methods. each controller method checks the user access (which Roles and permissions
      can pass throughout it, if user does not have permission a 4XX permission error is thrown) if permission is passed
      then a method from the business model is called to execute the request. the business method may throw a user error
      4XX or a system 5XX error or return the requested data or the result of the requested action, the error or data is
      packed into a response pack(JSON/string/file/etc. and response headers and status) by the controller method and is
      sent back to the web service caller.
        * **ui** contains controllers which make the system API used by UI services/apps 
        * **patch** contains controllers which where used for data conversions and applying patches
        * **webtest** contains controler for the built in web tester
    * **business** contains classes that model the business
        * **dto** contains classes which define the database, each class is a data entity (DataTransferObject). the DTOs
          are grouped by their logical relations. (see "database.md")
        * **model** contains classes which implement the business, each model method is called by a controller, runs the
          business and accesses data using DTO objects.
        * **service** contains modules that act as a service, a service runs parallel in it's own threads
        * **admin** contains admin modules related to the business, the modules are extended to the default Vantar admin
          dashboard. each module consists of a controller and model. 
* **web** contains the CSS, Javascript and fonts required for Backend dashboard, also contains script for a built-in
  web-service tester.
* **pom.xml** project MAVEN build script. contains reference to the dependency libraries, and the compile/build recipe.
   the IDE compiles the project and includes the dependencies using this script.

## Business modules ##
Each module consists of a set of DTO classes, a set of controllers and a set of models. usually these three layers are
grouped by directories that are the same. the groups are  listed below:

* **location** module that exposes actions on location data such as provinces and cities
* **site** module that exposes sites/bts data
* **user** module to access/define users and roles
* **project**
    * **container** module that define system projects
    * **hseaudit**: HSE Audit business implementation, data entry, data query, data export and report generators, map data
      provider, image upload and work flow. 
    * **radiometric**: Radiometric business implementation, data entry, data query, data export and report generators,
      map data provider, complain workflow, log upload, image upload and work flow. 