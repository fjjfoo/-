package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("yonghu")
public class YonghuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private String yonghuName;

    private String yonghuPhone;

    private String yonghuIdNumber;

    private String yonghuPhoto;

    private Integer sexTypes;

    private String yonghuEmail;

    private Double newMoney;

    private Date createTime;
}
