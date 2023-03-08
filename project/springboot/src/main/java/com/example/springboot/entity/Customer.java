package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2022-12-04
 */
@Getter
@Setter
  @TableName("sys_customer")
@ApiModel(value = "Customer对象", description = "")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String name;

    private String phone;

      @ApiModelProperty("邮箱")
      private String email;

      @ApiModelProperty("会员等级-客户的会员等级")
      private String level;

      @ApiModelProperty("余额-客户的充值金额")
      private Integer balance;

      @ApiModelProperty("父id-创建该客户的id")
      private Integer pid;

      @ApiModelProperty("修改时间")
      private LocalDateTime time;


}
