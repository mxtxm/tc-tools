## submit audit ##
technician would submit the questionnaire, and the answers after audit is done. related files may be uploaded after.
if a question has image, "photoName" field must be set to file name being uploaded for the answer.  
### url ###
"/ui/hse/audit/questionnaire/submit"
### access ###
* TECHNICIAN
### method ###
POST JSON 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format": "object",
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire",
"key": ["id"],
"includeFields":["id","answers"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## upload image file related to a question ##
* the filename must have been sent with {HseAuditAnswer} as "photoName".
* use for app.
### url ###
"/ui/hse/audit/questionnaire/image/upload"
### access ###
* TECHNICIAN
### method ###
POST (Multi part)
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: HseAuditQuestionnaire.id
* **File file**:
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




## upload image file related to a question by manager/engineer ##
* the filename must have been sent with {HseAuditAnswer} as "photoName".
* not use for app. use for correction.
### url ###
"/ui/hse/audit/questionnaire/image/upload/direct"
### access ###
* MANAGER
* ENGINEER
### method ###
POST (Multi part)
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: HseAuditQuestionnaire.id
* **Long questionId**:
* **File file**:
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



## delete image file related to a question by manager/engineer ##
### url ###
"/ui/hse/audit/questionnaire/image/delete"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **String path**: path of the image e.g. "/static/hse-audit/th91/th91-q1-1.jpg"
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





## delete questionnaire work flow ##
### url ###
"/ui/hse/audit/questionnaire/delete"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
{{
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire",
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




## update questionnaire (work flow) ##
can not change state with this service, must use "/ui/hse/audit/questionnaire/update/state"
### url ###
"/ui/hse/audit/questionnaire/update"
### access ###
* MANAGER
* ENGINEER
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire",
"key": ["id"],
"exclude": ["state","assignable","site"]
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




## update questionnaire work flow state ##
### url ###
"/ui/hse/audit/questionnaire/update/state"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
{{
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire",
"key": ["id"],
"includeFields": ["state","comments"]
}}
* **HseAuditFlowState state**: see HseAuditFlowState valid values
* String comments
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





## search questionnaires ##
* 
### url ###
"/ui/hse/audit/questionnaire/search"
### access ###
* MANAGER
* ENGINEER
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire"
}}
### output ###
{{
"searchResult": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError





## get user assigned questionnaire and sites list ##
* gets list for signed in user
### url ###
"/ui/hse/audit/questionnaire/get/assigned?lang=en&x=a1e4abb7e7ca9a6aa16988a375e54799959997e8"
### access ###
* MANAGER
* ENGINEER
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
### output ###
{{
"format":"object",
"dto":"com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire$ViewableTiny"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError





## get questionnaire ##
### url ###
"/ui/hse/audit/questionnaire/get"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**
### output ###
{{
"format":"object",
"dto": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError
