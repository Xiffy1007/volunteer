package com.xiffy.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xiffy.common.Result;
import com.xiffy.entity.Images;
import com.xiffy.entity.News;
import com.xiffy.entity.SignUp;
import com.xiffy.exception.CustomException;
import com.xiffy.mapper.ImagesMapper;
import com.xiffy.mapper.NewsMapper;
import com.xiffy.service.INewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/files")
public class ImagesController {
    @Autowired
    private INewsService NewsService;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private ImagesMapper imagesMapper;

    private static final String BASE_PATH=System.getProperty("user.dir")+"/src/main/resources/static/img_upload/";

    @PostMapping("/upload")
    public Result<Map<String,Object>> upload(MultipartFile file) throws IOException {
        Map<String,Object> resultMap=new HashMap<>();//返回数据
        String originalName = file.getOriginalFilename();
        if (originalName==null) {
            return Result.error("1001", "文件名不能为空");
        }
        if(!originalName.contains("png")&&!originalName.contains("jpg")&&!originalName.contains("jpeg")&&!originalName.contains("gif")) {
            return Result.error("1002", "只能上传图片");
        }
        String fileName = FileUtil.mainName(originalName)+System.currentTimeMillis()+"."+FileUtil.extName(originalName);
        FileUtil.writeBytes(file.getBytes(),BASE_PATH+fileName);
        resultMap.put("fileName",fileName);
        resultMap.put("originalName",originalName);

        return Result.success(resultMap);

    }



    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {
        if(StrUtil.isBlank(id)||"null".equals(id)){
            throw new CustomException("1001","您未上传文件");
        }
        News news=NewsService.getOne(new QueryWrapper<News>().eq("id",id));
        byte[] bytes=FileUtil.readBytes(BASE_PATH+news.getFileName());
        response.reset();
        response.addHeader( "Content-Disposition" ,"attachment;filename="+
                URLEncoder.encode(news.getOriginalName(),"UTF-8"));
        response.addHeader( "Content-Length" ,""+bytes.length);
        OutputStream toclient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType( "application/octet-stream" );
        toclient.write(bytes);
        toclient.flush();
        toclient.close();
    }


}
