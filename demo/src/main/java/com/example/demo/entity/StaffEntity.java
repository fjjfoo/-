package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("staff")
public class StaffEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String staffName;

    private String staffPhone;

    private String staffEmail;

    private String staffAvatar;

    private String staffSkills;

    private Double staffRating;

    private Integer staffStatus;

    private String staffIntroduction;

    private Date createTime;

    private Date updateTime;
}