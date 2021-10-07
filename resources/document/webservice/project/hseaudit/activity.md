## get HseAuditActivity list ##
### url ###
"/ui/hse/audit/activity/get"
### access ###
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
"format":"object",
"enumClass":"com.tctools.business.dto.project.hseaudit.HseAuditActivity"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError