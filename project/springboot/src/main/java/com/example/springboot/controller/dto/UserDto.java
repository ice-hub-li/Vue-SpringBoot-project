package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求的参数
 */

@Data
public class UserDto {
    private String username;
    private String email;
    private String code;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
