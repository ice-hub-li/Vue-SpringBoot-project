package com.example.springboot.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springboot.common.ValidationEnum;
import com.example.springboot.entity.Validation;
import com.example.springboot.mapper.ValidationMapper;
import com.example.springboot.service.IValidationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 李兆年
 * @since 2022-11-12
 */
@Service
public class ValidationServiceImpl extends ServiceImpl<ValidationMapper, Validation> implements IValidationService {

    @Transactional
    @Override
    public void saveCode(String email, String code, Integer type, DateTime expireDate) {

        Validation validation = new Validation();
        validation.setEmail(email);
        validation.setCode(code);
        validation.setType(type);
        validation.setTime(expireDate);

        //删除同类型的验证
        if(type == ValidationEnum.LOGIN.getCode()){
            UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
            validationUpdateWrapper.eq("email",email);
            validationUpdateWrapper.eq("type", ValidationEnum.LOGIN.getCode());
            remove(validationUpdateWrapper);
        }
        if (type == ValidationEnum.USER_REGIST.getCode()){
            UpdateWrapper<Validation> validationUpdateWrapper = new UpdateWrapper<>();
            validationUpdateWrapper.eq("email",email);
            validationUpdateWrapper.eq("type", ValidationEnum.USER_REGIST.getCode());
            remove(validationUpdateWrapper);
        }
        save(validation);


    }
}
