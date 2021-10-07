## get ComplainType list ##
### url ###
"/ui/complain/type/get"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.project.radiometric.complain.ComplainType"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError