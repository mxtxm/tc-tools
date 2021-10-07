## get property list ##
### url ###
"/ui/properties/get"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###

### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.project.radiometric.complain.PropertyType"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get property list as key value ##
### url ###
"/ui/properties/keyval"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###

### output ###
    JSON
    {1: "value1", 2: "value2", ...}
### exceptions ###
* 500 ServerError
* 204 NoContentException