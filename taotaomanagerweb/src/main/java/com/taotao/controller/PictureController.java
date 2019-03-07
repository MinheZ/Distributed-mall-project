package com.taotao.controller;

import com.taotao.commons.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String address;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile){
        try {
            // 接收上传的文件
            String originalFilename = uploadFile.getOriginalFilename();
            // 取扩展名,取“.”后面的子串
            String extName = originalFilename.substring(originalFilename.indexOf(".") + 1);
            // 上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            // 响应上传图片的url
            String  url = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
            url = address + url;
            Map result = new HashMap<>();
            result.put("error",0);
            result.put("url",url);
            // 返回字符串的形式，解决浏览器不兼容的问题
            return JsonUtils.objectToJson(result);
        }catch (Exception e){
            e.printStackTrace();
            Map result = new HashMap<>();
            result.put("error",1);
            result.put("message","图片上传失败！");
            return JsonUtils.objectToJson(result);
        }
    }

}
