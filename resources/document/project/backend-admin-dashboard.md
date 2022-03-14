# Backend admin dashboard #
The system has a versatile dashboard which advanced users can use it to monitor and control almost everything in the system.

## Menus ##

### Home ###
Shows an overview of the system, services and memory usage.
### Monitoring ###
* view system error logs
* view user logs
* view services status, last run time
* database status
* monitor online users
* view cached data status 
### Data ###
To view database and perform actions:
* list data
* search and sort data
* add new records
* update records
* delete records
* view and add indexes
* import data
* clear database
### Advanced ###
Advanced features which must be used with caution, the actions in this section can make the system un-usable if not done
correctly
* manage database backup/restore
* database advanced features
* rabbitmq management and queue status view
* manage system settings via dashboard (config.properties and tune.properties) (see "settings.md")
* stop services
* start services
* return to factory reset state
### Schedule ###
To view system's scheduled tasks/jobs and run them manually
### Queries ###
To run and create custome database queries
### Documents ###
To view all backend documentations
### ImportExport ###
Advanced data import exports are done via this menu, for example importing Aras data is done from here
### Photos ###
To view uploaded photos, due huge number of files this feature may be un-usable  
### Tools ###
Tools related to the business model are included here