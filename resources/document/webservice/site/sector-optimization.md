## get SectorOptimization list ##
### url ###
"/ui/site/sector/optimization/get"
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
"dto":"com.tctools.business.dto.site.SectorOptimization"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get SectorOptimization list as key value ##
### url ###
"/ui/site/sector/optimization/keyval"
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
