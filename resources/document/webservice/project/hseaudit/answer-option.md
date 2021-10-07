## get HseAuditAnswerOption list ##
### url ###
"/ui/hse/audit/answer/option/get"
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
"enumClass":"com.tctools.business.dto.project.hseaudit.HseAuditAnswerOption"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError