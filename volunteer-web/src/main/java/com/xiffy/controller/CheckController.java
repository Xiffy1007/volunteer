package com.xiffy.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiffy.common.Result;
import com.xiffy.entity.*;
import com.xiffy.exception.CustomException;
import com.xiffy.mapper.CheckMapper;
import com.xiffy.mapper.ImagesMapper;
import com.xiffy.mapper.WxUserInfoMapper;
import com.xiffy.service.ICheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.metadata.IIOMetadata;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/check")
public class CheckController {
    @Autowired
    private CheckMapper checkMapper;

    @Autowired
    private ICheckService checkService;

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private WxUserInfoMapper wxUserInfoMapper;

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/resources/static/img_upload/";


    @PostMapping("/upload")
    public Result<Integer> test(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if (originalName == null) {
            return Result.error("1001", "文件名不能为空");
        }
        if (!originalName.contains("png") && !originalName.contains("jpg") && !originalName.contains("jpeg") && !originalName.contains("gif")) {
            return Result.error("1002", "只能上传图片");
        }
        String fileName = FileUtil.mainName(originalName) + System.currentTimeMillis() + "." + FileUtil.extName(originalName);
        FileUtil.writeBytes(file.getBytes(), BASE_PATH + fileName);
        Images images = new Images();
        images.setFileName(fileName);
        images.setOriginalName(originalName);
        images.setCreateTime(new Date());
        int res = imagesMapper.insert(images);
        if (res == 1 && images != null) {
            return Result.success(images.getId());
        } else return Result.error();

    }

    /*
    static class UploadUtils{
        // 项目根路径下的目录  -- SpringBoot static 目录相当于是根路径下（SpringBoot 默认）
        public final static String IMG_PATH_PREFIX = "static/img_upload";

        public static File getImgDirFile(){

            // 构建上传文件的存放 "文件夹" 路径
            String fileDirPath = new String("src/main/resources/" + IMG_PATH_PREFIX);
            File fileDir = new File(fileDirPath);
            if(!fileDir.exists()){
                // 递归生成文件夹
                fileDir.mkdirs();
            }
            return fileDir;
        }
    }

    @PostMapping("/upload")
    public Result upload(@RequestParam(value = "file", required = false)MultipartFile file){
        System.out.println("进来了上传图片接口");
        System.out.println(file);
        String msg = "";
        if (file.isEmpty()) {
            System.out.println("文化上传失败!");
            msg="文化上传失败!";
            return Result.error();
        }else {
            System.out.println("文化上传成功!");
            // 拿到文件名
            String filename = file.getOriginalFilename();
            System.out.println("0"+file);//获取图片的文件名
            // 存放上传图片的文件夹
            File fileDir = UploadUtils.getImgDirFile();
            // 输出文件夹绝对路径  -- 这里的绝对路径是相当于当前项目的路径而不是“容器”路径
            //System.out.println(fileDir.getAbsolutePath());

            try {
                // 构建真实的文件路径
                File newFile = new File(fileDir.getAbsolutePath() + File.separator + filename);
                // 上传图片到 -》 “绝对路径”
                file.transferTo(newFile);
                System.out.println(newFile.getAbsolutePath());
                msg = "上传成功!";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }*/

    //提交审核资料
    @PutMapping("/content")
    public Result submit(@RequestBody Check check) {

        check.setCreateTime(new Date());
        check.setState(0);

        int res = checkMapper.insert(check);
        if (res == 1) {
            return Result.success();
        } else return Result.error();
    }

    @RequestMapping("/list")
    public Result<Map<String, Object>> list(Integer userId, Integer state, Integer page, Integer pageSize) {

        List<Check> list = null;
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Page<Check> pageOrder = new Page<>(page, pageSize);
        if (state == 3) {
            Page<Check> Results = checkMapper.findAll(pageOrder, userId);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());
        } else {
            Page<Check> Results = checkMapper.findcheck(pageOrder, userId, state);
            list = Results.getRecords();
            resultMap.put("total", Results.getTotal());
            resultMap.put("totalPage", Results.getPages());
        }

        resultMap.put("list", list);
        return Result.success(resultMap);
    }

    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Integer id) {
        Map<String, Object> resultMap = new HashMap<>();//返回数据
        Check check = checkMapper.selectById(id);
        resultMap.put("check", check);
        String ids = check.getImages();
        ids = ids.substring(1, ids.length());
        System.out.println(ids);
        List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        resultMap.put("idList", idList);
        return Result.success(resultMap);
    }


    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {
        if (StrUtil.isBlank(id) || "null".equals(id)) {
            throw new CustomException("1001", "您未上传文件");
        }
        Images images = imagesMapper.selectById(id);
        byte[] bytes = FileUtil.readBytes(BASE_PATH + images.getFileName());
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(images.getOriginalName(), "UTF-8"));
        response.addHeader("Content-Length", "" + bytes.length);
        OutputStream toclient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toclient.write(bytes);
        toclient.flush();
        toclient.close();
    }


    //分页查询审核情况
    @GetMapping("/page")
    public Result<PageInfo<Check>> page(@RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "15") Integer pageSize,
                                        @RequestParam Integer check
    ) {

        PageHelper.startPage(pageNum, pageSize);
        List<Check> list = checkMapper.find(check);
        return Result.success(PageInfo.of(list));


    }

    @GetMapping("/images/{id}")
    public Result<List<Integer>> images(@PathVariable Integer id) {
        Check check = checkMapper.selectById(id);
        String ids = check.getImages();
        ids = ids.substring(1, ids.length());
        System.out.println(ids);
        List<Integer> idList = Arrays.stream(ids.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        return Result.success(idList);
    }

    @PostMapping("/save/{state}")
    public Result save(@RequestBody Check check, @PathVariable Integer state) {
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("id", check.getId());
        check.setCheckTime(new Date());
        check.setState(state);
        if(state==1){
            int flag1= wxUserInfoMapper.duration(check.getDuration(),check.getUserId());//志愿时长增加
        }
        Boolean res = checkService.update(check,wrapper);
        if (res)
            return Result.success();
        else
            return Result.error();
    }

}
