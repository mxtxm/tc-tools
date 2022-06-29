# TC-Tools #

* front: 


YZ6003 ibs
KS0205
TH1262

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


## 2022-06-30 2.0.2 ##
* add: detect measurement log file unit (isMwCm2 or vm)


## 2022-03-13 2.0.1 ##
* fix: search bug


## 2022-03-11 2.0.0 ##
* change: code cleanup
* change: code review and memory management enhancements
* change: JSON lib changed to Jackson
* change: documents updated

