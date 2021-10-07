package com.tctools.business.dto.project.map;

import com.tctools.business.dto.site.Site;
import com.vantar.util.object.ObjectUtil;


public class SiteForMap {

    public Long id;
    public String code;
    public String name;
    public Double[] location;


    public SiteForMap(Site.ViewableTiny site) {
        id = site.id;
        code = site.code;
        name = site.name;
        if (site.location != null && !site.location.isEmpty()) {
            location = new Double[] {site.location.latitude, site.location.longitude};
        }
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}
