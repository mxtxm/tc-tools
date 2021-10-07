# Export #
* These webservices output file for download and must be called directly as a link.
* Do not call by AJAX


## download site report and files ##
download a zip file
### url ###
"/ui/radio/metric/site/zip"
### access ###
* VENDOR
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: radiometric flow id
### output ###
    File zip
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## download site report document ##
download a word docx file
### url ###
"/ui/radio/metric/site/docx"
### access ###
* VENDOR
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **Long id**: radiometric flow id
### output ###
    File docx
### exceptions ###
* 204 NoContentException
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError





## download assigned sites excel ##
download am excel file
### url ###
"/ui/radio/metric/site/assigned/excel"
### access ###
* VENDOR
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




## download wave control report ##
download an excel
### url ###
"/ui/radio/metric/report/wavecontrol/excel"
### access ###
* VENDOR
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
