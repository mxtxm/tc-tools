## get district list ##
### url ###
"/ui/districts/get"
### access ###
PUBLIC
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###

### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.location.District"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get district list as key value ##
### url ###
"/ui/districts/keyval"
### access ###
PUBLIC
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