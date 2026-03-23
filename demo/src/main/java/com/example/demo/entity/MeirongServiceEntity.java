package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("meirong_service")
public class MeirongServiceEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String serviceName;

    private Integer serviceCategory;

    private Double serviceOldMoney;

    private Double serviceNewMoney;

    private Integer serviceDuration;

    private String servicePhoto;

    private String serviceProcess;

    private String serviceProducts;

    private String serviceNotes;

    private String serviceContent;

    private Integer serviceClicknum;

    private Double serviceRating;

    private Integer shangxiaTypes;

    private Date createTime;
}
