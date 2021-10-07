## get sub contractor data ##
### url ###
"/ui/hse/audit/subcontractor/get"
### access ###
* MANAGER
* ENGINEER
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**
### output ###
{{
"format":"object",
"dto":"com.tctools.business.dto.project.hseaudit.SubContractor$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## search subcontractors, also use for autocomplete ##
### url ###
"/ui/hse/audit/subcontractors/search"
### access ###
* MANAGER
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams": "com.tctools.business.dto.project.hseaudit.SubContractor"
}}
### output ###
{{
"format":"object",
"searchResult": "com.tctools.business.dto.project.hseaudit.SubContractor$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError
