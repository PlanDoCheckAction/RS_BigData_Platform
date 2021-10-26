package com.example.demo.service.impl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.MetaData;
import com.example.demo.mapper.MetaDataMapper;
import com.example.demo.service.DataService;
import com.example.demo.util.HdfsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    MetaDataMapper metaDataMapper;

    @Autowired
    private HdfsUtil hdfsService;

    @Override
    public Page<MetaData> getMetaDataBySatelite(Integer pageNum, Integer pageSize, String search) {
        LambdaQueryWrapper<MetaData> wrapper = Wrappers.<MetaData>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(MetaData::getRSatelite, search);
        }
        Page<MetaData> metaDataPage = metaDataMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return metaDataPage;
    }

    @Override
    public boolean downloadData(String hdfsPath) {

        return false;
    }
}
