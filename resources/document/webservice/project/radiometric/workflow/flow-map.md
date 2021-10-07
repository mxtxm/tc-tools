## search sites ##
### url ###
"/ui/radio/metric/flows/search/map"
### access ###
* MANAGER
* ENGINEER
### method ###

### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow"
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




## assign site to technician on radiometric project task ##
### url ###
"/ui/radio/metric/site/assign"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
"includeFields":["siteId","assigneeId","comments"]
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
* 204 NoContentException
* 500 ServerError





## create pending child from a flow ##
### url ###
"/ui/radio/metric/create/child"
### access ###
* MANAGER
* ENGINEER
### method ###

### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow"
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