package com.xiffy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web项目配置类
 * @author java1234_小锋
 * @site www.java1234.com
 * @company 南通小锋网络科技有限公司
 * @create 2022-02-24 11:45
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/swiper/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\swiperImgs\\");
        registry.addResourceHandler("/image/bigType/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\bigTypeImgs\\");
        registry.addResourceHandler("/image/product/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\productImgs\\");
        registry.addResourceHandler("/image/productSwiperImgs/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\productSwiperImgs\\");
        registry.addResourceHandler("/image/productIntroImgs/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\productIntroImgs\\");
        registry.addResourceHandler("/image/productParaImgs/**").addResourceLocations("file:E:\\springbootProjects\\java1234-mall-imags\\productParaImgs\\");
        System.out.println("1"+"1");
    }




    public static void main(String[] args) {
        for(int i=0;i<10000;i++){
            System.out.println("测试="+i);
        }
    }
}
