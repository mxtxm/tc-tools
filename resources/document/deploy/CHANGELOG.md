# TC-Tools #
* https://www.rf-emf.nak-mci.ir


## Todo ##
* Normal ---> Regular
* date time --> handle
todo: pahbad ertefa faghat yekie
TODO: KHALABAN=PARTOKAR
TODO: karshenase tahiye konande (texti bashe)
TODO: khalaban = partokar
todo: word gozaresh -> 6 ---> ax near medical o ammozeshi biad age dasht
* todo: add type=drone to all workflow services 
* todo: add type=drone to all reports services
* todo: add type=drone word template
* todo: add type=drone to all ui pages
* todo: cc-> normal ---> eine normal barkhord shavad
* todo: GL0756 --> complain normal ---> map ---> cc nabashe
* TODO: siteLocation ---> BASHE
* WAVE CONTROL ---> CC NORMAL --> CC NABAHSE
* ADDRESS DAGHIGH --> CITY PROVINCE TAHESH MIOFTE
* verified as excel
* near site --> when state is pending does not show 
* word ---> form
* min --> null
* 5 ax drone
* beja adadhaye ghermez noghteh chin


## 2024-8-11 2.14 ##
* add: assignable to search export
* fix: upload image files
* fix: map size
* updates: ui



## 2024-8-10 2.13 ##
* add: assignable to search


## 2024-7-17 2.12 ##
* update: do not show "OLD WORKFLOW" as assignable

## 2024-7-16 2.11 ##
* check: cc/normal dto
* fix: address export site/word
* fix: cc/normal export site/word
* fix: cc/normal export search control
* fix: cc/normal export wave control
* fix: cc/normal map search
* fix: ui/radio/metric/flow/get > map > site.location ---> siteLocation (updated by tech)


## 2024-7-01 2.10 ##
* add frequency extract TH3126 -> added "TDLTE"


## 2024-6-22 2.9 ##
* add: drone image types:
    * Drone
    * TargetBuildingFromTower
    * TargetBuilding
    * FlightControlScreen
    * NearCenter
* add: drone mode: "/ui/radio/metric/log/delete" --> height=X
* add: drone mode: "/ui/radio/metric/measurement/submit" --> height=X


## 2024-6-22 2.8 ##
* add: template site-radiometric-drone.docx
* change: site.address --> trim province and city
* change: complain.address --> trim province and city
* change: flow.siteAddress --> trim province and city
* word: flow.siteAddress --> format add - city - province
* patch: address normalize 
* patch: CCConflictFix 
* patch: CityFix 
* data: cleanup cc conflicts




## 2024-6-8 2.7 ##
* create: list of comments "CONVERT MSS MATCH" or "MANUALLY APPROVED"
* patch: OldStateToPending --> set to pending if comments "CONVERT MSS MATCH" or "MANUALLY APPROVED", set comments to "OLD WORKFLOW"
    * bazras, date measurement
    * complain ----> 32894 AR0793
    * complain ----> 32895 ES0010
    * complain ----> 32896 AS0122
    * has data ----> 6021 KM0215
    * has data ----> 6453 KM0986
    * has data ----> 6471 KM1124
    * has data ----> 9411 QN0022
    * has data ----> 9447 QN0059
    * has data ----> 9561 QN0200
    * has data ----> 9568 QN0207
    * has data ----> 12382 GL0440
    * has data ----> 12467 GL0300
    * has data ----> 12512 GL0536
    * has data ----> 12534 GL0530
    * has data ----> 12578 GL0540
    * has data ----> 12618 GL0202
    * has data ----> 12690 GL0017
    * has data ----> 12691 GL0109
    * has data ----> 12852 GL0443
    * has data ----> 15287 KM0555
    * has data ----> 16943 TH0699
    * has data ----> 17248 TH1888
    * has data ----> 17468 TH2084
    * has data ----> 18015 TH2079
* update: map --> "OLD WORKFLOW" --> show different marker
* update: /ui/radio/metric/flows/search/map --> tag added to be able to differentiate between markers


## 2024-5-05 2.6 ##
* site import
* framework update
* site duplicate patch
* fix cc missing file in report as1356 6part 1png


## 2024-4-21 2.5 ##
* user signature upload
* user signature bug fix


## 2024-4-21 2.5 ##
* fix: bug when import site

## 2024-3-5 2.4.4 ##
* import site updates
* bug: fix measurements

## 2024-3-5 2.4.4 ##
* add: role ATOMI add to map


## 2024-3-4 2.4.3 ##
* add: role ATOMI add to front


## 2024-3-3 2.4.2 ##
* fix: bug > de assign
* fix: bug in data type mapping when reading from database


## 2024-3-2 2.4.1 ##
* add: role ATOMI add to backend


## 2024-03-1 2.4 ##
* add: health check service 
* add: system monitoring 


## 2024-02-27 2.3 ##
* update: framework update


## 2023-05-22 2.1.3 ##
* change: radiometrics > rounding numbers in word report
* change: __ok file unit
* change: extention name changed: http://172.16.1.161:8080/admin/index?lang=en&x=d5a4d68676fb61e1b7b58d0023488ff4f73135f3


## 2023-03-15 2.1.2 ##
* add: "http://cel.ictrc.ac.ir:8080/ui/radio/metric/site/state/excel"
* add: isMwCm2100
* add: isMwCm2150;
* add: isMwCm2170;

as1356 6part 1png


## 2022-10-30 2.1.1 ##
* add: "http://cel.ictrc.ac.ir:8080/ui/radio/metric/site/state/excel"
* add: "http://172.16.1.161:8080/ui/radio/metric/site/state/excel"
* add: "/patch/radiometric/measurement/redo?siteCode=XXXX"


## 2022-10-04 2.1 ##
* update: new framework
* add: "/ui/hse/audit/data/many?dateMin=2022-01-01&dateMax=2022-01-10"
* fix: radiometrics > bug in setting state dates for CC
* patch: "/patch/missing/date/fix"
* add: auto delete temp



## 2022-06-30 2.0.2 ##
* add: detect measurement log file unit (isMwCm2 or vm)
* fix: search date bug



## 2022-03-13 2.0.1 ##
* fix: search bug



## 2022-03-11 2.0.0 ##
* change: code cleanup
* change: code review and memory management enhancements
* change: JSON lib changed to Jackson
* change: documents updated


---------------------------------------------------
FRONTEND/BACKEND SERVER:
frontend: https://www.rf-emf.nak-mci.ir
backend admin: https://www.rf-emf.nak-mci.ir:8443/admin/signin
user: nak-root
password: nak-947#85-root

server login: sudo ssh ubuntu@185.147.162.67
password: QAZqaz@1234
---------------------------------------------------
MAP SERVER:
panel: http://www.peakpointmap.com:2082/
user: نام کاربری
password: In7en87C7f

