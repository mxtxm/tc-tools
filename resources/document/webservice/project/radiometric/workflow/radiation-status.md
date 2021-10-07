## get RadioMetricRadiationStatus list ##
### url ###
"/ui/radio/metric/radiation/state/get"
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
"dto":"com.tctools.web.ui.project.radiometric.workflow.RadioMetricRadiationStatus"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError