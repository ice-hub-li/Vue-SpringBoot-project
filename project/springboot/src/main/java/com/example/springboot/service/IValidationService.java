package com.example.springboot.service;

import cn.hutool.core.date.DateTime;
import com.example.springboot.entity.Validation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 李兆年
 * @since 2022-11-12
 */
public interface IValidationService extends IService<Validation> {

    void saveCode(String email, String code, Integer type, DateTime offsetMinute);
}
