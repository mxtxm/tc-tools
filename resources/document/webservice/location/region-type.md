## get region type list ##
### url ###
"/ui/regions/get"
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
"dto":"com.tctools.business.dto.location.Region"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get region type list as key value ##
### url ###
"/ui/regions/keyval"
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
