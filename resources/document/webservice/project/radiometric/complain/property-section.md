## get property section list ##
### url ###
"/ui/property/section/get"
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
"dto":"com.tctools.business.dto.project.radiometric.complain.PropertySection"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get property section list as key value ##
### url ###
"/ui/property/section/keyval"
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