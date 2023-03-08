package com.example.springboot.service;

import com.example.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 李兆年
 * @since 2022-09-12
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
