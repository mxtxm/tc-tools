## get HseAuditFlowState list ##
### url ###
"/ui/hse/audit/flow/state/get"
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
"enumClass":"com.tctools.business.dto.project.hseaudit.HseAuditFlowState"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError