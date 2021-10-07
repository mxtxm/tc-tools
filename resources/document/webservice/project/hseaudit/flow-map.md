## search questionnaires ##
### url ###
"/ui/hse/audit/flows/search/map"
### access ###
* MANAGER
* ENGINEER
### method ###

### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams": "com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire"
}}
### output ###
    JSON
    [
        {
            Long id;
            Boolean isAssignable: if true allow to be assigned to a user
            
            site: {
                Long id:
                String code:
                String name:
                Double[] location:
            }
            Long subContractorId:
    
            assignor: {
                Long id:
                String name:
            }
            assignee: {
                Long id:
                String name:
            }
            HseAuditFlowState lastState:
    
            DateTime lastStateDateTime:
            DateTime assignDateTime:
            DateTime scheduledDateTimeFrom:
            DateTime scheduledDateTimeTo:
            DateTime auditDateTime:
        }
    ]
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError





## assign site to technician (HSE auditor) ##
### url ###
"/ui/hse/audit/site/assign"
### access ###
* MANAGER
* ENGINEER
### method ###

### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto":"com.tctools.business.dto.project.hseaudit.HseAuditQuestionnaire",
"includeFields":["id","assigneeId","subContractorId","activity","scheduledDateTimeFrom","scheduledDateTimeTo"]
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
* 204 NoContentException





## create pending child from a flow ##
### url ###
"/ui/hse/audit/create/child"
### access ###
* MANAGER
* ENGINEER
### method ###

### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
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
* 204 NoContentException
