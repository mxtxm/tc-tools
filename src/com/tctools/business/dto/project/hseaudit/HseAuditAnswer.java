package com.tctools.business.dto.project.hseaudit;

import com.tctools.common.Param;
import com.vantar.database.dto.*;
import com.vantar.util.file.FileUtil;
import com.vantar.util.string.StringUtil;
import java.util.*;

@NoStore
public class HseAuditAnswer extends DtoBase {

    private static final int MAX_FILE_IN_DIR = 100;

    public Long questionId;
    public HseAuditAnswerOption answer;
    public String comments;
    public HseAuditQuestion question;
    public String imageName;
    public List<String> imageNames;
    public List<String> imageUrls;


    public void setImageUrl(HseAuditQuestionnaire.Viewable flow) {
        if (flow.site == null || flow.site.code == null) {
            return;
        }

        String flowId = flow.id.toString();
        String siteCode = flow.site.code.toLowerCase();
        String urlPart = Param.HSE_AUDIT_URL + siteCode + '/';
        String q = "q" + questionId;
        imageUrls = new ArrayList<>();
        for (String file : FileUtil.getDirectoryFiles(Param.HSE_AUDIT_FILES + siteCode + '/')) {
            String[] parts = StringUtil.split(file, '/');
            String filename = parts[parts.length - 1];
            String[] filenameParts = StringUtil.split(filename, '-');
            if (filenameParts.length != 4 || !filenameParts[1].equals(flowId) || !filenameParts[2].equals(q)) {
                continue;
            }
            imageUrls.add(urlPart + filename);
        }
    }

    public String getNextImagePath(HseAuditQuestionnaire flow) {
        if (flow == null || flow.site == null || flow.site.code == null) {
            return "";
        }
        for (int i = 1; i < MAX_FILE_IN_DIR; ++i) {
            String filePath = getImageFilePath(flow, i);
            if (!FileUtil.exists(filePath)) {
                return filePath;
            }
        }
        return getImageFilePath(flow, 0);
    }

    private String getImageFilePath(HseAuditQuestionnaire flow, int i) {
        String siteCode = flow.site.code.toLowerCase();
        return Param.HSE_AUDIT_FILES + siteCode + "/" + siteCode + '-' + flow.id + "-q" + questionId + '-' + i + ".jpg";
    }
}
