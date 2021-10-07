## get province list ##
### url ###
"/ui/provinces/get"
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
"dto":"com.tctools.business.dto.location.Province"
}}
### exceptions ###
* 500 ServerError
* 204 NoContentException




## get province list as key value ##
### url ###
"/ui/provinces/keyval"
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
