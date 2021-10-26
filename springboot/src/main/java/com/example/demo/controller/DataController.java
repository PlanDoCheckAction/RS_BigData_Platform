package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.MetaData;
import com.example.demo.entity.User;
import com.example.demo.mapper.MetaDataMapper;
import com.example.demo.service.DataService;
import com.example.demo.util.HdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private DataService dataService;

    @Autowired
    private HdfsUtil hdfsService;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        Page<MetaData> metaDataPage = dataService.getMetaDataBySatelite(pageNum, pageSize, search);

        return Result.success(metaDataPage);
    }

    @RequestMapping("/hdfs/downloadFile")
    public Result<?> download(@RequestParam("fileName") String fileName, final HttpServletResponse response){
        OutputStream os = null;

        String[] n = fileName.split("/");
        try {
            byte[] data = hdfsService.openWithBytes(fileName);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + n[n.length-1]);
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(data);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("303", "下载文件失败！");
        }
        return null;
    }

    @PostMapping("/hdfs/upload")
    public Result<?> upload(HttpServletRequest request, @RequestParam("zipFile") MultipartFile file,
                            @RequestParam("satelite") String sateliteType,
                            @RequestParam("fileName") String fileName){
        if (file.isEmpty()) {
            return Result.error("304", "上传文件为空");
        } else {
            System.out.println(sateliteType);
            System.out.println(fileName);
            try {
                byte[] b = file.getBytes();
                System.out.println(new String(b));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Result.success();
        }
    }
}
