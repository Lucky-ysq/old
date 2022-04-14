package com.doup.controller;

import com.doup.domain.FileProperties;
import com.doup.domain.Name;
import com.doup.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("file")
@CrossOrigin(origins = "*")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private FileProperties fileProperties;

    @RequestMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return "文件为空";
            }
            // 获取大小
            long size = file.getSize();
            logger.info("文件大小： " + size);
            // 获取文件名
            String fileName = file.getOriginalFilename();
            logger.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            logger.info("文件的后缀名为：" + suffixName);
            // 设置文件存储路径
            /* 路径三 上传文件的位置 */
            String filePath = Paths.get(fileProperties.getDocDir()).toAbsolutePath().normalize().toString();
            System.out.println(filePath+"/");
            String path = filePath + "/" + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            return "文件上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "文件上传失败";
    }

    @GetMapping("download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName,
                                                 HttpServletRequest request) {
        Resource resource = fileService.loadFileAsResource(fileName);
        System.out.println("res" + resource);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            logger.error("无法获取文件类型", e);
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/downName")
    public List<Object> downName(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /* 路径一  获取名称 */
        String path = Paths.get(fileProperties.getDocDir()).toAbsolutePath().normalize().toString();;
        return getFile(path);
    }

    private static List<Object> getFile(String path) {
        // 获得指定文件对象
        File file = new File(path);
        // 获得该文件夹内的所有文件
        File[] array = file.listFiles();

        List<Object> list = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            if (array[i].isFile())//如果是文件
            {
                list.add(new Name(array[i].getName(),(array[i].length() / 1024) + "kb"));
            } else if (array[i].isDirectory())//如果是文件夹
            {
                getFile(array[i].getPath()).forEach(s -> {
                    list.add(s);
                });
            }
        }
        return list;
    }


}
