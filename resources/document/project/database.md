# Database #
The database entities are defined inside the projects as simple Java classes called DTO (data transfer object).
The DTOs can be found inside the "/tc-tools/src/com/tctools/business/dto/" directory.
Data entities are grouped based on their logics, each group is a directory, there can be sub-groups (inner directories).



## Database ##
Using vantar framework there is no need to manually create databases. Databases are set in the system configuration file
and DTO objects contain all the information needed to create the database, relations, constraints, ets. Vantar framework
creates and handles the database. The system is flexible and based on the future requirements and changes, updating DTO
objects is mostly enough to match the needs. In this project MongoDB is used as the DBMS. Although there are logical 
referencing between the DTOs the data is stored Object Oriented rather than relational thus ERD is meaningless. 

### setting up database ###
* install the latest version of required DBMS (MongoDB) (see /deploy/deployment.md)
* create user/password on mongo (if required)
* to override default settings update database parameters inside system configuration file
  ("/opt/tc-tools/config.properties"). "config.properties" can be copied from the project "/resources/config.properties"
* see "/deploy/setting.md"

### database administration ###
The database can be accessed via backend administration panel. Only ROOT users can access this panel.

* sign into backend using root user > "BACKEND-BASE-URL/admin"
* do database actions 

#### view data entities (DTOs) ####
Goto "Data" menu, you can see the data groups and the data entities. for each entity the following actions can be done: 

* **List** view a list of data records
* **Search** (when listing data) search data records (when listing data) 
* **Sort** (when listing data) sort data records
* **View Log** (when listing data) view a history of all actions that have been performed on a record, contains user data,
  IP, headers, data changes, the date, etc...
* **New** add a new record
* **Update** (when listing data) update data
* **Delete** (when listing data) delete data
* **Import** bulk data import, some entities contain default built-in import data
* **Delete all data** delete all the records of the entity

#### view/update indexes ####
Goto "Data" menu. find the link to the database index page, you can view the indexes and create/re-create the indexes.
Creating/re-creating the indexes is safe and will not corrupt the data.

#### view sequence (auto-increments) ####
Goto "Data" menu. find the link to the database sequence page, you can view the sequences for DTO primary keys. Changing
the data can result on data corruption or system malfunction. This feature must only be used if there is a good reason.

#### backup/restore ####
Goto "Advanced" menu. Find the DBMS (Mongo), there are three links:
* **Create backup** to manually create a backup from the whole database.
* **Restore backup** to select and open a backup, this will overwrite the current database.
* **Backup files** view the backup files, download or delete them

The backup files (".dump") are created inside or restored from "/opt/tc-tools/backup/"

#### auto backup ####
By default a backup is created from the database every 12H. Auto backup configuration settings can be found and changed
inside "/resources/tune.properties".

#### Queries ####
Goto "Queries" menu.
You can create, run and store queries on the database in (JSON format). The queries are only to get data, changing data
is not allowed. To findout how to create a query JSON see "/document/webservice/common/search.md". 

#### Other database options ####
Goto "Advanced" or "Monitoring" menu. You can find other database options such as database status view. Actions such as
"factory resetting" or "init data importing" will over write existing data and are not recommended to do.



## groups ##
* "system": contains data entities which are not related to the business such as system settings and test data.
* "user": contains data entities that keep user data and roles.
* "location": contains data entities that keep location data, data dictionaries such as cities, provinces, etc.
* "site": contains site/node/bts data and site data related data dictionaries.
* "project":
    * "container": contains data entities which define system projects.
    * "hseaudit": contains data entities that keep data related to HSE Audit.
    * "radiometric": contains data entities that keep data related to Radiometric.    
  

## DTO (data entities) ##
A DTO is an object that defines a data entity, in other words everything about a data entity and the data which is going
to be stored is modeled there, also the instances of the dto are directly stored inside the database and data is queried
from the database as the DTO. The object definition contains all the required information for the Vantar framework to
create database schema and do all the data related actions. This system has the following advantages over the traditional
relational model:
* Object Oriented system, Object Oriented data. there is no need for ORMs to adapt an Object Oriented system to a flat
  relational database.
* Performance gain because the objects can be stored directly.
* Simplicity: there is no need to work with the database separately, everything is defined and viewed inside the syste,/  
* Integrated cache system at framework level. 
* Flexibility: changing the DTOs does not break the system, customer requirements can be implemented easier at any stage.
* This system is no DBMS related, with Vantar SQL and NOSQL databases can both be used for a system, but NOSQL databases
  bring more benefits.
* Multiple DTOs can be linked to a database entity, based on different actions and queries suitable DTOs can be created
  and linked to a database entity
* Collection, Map, List, Location, other DTOs, etc. objects can be stored in the database as a DTO data field.

### Review DTOs ### 
Goto "CODE-BASE/tc-tools/src/com/tctools/business/dto/" for each group find the DTOs.

Each DTO is a Java class, when stored in the database the class-name will be the Collection/Table name as underscored case.
The DTO class may have the following annotations:
* **@Mongo** The Dto is stored on MongoDB
* **@Elastic** The Dto is stored on Elastic search (NA in this project)
* **@Sql** The Dto is stored on PostgreSql/Oracle/etc (NA in this project)
* **@NoStore** The Dto is not stored, some DTOs are volatile and are not stored on a database 
* **@Cache** The data of a DTO with this annotation are cached, this results in super fast data fetching while database
  actions remain the same, synchronizing with the database and cache managing is done automatically by the Vantar framework  
* **@Storage("Name")** To manually set the database collection/table name, this is usefull when different DTOs are linked
  to the same data entity. 
* **@Index({"property:type,..."}** To define database indexes which must be set on the database on the data entity. 

The properties of a DTO are the data fields, the field types can be string, number, boolean, enum, Date/Time, Location,
List, Map, nested DTO objects, etc. Constraints, validation hints, dependant DTOs, relations and such additional data
can be set to a property by annotations. A sample DTO may look like below:  
    
    @Cache
    @Mongo
    @Index({"username:1"})
    public class User extends DtoBase implements CommonUser, CommonUserPassword {
    
        public Long id;
    
        @Required
        public AccessStatus accessStatus;
    
        @Required
        public Role role;
    
        public List<ProjectType> projectTypes;
    
        @PresentBy("Province")
        public List<Long> provinceIds;
    
        @Required
        public String firstName;
        @Required
        public String lastName;
        @Tags("none")
        public String fullName;
    
        public String email;
        public String mobile;
        public String username;
        public String password;
    
        @CreateTime
        @Timestamp
        @Tags("none")
        public DateTime createT;
    
        @UpdateTime
        @Timestamp
        public DateTime signinT;
    
        @NoStore
        public String signature;
    
        @NoStore
        public String token;
    }
