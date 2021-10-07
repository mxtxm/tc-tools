## signin ##
### url ###
"/ui/user/signin"
### access ###
PUBLIC
### method ###
POST
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
### params ###
* **String username**: email or mobile
* **String password**:
### output ###
    JSON
    {
        "code": 200,
        "message": "success message",
        "successful": true
        "value": {User}
    }
    
user (value) token property is required for subsequent dashboard requests
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 400 InvalidInputParams




## signout ##
### url ###
"/ui/user/signout"
### access ###
PUBLIC
### method ###
GET
### headers ###
* String X-Lang: (default=SYSTEM-DEFINED-LANG) "fa" or "en" or ...
* **String X-Auth-Token**: auth token
### params ###

### output ###

### exceptions ###
