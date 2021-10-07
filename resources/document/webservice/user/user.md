## get user roles ##
* ROOT: access to whole system (critical system actions, backend and frontend)
* ADMIN: access to everything on (backend and frontend)
* MANAGER: can do everything on all projects (restricted backend and all frontend)
* ENGINEER: can do everything on owned projects, can not do actions on MANAGER (restricted backend and all frontend)
* TECHNICIAN: access to technician allowed parts of project (restricted frontend) 
* VENDOR: access to parts of owned projects. (restricted frontend)
### url ###
"/ui/users/get/roles"
### access ###
* MANAGER
* ENGINEER
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
{{
"format":"object",
"enumClass":"com.tctools.business.dto.user.Role"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get signed in user ##
### url ###
"/ui/user/get/current"
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
"format":"object",
"dto":"com.tctools.business.dto.user.User"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 204 NoContentException




## get user by id ##
get user by id
### url ###
"/ui/user/get/by/id"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
GET
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **Long id**: user id
### output ###
{{
"format":"object",
"dto":"com.tctools.business.dto.user.User"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError
* 204 NoContentException




## get all users ##
* MANAGER can see all projects users
* ENGINEER can only see project users and providing and having access to the "project" is required
### url ###
"/ui/users/all"
### access ###
* MANAGER
* ENGINEER
* VENDOR
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
"includeFields":["project"]
}}
### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.user.User"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get users as key value ##
* MANAGER can see all projects users
* ENGINEER can only see project users and providing and having access to the "project" is required
### url ###
"/ui/users/all/keyval"
### access ###
* MANAGER
* ENGINEER
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
{{
"format":"object",
"dto":"com.tctools.business.dto.user.User",
"includeFields":["project"]
}}
### output ###
    JSON
    [User.id: User.fullName, ...]
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 204 NoContentException




## get technicians ##
get all technicians
### url ###
"/ui/users/technicians/get"
### access ###
* MANAGER
* ENGINEER
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
"includeFields":["project"]
}}
### output ###
{{
"format":"list",
"dto":"com.tctools.business.dto.user.User"
}}
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## get technicians as key value ##
get all technicians
### url ###
"/ui/users/technicians/get/keyval"
### access ###
* MANAGER
* ENGINEER
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
"includeFields":["project"]
}}
### output ###
    JSON
    [User.id: User.fullName, ...]
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 204 NoContentException



## insert new user ##
ENGINEER must have access to project and can not create MANAGER
### url ###
"/ui/site/insert"
### access ###
* MANAGER
* VENDOR
### method ###
POST JSON
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
"exclude": ["role", "emailVerified", "mobileVerified", "createT", "signinT"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "value": inserted-id (long),
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError




## user update ##
ENGINEER must have access to project and can not create MANAGER
### url ###
"/ui/user/update"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
"exclude": ["role", "emailVerified", "mobileVerified", "createT", "signinT"]
}}
### output ###
    JSON
    {
        "code": 200,
        "message"; "update success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError





## delete user ##
### url ###
"/ui/site/delete"
### access ###
* MANAGER
* VENDOR
### method ###
POST
### headers ###
* **String X-Auth-Token**: auth token
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
{{
"dto":"com.tctools.business.dto.user.User",
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




## Submit signature image ##
### url ###
"/ui/user/signature/submit"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
* **File file**
* Long id: if "id" is not provided then signature is assigned to signed in user. MANAGER can upload other user signature by sending "id".
### output ###
    JSON
    {
        "code": 200,
        "message"; "update success message",
        "successful": true,
        "value": signature-url
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams




## Does signature image exists ##
### url ###
"/ui/user/signature/exists"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
### output ###
    JSON
    {
        "code": 200,
        "message"; "exists or not exists message",
        "successful": true,
        "value": signature-url or empty
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError




## change password ##
if "id" is not provided then signed in user's password will be changed. 
MANAGER can change other user passwords by sending "id".
### url ###
"/ui/user/change/password"
### access ###
* MANAGER
* ENGINEER
* VENDOR
* TECHNICIAN
### method ###
POST
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###
* **String password**
* Long id: if "id" is not provided then signature is assigned to signed in user. MANAGER can upload other user signature by sending "id".
### output ###
    JSON
    {
        "code": 200,
        "message"; "update success message",
        "successful": true
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 400 InvalidInputParams
* 500 ServerError