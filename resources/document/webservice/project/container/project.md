## insert new project ##
### url ###
"/ui/project/insert"
### access ###
* MANAGER
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.container.Project",
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





## update project ##
### url ###
"/ui/project/update"
### access ###
* MANAGER
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.container.Project",
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




## delete project ##
### url ###
"/ui/project/delete"
### access ###
* MANAGER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.container.Project",
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




## get project ##
### url ###
"/ui/project/get"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
* VENDOR
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.container.Project",
"exclude": ["all"],
"key": ["id"]
}}
### output ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.container.Project$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## get all projects ##
### url ###
"/ui/projects/search"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###

### output ###
{{
"format":"list",
"dto": "com.tctools.business.dto.project.container.Project$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError



