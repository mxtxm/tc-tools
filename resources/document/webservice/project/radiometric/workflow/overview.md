# Overview #


## get overview ##

### url ###
"/ui/radio/metric/overview/state"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "Verified": a,
        "Approved": b,
        "Completed": c,
        "Revise": d,
        "Planned": e,
        "Pending": f,
        "Problematic": g
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError



## get monthly overview ##

### url ###
"/ui/radio/metric/overview/state/monthly"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "year month": {"completed": a, "verified": b, "approved": c},
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError





## get monthly overview with targets ##

### url ###
"/ui/radio/metric/overview/state/monthly/target"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "year month": {"completed": a, "verified": b, "approved": c, "total": t, "target": s},
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## get total overview by radio metric type CC / Regular ##

### url ###
"/ui/radio/metric/overview/type"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "CC/Regular": count
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## get technician monthly performance overview  ## 

### url ###
"/ui/radio/metric/performance/tech/monthly"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **date from**:
* **date to**:
* String userids: technician user id(s) i.e: userids=12,3537
### output ###
    JSON
    {
        "technician name": {
            "year - month name": {
                "yearMonth": int yyyymm
                "completed": int
                "verified": int
                "approved": int
                "total": int
            }
        },
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## get province monthly performance overview  ## 

### url ###
"/ui/radio/metric/performance/province/monthly"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **date from**:
* **date to**:
* String provinceids: province id(s) i.e: userids=1,3
### output ###
    JSON
    {
        "province name": {
            "year - month name": {
                "yearMonth": int yyyymm
                "completed": int
                "verified": int
                "approved": int
                "total": int
            }
        },
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError






## get province total performance overview  ## 

### url ###
"/ui/radio/metric/performance/province"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* String provinceids: province id(s) i.e: userids=1,3
### output ###
    JSON
    {
        "province name": int,
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError





## get technician total performance ##
### url ###
"/ui/radio/metric/performance/tech"
### access ###
* VENDOR
* MANAGER
* ENGINEER
* TECHNICIAN
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* String provinceids: province id(s) i.e: userids=1,3
### output ###
    JSON
    {
        "user name": int,
    }
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
