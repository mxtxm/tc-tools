## get RadioMetricProximityType list ##
### url ###
"/ui/radio/metric/proximity/type/get"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
{{
"format":"list",
"dto":"com.tctools.web.ui.project.radiometric.workflow.RadioMetricProximityType"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError