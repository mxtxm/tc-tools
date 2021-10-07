## get RadioMetricFlowState list ##
### url ###
"/ui/radio/metric/flow/state/get"
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
"enumClass":"com.tctools.business.dto.project.radiometric.workflow.RadioMetricFlowState"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError