package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Hjm
 * @date 2026/2/5 17:16
 * @motto 不经一番寒彻骨 怎得梅花扑鼻香
 * @description
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController
{
	@PostMapping("/upload")
	public Result<String> upload(MultipartFile file) {
		log.info("文件上传: {}", file.getOriginalFilename());
		
		try {
			// 1. 定义本地存储目录（建议配置在 application.yml 中）
			String basePath = "E:/cangqiong/uplode/images/";
			File dir = new File(basePath);
			if (!dir.exists()) {
				dir.mkdirs(); // 如果目录不存在则创建
			}
			
			// 2. 获取原始文件名并提取后缀
			String originalFilename = file.getOriginalFilename();
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			
			// 3. 使用 UUID 生成新文件名，防止文件名冲突导致覆盖
			String fileName = UUID.randomUUID().toString() + extension;
			
			// 4. 将文件保存到目标路径
			file.transferTo(new File(basePath + fileName));
			
			// 5. 返回文件的存储路径（或者访问路径）
			String filePath = basePath + fileName;
			log.info("文件上传成功，文件存储路径: {}", filePath);
			return Result.success(filePath);
			
		} catch (IOException e) {
			log.error("文件上传失败", e);
			return Result.error("文件上传失败");
		}
	}

}
