## state count ##
i.e:
* Incomplete: 7
* PreApproved: 17
* Completed: 12
* Approved: 89
* Expired: 107
* Planned: 2
* Terminated: 19
* Pending: 32011
### url ###
"/ui/hse/audit/state/aggregate"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        StateName: count,
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## activity count ##
i.e:
* Modernization: 7
* Swap: 17
* NewSite: 12
* PM: 89
* Relocation: 107
* Planned: 2
* MakingSafe: 1
### url ###
"/ui/hse/audit/activity/aggregate"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        Activity: count,
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## province audit count ##
i.e:
* گلستان: 7
* کرمانشاه: 17
* خوزستان: 12
### url ###
"/ui/hse/audit/province/aggregate"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "province1":{"Approved":3, "PreApproved":14},
        "province2":{"Approved":4, "PreApproved":1, "Completed": 2}
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError




## user work count ##
i.e:
* 1400-02-04	{"state1": 2, "state3": 32}
* 1400-02-19	{"state2": 12, "state1": 1}
* 1400-02-05	{"state3": 32}
* 1400-02-20	{"state1": 52}
### url ###
"/ui/hse/audit/user/done/aggregate"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###
* **Long userId**
### output ###
    JSON
    {
        day: {state: count},
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 400 InvalidInputParams




## all users work count ##
i.e:
{
    "14||negahban||علیرضا نگهبان":
        {"1400-01":{"Approved":1},"1399-12":{"PreApproved":4},"1399-10":{"Approved":1,"PreApproved":25},"1399-11":{"Approved":4,"PreApproved":7}},
    "67||forutan||پویا فروتن نیا":
        {"1400-02":{"Approved":4,"PreApproved":1}},
    "58||chitsaz||فاطمه چیت ساز":
        {},
}
### url ###
"/ui/hse/audit/users/done/aggregate"
### access ###
* MANAGER
* ENGINEER
### method ###
GET 
### headers ###
* **String X-Auth-Token**: auth token
### params ###

### output ###
    JSON
    {
        "userId||userName||userFullname": {
            month: {state: count},
        }
    }
### exceptions ###
* 401/403 AuthError/AuthPermissionError
* 500 ServerError
* 400 InvalidInputParams
