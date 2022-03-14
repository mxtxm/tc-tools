package com.tctools.business.dto.project.radiometric.workflow;

import com.tctools.common.Param;
import com.vantar.database.dto.*;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import java.util.*;

@NoStore
public class Proximity extends DtoBase {

    private static final int MAX_FILES_IN_DIR = 5;

    public RadioMetricProximityType proximityType;
    public String comments;
    public List<String> imageUrls;
    public List<String> imagePaths;


    // is called after RadioMetricFlow fetch
    public void setImages(String siteCode, long flowId) {
        if (proximityType == null) {
            return;
        }

        imageUrls = new ArrayList<>();
        imagePaths = new ArrayList<>();
        String type = StringUtil.toKababCase(proximityType.toString());

        getImagePaths(siteCode, flowId, true, true).forEach((typeIndex, url) -> {
            if (typeIndex.startsWith(type)) {
                imageUrls.add(url);
            }
        });
        getImagePaths(siteCode, flowId, false, true).forEach((typeIndex, path) -> {
            if (typeIndex.startsWith(type)) {
                imagePaths.add(path);
            }
        });
    }

    public String getNextImagePath(RadioMetricFlow flow) {
        if (flow == null || flow.site == null || flow.site.code == null || proximityType == null) {
            return "";
        }
        for (int i = 1; i < MAX_FILES_IN_DIR; ++i) {
            String filePath = getImageFilePath(flow, i);
            if (!FileUtil.exists(filePath)) {
                return filePath;
            }
        }
        return getImageFilePath(flow, 0);
    }

    private String getImageFilePath(RadioMetricFlow flow, int i) {
        String siteCode = flow.site.code.toLowerCase();
        return Param.RADIO_METRIC_FILES + siteCode +  "/measurement/" + flow.id + "/" + siteCode + "-"
            + proximityType.toString() + '-' + i + ".jpg";
    }

    public static Map<String, String> getImagePaths(String siteCode, long flowId, boolean isUrl, boolean exist) {
        String path = siteCode + "/measurement/" + flowId + "/" + siteCode  + '-';
        String urlBase = Param.RADIO_METRIC_URL + path;
        String dir = Param.RADIO_METRIC_FILES + path;

        Map<String, String> paths = new HashMap<>(RadioMetricProximityType.values().length * 5);
        for (RadioMetricProximityType type : RadioMetricProximityType.values()) {
            for (int i = 1; i < MAX_FILES_IN_DIR; ++i) {
                String t = StringUtil.toKababCase(type.toString());
                String filepath = dir + t + '-' + i + ".jpg";
                if (exist && !FileUtil.exists(filepath)) {
                    continue;
                }

                String v = isUrl ? (urlBase + t + '-' + i + ".jpg") : filepath;
                if (i == 1) {
                    paths.put(t, v);
                } else {
                    paths.put(t + i, v);
                }
            }
        }
        return paths;
    }
}
