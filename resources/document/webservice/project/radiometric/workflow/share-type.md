## get ShareType list ##
### url ###
"/ui/radio/metric/share/type/get"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
{{
"enumClass":"com.tctools.business.dto.site.CollocationType"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError