package com.example.demo.common;

import com.example.demo.util.HdfsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HdfsConfig {
    //这里的hdfs.path就是上面fs.defaultFS链接
    @Value("${hdfs.path}")
    private String defaultHdfsUri;

    @Bean
    public HdfsUtil getHbaseService(){
        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        //操作文件io,用来读写
        //conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
        conf.set("fs.defaultFS",defaultHdfsUri);
        conf.set("dfs.client.use.datanode.hostname","true");
        return new HdfsUtil(conf,defaultHdfsUri);
    }
}

