package com.tctools.common.util;

import com.vantar.util.string.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.slf4j.*;
import java.util.*;


public class HtmlParser {

    protected static final Logger log = LoggerFactory.getLogger(HtmlParser.class);
    protected Document doc;


    public void setHtml(String html) {
        doc = Jsoup.parse(html);
    }

    public Map<String, String> getBySelector(String selector) {
        Map<String, String> items = new LinkedHashMap<>();
        try {
            for (Element e : doc.select(selector)) {
                String paraId = e.attr("w14:paraId");
                if (StringUtil.isNotEmpty(paraId)) {
                    items.put(paraId, e.text());
                }
            }
        } catch (Exception e) {
            log.error("! ! !", e);
        }
        return items;
    }

    public Map<String, String> getBySelectorIx(String selector) {
        Map<String, String> items = new LinkedHashMap<>();
        int i = 0;
        try {
            for (Element e : doc.select(selector)) {
                items.put(Integer.toString(++i), e.text());
            }
        } catch (Exception e) {
            log.error("! ! !", e);
        }
        return items;
    }
}