## get city list ##
### url ###
"/ui/cities/get"
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
"dto":"com.tctools.business.dto.location.City"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get city list as key value as key value ##
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
    {"1": "Tehran", "2": "Kerman",...}
### exceptions ###
* 500 ServerError
* 204 NoContentException
