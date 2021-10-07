## insert new site ##
### url ###
"/ui/site/insert"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format":"object",
"dto": "com.tctools.business.dto.site.Site",
"exclude": ["id"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "value": inserted-id (long),
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError





## update site ##
### url ###
"/ui/site/update"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format":"object",
"dto": "com.tctools.business.dto.site.Site",
"key": ["id"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## delete site ##
### url ###
"/ui/site/delete"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.site.Site",
"exclude": ["all"],
"key": ["id"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## get site ##
### url ###
"/ui/site/get"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* Long id
* String code

one of id or code is required 
### output ###
{{
"format":"object",
"dto": "com.tctools.business.dto.site.Site$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## search sites ##
### url ###
"/ui/sites/search"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams": "com.tctools.business.dto.site.Site"
}}
### output ###
{{
"searchResult": "com.tctools.business.dto.site.Site$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## site select auto complete ##
use for autocomplete based on site code
### url ###
"/ui/sites/autocomplete"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **String code**: part of site code
### output ###
{{
"searchResult": "com.tctools.business.dto.site.Site$ViewableTiny"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError





## get sites near a point ##
### url ###
"/ui/sites/near"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Double latitude**:
* **Double longitude**:
* Double radius: in meters, default=1000m
### output ###
{{
"searchResult": "com.tctools.business.dto.site.Site$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 400 InvalidInputParams
