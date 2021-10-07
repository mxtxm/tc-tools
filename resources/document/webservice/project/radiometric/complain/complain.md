# Complain flow #
* A customer complain (complain type = CustomerComplain) or a normal measurement request (complain type = NormalRequest)
  is submitted. "/ui/radio/metric/complain/insert"
* Submitted complains are searched and viewed.
  "/ui/radio/metric/complains/search" "/ui/radio/metric/complain/get"
* A complain can be updated or deleted by a manager or an engineer (only when assigneeId == null). 
  "/ui/radio/metric/complain/update" "/ui/radio/metric/complain/delete"
* A complain can be assigned to a technician by a manager or an engineer.
    * when assigned a new work flow is created and assigned to a technician, which can be seen in technician page by a
      query on workflow data based on technician's user id.
    * assigned complain can be removed (by workflow id).
        * workflow last state will become Canceled
        * complain will become assignable



## insert new complain ##
### url ###
"/ui/radio/metric/complain/insert"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST (multi-part) 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain",
"exclude": ["id","location"]
}}
* Double location_latitude:
* Double location_longitude:
* Upload file: jpg file
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





## update complain ##
### url ###
"/ui/radio/metric/complain/update"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
POST (multi-part) 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain",
"key": ["id"],
"exclude": ["location"]
}}
* Double location_latitude:
* Double location_longitude:
* Upload file: jpg file
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




## delete complain ##
deletes complain record and associated work-flow record
### url ###
"/ui/radio/metric/complain/delete"
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
"dto": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain",
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




## get complain ##
### url ###
"/ui/radio/metric/complain/get"
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
"dto": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain",
"exclude": ["all"],
"key": ["id"]
}}
### output ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## search complains ##
### url ###
"/ui/radio/metric/complains/search"
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
"searchParams": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain"
}}
### output ###
{{
"format":"object",
"searchResult": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## get assignable complains ##
### url ###
"/ui/radio/metric/complains/assignable"
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

### output ###
{{
"format":"list",
"searchResult": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException
* 500 ServerError




## get assigned complains ##
### url ###
"/ui/radio/metric/complains/assigned"
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
### output ###
{{
"format":"list",
"searchResult": "com.tctools.business.dto.project.radiometric.complain.RadioMetricComplain$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException
* 500 ServerError




## assign complain to technician ##
### url ###
"/ui/radio/metric/complain/assign"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long complainId**:
* **Long assigneeId**: user id
* String comments:
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException
* 500 ServerError




## remove assigned complain from technician ##
### url ###
"/ui/radio/metric/complain/assign/remove"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: workFlowId 
* String comments:
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException
* 500 ServerError
