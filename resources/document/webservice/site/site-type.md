## get SiteType list ##
### url ###
"/ui/site/type/get"
### access ###
* VENDOR
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
"dto":"com.tctools.business.dto.site.SiteType"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get SiteType list as key value ##
### url ###
"/ui/site/type/keyval"
### access ###
* VENDOR
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
    JSON
    {"1": "xxxxx", "2": "yyyyyy",...}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
