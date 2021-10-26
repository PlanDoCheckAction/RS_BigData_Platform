package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.MetaData;

public interface DataService {

    public Page<MetaData> getMetaDataBySatelite(Integer pageNum, Integer pageSize, String search);

    public boolean downloadData(String hdfsPath);

}
