package com.ecust.controller.system;


import com.ecust.config.upload.UploadService;
import com.ecust.controller.BaseController;
import com.ecust.vo.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system/upload")
public class UploadController extends BaseController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("/doUploadImage")
    public AjaxResult doUploadImage(MultipartFile multipartFile){
        Map<String,Object> map=new HashMap<>();
        if (multipartFile!=null){
            map.put("name",multipartFile.getOriginalFilename());
            map.put("url",uploadService.uploadImage(multipartFile));
            return AjaxResult.success(map);
        }
        return AjaxResult.error("文件上传失败");
    }
}
