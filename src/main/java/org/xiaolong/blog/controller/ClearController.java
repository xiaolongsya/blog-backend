package org.xiaolong.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xiaolong.blog.utils.UnusedImageCleaner;

@RestController
public class ClearController
{

    @Autowired
    private UnusedImageCleaner unusedImageCleaner;

    /**
     * 废弃图片检测接口
     * @param archive true=归档废弃图片，false=仅生成报告（推荐先传false）
     * @return 执行结果
     */
    @GetMapping("/clean/unused/images")
    public String cleanUnusedImages(@RequestParam(defaultValue = "false") boolean archive) {
        try {
            unusedImageCleaner.detectAndArchiveUnusedImages(archive);
            if (archive) {
                return "✅ 废弃图片检测+归档完成！报告路径：/opt/1panel/www/sites/dragon-blog-static/index/unused_images_report.txt";
            } else {
                return "✅ 废弃图片检测完成（仅生成报告）！报告路径：/opt/1panel/www/sites/dragon-blog-static/index/unused_images_report.txt";
            }
        } catch (Exception e) {
            return "❌ 执行失败：" + e.getMessage();
        }
    }
}