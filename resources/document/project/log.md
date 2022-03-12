# Log #
There are three types of logs


## System error logs ##
Stores system errors that occure and are important to be checked.
These logs are stored inside the database.
* Goto backend admin dashboard
* Goto menu "Monitoring"
* Goto link "System errors"

or
* Goto backend admin dashboard
* Goto menu "Data"
* Goto "Log" data


## User action logs ##
Stores all actions a user does on the system. It can be viewed by user or by data records/
These logs are stored inside the database.

* Goto backend admin dashboard
* Goto menu "Monitoring"
* Goto link "User actions" and "Request/Response logs"

or
* Goto backend admin dashboard
* Goto menu "Data"
* Goto "UserLog" data

or see the history log of all actions performed on any record in the database
* Goto backend admin dashboard
* Goto menu "Data"
* Goto "List" of the data entity
* in the list click on "Log"


## Code error/info/debug logs ##
The framework and system logs are stored in log data and can be used for debugging and tracing. by default settings these
logs are stored in:

* /opt/tc-tools/log/ 
* /var/log/tomcatX/log

The log configs are set inside "CODE-BASE/log4j2.xml" though it is recommended not to change this file.