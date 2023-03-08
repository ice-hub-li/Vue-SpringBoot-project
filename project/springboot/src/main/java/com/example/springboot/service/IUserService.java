package com.example.springboot.service;

import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 李兆年
 * @since 2022-08-28
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    User register(UserDto userDto);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    UserDto loginEmail(UserDto userDto);


    //在这里实现发送邮箱验证码的逻辑
    void sendEmailCode(String email);

    void sendEmailCodeForResetPassword(String email);

    void verifyUserName(String username);
}
