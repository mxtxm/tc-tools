## get all questions ##
### url ###
"/ui/hse/audit/questions/get"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
### output ###
{{
"format":"list",
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestion$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException
* 500 ServerError
