## get project types (names) list ##
### url ###
"/ui/project/type/get"
### access ###
* MANAGER
* VENDOR
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
{{
"enumClass":"com.tctools.business.dto.project.container.ProjectType"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError