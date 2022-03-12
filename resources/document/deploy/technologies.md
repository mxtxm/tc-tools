# Backend

## Technologies

* Java
    * Why use java?
        * One of the best languages for developing high performance enterprise solutions.
        * Infinite number of open-source and free libraries.
        * Great facilities for creating fast and efficient code.
        * Multi platform.

* Vantar open source Java framework
    * Developed by Mehdi Torabi and Iman Heidari
    * Custom full stack framework for creating efficient Java applications and services
    using agile methodologies. Keywords:
        * Features:
            * Creating testable, standard modules and services using Java
            * Creating multi-threaded services and modules.
            * Creating advanced data objects and models.
            * Sophisticated data cache system to reduce database calls and improve performance.
            * Advanced queue management interface.
            * MVC.
            * Creating services and microservices.
            * Multi database support (SQL/NOSQL).
            * Database independent data modeling and logic building.
            * Sophisticated JSON query builder for easy UI/APP integration.
        * Fast development: using the available reliable modules and libs build within the framework.
        * Write less do more: the framework manages anything that it can, developer does not have to deal
            with repetitive and obvious stuff.
        * Clean code: forces developers to use strict industry standards to make codes readable, efficient,
            understandable and usable, great teamwork and re-usability.
        * Flexible: lets changing anything even the data structures possible with minimal impact, at all
            project phases.
        * Bug-less: small simple code leading to less complexity and less bugs.
        * Low level Admin panel: a low level admin panel only available for super user lets monitoring and
            managing the system. some features:
            * System/Server/Service performance monitoring.
            * System/Server/Service error monitoring (logs).
            * User management.
            * Factory-reset.
            * Database management: creating indexes, manual/scheduled backups, restore/download backups.
            * Database data: all database data can be viewed and manipulated.
            * Creating custom database queries.
            * Queue monitoring and management.
            * Viewing system documents online (formatted md files).
            * Custom menus based on project.

* Tomcat
    * Standard Java opensource web server.
    
* MongoDB (reference: https://www.tutorialspoint.com/mongodb/mongodb_advantages.htm)
    * Advantages of MongoDB over RDBMS
        * Schema less − MongoDB is a document database in which one collection holds different documents. Number of fields,
          content and size of the document can differ from one document to another.
        * Structure of a single object is clear.
        * No complex joins.
        * Deep query-ability. MongoDB supports dynamic queries on documents using a document-based query language that's
          nearly as powerful as SQL.
        * Tuning.
        * Ease of scale-out − MongoDB is easy to scale.
        * Conversion/mapping of application objects to database objects not needed.
        * Uses internal memory for storing the (windowed) working set, enabling faster access of data.
        * Document Oriented Storage − Data is stored in the form of JSON style documents.
        * Index on any attribute.
        * Replication and high availability.
        * Auto-Sharding.
        * Rich queries.
        * Fast in-place updates.
        * Vertical(hardware) and horizontal(more servers can be added) scaling.
        * In the race, Mango DB stands in the first position compared to Oracle DB because MongoDB is much easier to handle
          during the migrations because it is schemaless in nature.
        * MongoDB's performance is better than Oracle, and it could be even faster if sharded the right way. 
    * Downsides
        * Less Mongo experts.
        * More memory usage.
        * No support for SQL language.
        * There is a limit for document size, i.e. 16mb.
        * There is no transaction support in older MongoDB, new versions support but it is not as easy as SQL.
    * Who uses MongoDB
        * MongoDB is most often used by companies with 10-50 employees and 1M-10M dollars in revenue.
        * Uber, CISCO, DHL, ebay, Google, Adobe, BOSCH; (see: https://www.featuredcustomers.com/vendor/mongodb/customers)

* Elastic Search
    * Opensource big data storing system with many search features.
    * For user log storing and search. MongoDb can be used too. When implementing the better system will be choosen. 
    
* RabbitMQ
    * RabbitMQ is the most widely deployed open source message broker. RabbitMQ is lightweight and easy to deploy on premises
      and in the cloud. It supports multiple messaging protocols. RabbitMQ can be deployed in distributed and federated
      configurations to meet high-scale, high-availability requirements. 
    * For queuing, messaging, broadcasting, load balancing, serial processing, etc.

* PHP
    Limited use only when PHP has better opensource libs for some Microsoft office and photo editing tasks.


## Architecture

Service/Microservice. Backend consists of services which interact with each other via REST/RESTFull API or queue messages.
When bottle-neck processing exists queuing is implemented for load balancing and resources management. With queue tasks
are serialized and one or more workers process the tasks in parallel.

A NoSQL database system is used which is common between the services.

Services may interact with each other by a message broadcasting service. 

Services, database and queue can be installed on one server or multiple servers or virtual machines or managed by a framework
such as Docker and Kubernetes.


## Deployment
SEE deployment.md