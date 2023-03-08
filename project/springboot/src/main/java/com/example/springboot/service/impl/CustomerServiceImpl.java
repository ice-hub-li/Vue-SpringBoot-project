package com.example.springboot.service.impl;

import com.example.springboot.entity.Customer;
import com.example.springboot.mapper.CustomerMapper;
import com.example.springboot.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 李兆年
 * @since 2022-12-04
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
