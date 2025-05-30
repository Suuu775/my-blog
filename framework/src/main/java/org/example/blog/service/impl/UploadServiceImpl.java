package org.example.blog.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.example.blog.domain.ResponseResult;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.UploadService;
import org.example.blog.utils.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Service
@ConfigurationProperties("oss")
@Data
public class UploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String domain;


    @Override
    public ResponseResult upload(MultipartFile img) {
        // 合法性校验
        String originalFilename = img.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.endsWith(".jpeg") && !originalFilename.endsWith(".png"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        if (img.getSize()>1024*1024){
            throw new SystemException(AppHttpCodeEnum.FILE_SIZE_ERROR);
        }

        // 生成云上文件名
        String filePath = PathUtils.generaterFilePath(originalFilename);

        // 上传
        String url = uploadOSS(img,filePath);




        return ResponseResult.okResult(url);
    }


    private String uploadOSS(MultipartFile img,String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
//            byte[] uploadBytes = img.getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
            InputStream byteInputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(byteInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                return domain + key;
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
