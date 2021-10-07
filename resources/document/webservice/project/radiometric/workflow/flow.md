# radio metric flow #
### TECHNICIAN ###
* get lists (new tasks, finished tasks, submitted tasks...)
    * "/ui/radio/metric/flow/get/tasks/new"
    * "/ui/radio/metric/flow/get/tasks/finished"
    * "/ui/radio/metric/flows/search"
* get data of a task
    * "/ui/radio/metric/flow/get",
* update data
    * "/ui/radio/metric/flow/update"
* submit or update measurements  
    * "/ui/radio/metric/measurement/submit"
* upload image(s)
    * "/ui/radio/metric/image/upload"
* select finished items and commit (set state to completed will, after this technician can not update record)
    * "/ui/radio/metric/flow/commit"



## submit radio metric measurement ##
technician may update his works many times.
### url ###
"/ui/radio/metric/measurement/submit"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
POST (multi-part) 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
"exclude": ["all"],
"key": ["id"]
}}

* Upload file-100: (measurement file) csv file
* Upload file-150: (measurement file) csv file
* Upload file-170: (measurement file) csv file

* Upload file-health-facility: (proximity image) jpg file
* Upload file-educational-institution: (proximity image) jpg file
* Upload file-operator-site: (proximity image) jpg file
* Upload file-health-facility1: (proximity image) jpg file (index 1 to 5)
* Upload file-educational-institution1: (proximity image) jpg file (index 1 to 5)
* Upload file-operator-site1: (proximity image) jpg file (index 1 to 5)

* Upload file-tower-view: (site image) jpg file
* Upload file-probe-view-in-front-of-measured-sector: (site image) jpg file
* Upload file-tripod-in-location: (site image) jpg file
* Upload file-address: (site image) jpg file
* Upload file-extra1: (site image) jpg file
* Upload file-extra2: (site image) jpg file
   
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




## upload images ##
to update images
### url ###
"/ui/radio/metric/image/upload"
### access ###
* TECHNICIAN
* MANAGER
* ENGINEER
### method ###
POST (multi-part) 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
"exclude": ["all"],
"key": ["id"]
}}

* Upload file-health-facility: (proximity image) jpg file
* Upload file-educational-institution: (proximity image) jpg file
* Upload file-operator-site: (proximity image) jpg file
* Upload file-health-facility1: (proximity image) jpg file (index 1 to 5)
* Upload file-educational-institution1: (proximity image) jpg file (index 1 to 5)
* Upload file-operator-site1: (proximity image) jpg file (index 1 to 5)

* Upload file-tower-view: (site image) jpg file
* Upload file-probe-view-in-front-of-measured-sector: (site image) jpg file
* Upload file-tripod-in-location: (site image) jpg file
* Upload file-address: (site image) jpg file
* Upload file-extra1: (site image) jpg file
* Upload file-extra2: (site image) jpg ile
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




## delete work flow ##
deletes work-flow record and associated complain record if exists
### url ###
"/ui/radio/metric/flow/delete"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
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




## update state ##
    // Role:MANAGER > Role:TECHNICIAN
    Planned : manager has created and assigned a task to a technician

    // Role:TECHNICIAN > Role:MANAGER
    Unworkable : technician informs this task is not possible
    Restricted : technician informs this task is not doable because it requires permission
    Completed : technician informs this task is done

    // Role:MANAGER > Role:TECHNICIAN
    Revise : manager did not accept the work, sent back to technician
    Terminated : manager has terminated the work for this technician

    // Role:MANAGER > Role:VENDOR
    Verified : manager has verified and accepted the work, sent to vendor to approve

    // Role:VENDOR > Role:MANAGER
    Returned : vendor did not accept the work, rejected to manager

    // Role:VENDOR
    Approved : vendor has approved the work, finish
### url ###
"/ui/radio/metric/flow/update/state"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
{{
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
"includeFields":["id","state","comments"],
"key":["id"]
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



## update data ##
### url ###
"/ui/radio/metric/flow/update"
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
"dto": "com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
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




## commit tasks ##
when technician is done updating data/submitting measurements then she/he can commit the tasks to go to manager's view
### url ###
"/ui/radio/metric/flow/commit"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **String ids**: list of comma separated flow ids e.g: "2,34,244,4"
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




## get a site measurement work flow ##
### url ###
"/ui/radio/metric/flow/get"
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
"format":"object",
"dto":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable",
"exclude": ["all"],
"key": ["id"]
}}
### output ###
{{
"format":"object",
"dto":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## search radiometric flows ##
### url ###
"/ui/radio/metric/flows/search"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"searchParams":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable"
}}
### output ###
{{
"searchResult":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## get technicians new tasks ##
### url ###
"/ui/radio/metric/flow/get/tasks/new"
### access ###
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## get technicians finished tasks ##
### url ###
"/ui/radio/metric/flow/get/tasks/finished"
### access ###
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow$Viewable"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 204 NoContentException
* 500 ServerError




## remove assigned site from technician ##
### url ###
"/ui/radio/metric/assign/remove"
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
"format":"object",
"dto":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlow",
"exclude": ["all"],
"key": ["id"]
}}
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





## delete image file ##
can delete radiometric flow and complain images
### url ###
"/ui/radio/metric/image/delete"
### access ###
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **String path**: path of the image e.g. "/static/xxxxxx"
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




## delete measurement log ##
### url ###
"/ui/radio/metric/log/delete"
### access ###
* MANAGER
* ENGINEER
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **Long id**: work flow id
* **Integer height**: (100, 150, 170) measurement height  
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