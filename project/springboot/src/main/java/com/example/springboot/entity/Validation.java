package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author 李兆年
 * @since 2022-11-12
 */
@Getter
@Setter
@TableName("sys_validation")
@ApiModel(value = "Validation对象", description = "")
public class Validation implements Serializable {

    private static final long serialVersionUID = 1L;
      @ApiModelProperty("id")
      @TableId(value = "id",type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("邮箱")
      private String email;

      @ApiModelProperty("验证码")
      private String code;

      @ApiModelProperty("验证类型")
      private Integer type;

      @ApiModelProperty("过期时间")
      private Date time;


}
