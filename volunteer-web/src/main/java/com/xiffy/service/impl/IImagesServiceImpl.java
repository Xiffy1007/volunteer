package com.xiffy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiffy.entity.Check;
import com.xiffy.entity.Images;
import com.xiffy.mapper.CheckMapper;
import com.xiffy.mapper.ImagesMapper;
import com.xiffy.service.ICheckService;
import com.xiffy.service.IImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 图片Service实现类

 */
@Service("imagesService")
public class IImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements IImagesService {

}
