package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@TableName("rs_metadata")
@Data
public class MetaData {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer rPath;           //轨道号
    private Integer rRow;            //行编号
    private String rDesc;           //空间投影以及坐标系等
    private String sensorType;      //类型、精度以及分辨率等
    private String rSatelite;       //卫星类型
    private String rResource;       //影像的数据来源
    private Date rCollectDate;      //采集时间
    private Double centerLat;       //几何中心点的纬度
    private Double centerLong;      //几何中心点的经度
    private Double fileSize;        //影像实体的文件大小
    private String productId;       //影像的数据类型
    private String sensorDesc;      //传感器描述信息
    private String productVersion;  //影像版本号
    private Integer band;           //影像的波段数
    private String contact;         //电话、邮箱以及单位等
    private Double upperLeftLat;    //左上点维度
    private Double upperLeftLong;   //左上点经度
    private Double upperRightLat;   //右上点维度
    private Double upperRightLong;  //右上点经度
    private Double lowerLeftLat;    //左下点维度
    private Double lowerLeftLong;   //左下点经度
    private Double lowerRightLat;   //右下点维度
    private Double lowerRightLong;  //右下点经度
    private String hdfsPath;        //影像在hdfs的存储路径
    private String webHdfsPath;     //hdfs内影像的映射地址

}
