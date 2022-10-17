# TC-Tools #




https://docs.vmware.com/en/VMware-Horizon-Client-for-Linux/2206/horizon-client-linux-installation/GUID-AB6F0B4D-03DD-4E7A-AE16-BAB77CE4D42D.html
vmware-view --kioskLogin --nonInteractive --once --fullscreen --nomenubar --serverURL="172.26.19.53" --userName="ictrc.ac" --password="JNt987654321"
ubuntu@172.26.7.98
U@123456



/ui/hse/audit/data?id=30582
30582


http://localhost:8081/ui/hse/audit/significance/month?excel=true&from=1399-09-01&to=1400-05-30
http://localhost:8081/ui/hse/audit/significance/subcontractor/province?excel=true&from=1399-09-01&to=1400-05-30

http://172.16.1.161:8080/ui/hse/audit/data/many?dateMin=2022-09-01&dateMin=2022-09-20

    //http://localhost:8081/ui/hse/audit/significance/subcontractor/province/complete?from=1400-01-1&to=1400-5-5&excel=1
    //http://www.cel.ictrc.ac.ir:8080/ui/hse/audit/significance/subcontractor/province/complete?from=1400-01-1&to=1400-5-5&excel=1

    //http://localhost:8081/ui/hse/audit/significance/subcontractor/province?from=1400-01-1&to=1400-5-5&subcontractorids=399,123
    //http://localhost:8081/ui/hse/audit/significance/subcontractor?from=1400-01-1&to=1400-5-5&provinceids=1,2,30
    //http://localhost:8081/ui/hse/audit/question/subcontractor?from=1399-09-1&to=1400-5-30&provinceids=1,2,30


* todo: picture: dakal dast nazanad 
* todo: picture: 750x999
* Map: KS020 > drag and drop 
* Map: KS020 > fit to map 
* complain assign > date nazad too complain
* complain> plan > reassign. RADIOMETRIC delete nashavad
* gheyre plan ghabele assign nemishe
* agar plan nabood edit kone complain o amma reassign natoone bokone
* complain: agar planned nabashad delete nakonad
* complain state dashte bashad
* moghe zip kardan axe complain toosh nist
    * hast

* vendore > radio metric >  terminate approve return
* dashboard > complete verify approve darhar mah trend

*obversation report betavanad chand soal bezarad
    excel



var g = [
    'rgba(255, 0, 0, 0)',
    'rgba(255, 255, 0, 0.9)',
    'rgba(0, 255, 0, 0.7)',
    'rgba(173, 255, 47, 0.5)',
    'rgba(152, 251, 152, 0)',
    'rgba(152, 251, 152, 0)',
    'rgba(0, 0, 238, 0.5)',
    'rgba(186, 85, 211, 0.7)',
    'rgba(255, 0, 255, 0.9)',
    'rgba(255, 0, 0, 1)'
];

        $("html, body").animate({ scrollTop: 0 }, "slow");
        me.heatmap = new google.maps.visualization.HeatmapLayer({
            data: me.heatmapData,
            dissipating: true,
            max: 4.4,
            opacity: 0.8,
                gradient: g,
            map: me.gmap.map,
            radius: me.getNewRadius()
        });


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

