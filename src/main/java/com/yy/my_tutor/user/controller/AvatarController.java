package com.yy.my_tutor.user.controller;

import com.yy.my_tutor.common.RespResult;
import com.yy.my_tutor.config.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 头像上传接口：图片保存到本地 /tutor/photo/ 目录，返回访问地址。
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class AvatarController {

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    /** 保存目录（相对 user.dir 或绝对路径） */
    @Value("${file.upload.path:tutor/photo}")
    private String uploadPath;

    /** 返回的 URL 前缀，如 /tutor/photo */
    @Value("${file.upload.url-prefix:/tutor/photo}")
    private String urlPrefix;

    /**
     * 上传头像
     * 图片保存到本地服务器 /tutor/photo/ 目录，返回保存后的访问地址。
     *
     * @param file 图片文件（multipart 参数名：file 或 avatar）
     * @return 保存后的访问地址，如 /tutor/photo/xxx.jpg
     */
    @PostMapping("/avatar/upload")
    public RespResult<String> uploadAvatar(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        MultipartFile toUpload = file != null ? file : avatar;
        if (toUpload == null || toUpload.isEmpty()) {
            throw new CustomException("请选择要上传的图片");
        }

        String contentType = toUpload.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new CustomException("仅支持 jpeg、png、gif、webp 格式图片");
        }

        String originalFilename = toUpload.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        if (!ext.matches("(?i)\\.(jpe?g|png|gif|webp)")) {
            ext = ".jpg";
        }
        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

        File dir = new File(uploadPath);
        if (!dir.isAbsolute()) {
            dir = new File(System.getProperty("user.dir"), uploadPath);
        }
        if (!dir.exists()) {
            try {
                Files.createDirectories(dir.toPath());
            } catch (IOException e) {
                log.error("创建目录失败: {}", dir.getAbsolutePath(), e);
                throw new CustomException("创建上传目录失败");
            }
        }

        Path targetPath = dir.toPath().resolve(savedName);
        try {
            toUpload.transferTo(targetPath.toFile());
        } catch (IOException e) {
            log.error("保存头像失败", e);
            throw new CustomException("保存图片失败");
        }

        String url = urlPrefix + "/" + savedName;
        log.info("头像已保存: {}", url);
        return RespResult.success("上传成功", url);
    }
}
