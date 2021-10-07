## get BtsShare list ##
### url ###
"/ui/site/bts/share/get"
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
"dto":"com.tctools.business.dto.site.BtsShare"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get BtsShare list as key value ##
### url ###
"/ui/site/bts/share/keyval"
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
