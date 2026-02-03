package org.xiaolong.blog.utils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * XSS攻击过滤工具类，清理非法HTML/JS标签
 */
public class XssUtils {

    /**
     * 过滤所有HTML标签，仅保留纯文本内容
     * @param content 待过滤的评论内容
     * @return 过滤后的纯文本
     */
    public static String cleanXss(String content) {
        if (content == null || content.trim().isEmpty()) {
            return "";
        }
        // Safelist.none()：不允许任何HTML标签，彻底杜绝XSS
        return Jsoup.clean(content, Safelist.none());
    }
}