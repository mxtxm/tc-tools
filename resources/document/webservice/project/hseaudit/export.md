## download site hse audit report ##
download a zip file
### url ###
"/ui/hse/audit/data"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: id
### output ###
    File zip
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## download site hse audit report of many sites ##
download a zip file
### url ###
"/ui/hse/audit/data/many"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **String dateMin**:
* **String dateMax**:
* String states: default=Approved or states separated by ","
### output ###
    File zip
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## download daily report ##
download an excel file
### url ###
"/ui/hse/audit/daily/report"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###

### output ###
    File excel
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## question by province ##
get data or download an excel file
### url ###
"/ui/hse/audit/question/province"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {
        "questionStatistics": {
            "question": {
                {
                    "province name": {
                        "yes": int count yes
                        "no": int count no
                        "na": int count na
                        "option3": int count option3
                        "option4": int count option4
                        "option3Label": String Option3 question name
                        "option4Label": String Option4 question name
                    }
                },
                {
                    "other questions",
                }
            }
        },
        "provinceOrdered": {
            "name": province name
            "order": int sort order        
        } 
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## question by subcontractor ##
get data or download an excel file
### url ###
"/ui/hse/audit/question/subcontractor"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {
        "questionStatistics": {
            "question": {
                {
                    "subcontractor name": {
                        "yes": int count yes
                        "no": int count no
                        "na": int count na
                        "option3": int count option3
                        "option4": int count option4
                        "option3Label": String Option3 question name
                        "option4Label": String Option4 question name
                    }
                },
                {
                    "other questions",
                }
            }
        },
        "subcontractorOrdered": {
            "name": subcontractor name
            "order": int sort order        
        } 
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError






## question significance by subcontractor ##
get data or download an excel file
### url ###
"/ui/hse/audit/significance/subcontractor"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {

    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError





## question significance by province ##
get data or download an excel file
### url ###
"/ui/hse/audit/significance/subcontractor/province"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {

    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError






## question significance by province ##
get data or download an excel file
### url ###
"/ui/hse/audit/significance/province"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {
        "provinceName": {
            "critical": int,
            "major": int,
            "safe": int,
            "unsafe": int,
            "negativeScore": int,
            "auditCount": int
        },
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError





## question significance by month ##
get data or download an excel file
### url ###
"/ui/hse/audit/significance/month"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Boolean excel**: true=download excel file, false=get data
* **DateTime from**
* **DateTime to**
* String provinceids: ids separated by ',' 1,2,3
* String subcontractorids: ids separated by ',' 1,2,3
### output ###
    File excel
    JSON
    {
        "month": {
            "critical": int,
            "major": int,
            "safe": int,
            "unsafe": int,
            "negativeScore": int,
            "provinceAuditCount": int
        },
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError
