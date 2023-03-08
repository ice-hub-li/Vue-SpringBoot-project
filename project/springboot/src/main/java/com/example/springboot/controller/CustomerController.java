package com.example.springboot.controller;

//自加包
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.service.ICustomerService;
import com.example.springboot.entity.Customer;

//自加包




import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 李兆年
 * @since 2022-12-04
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    /*
    相当于
     @Autowired
    private UserService userService;
     */
    private ICustomerService customerService;

    //新增或者更新
    @PostMapping
    public boolean save(@RequestBody Customer customer){
        return customerService.saveOrUpdate(customer);
    }

    //删除
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return customerService.removeById(id);
    }

    //批量删除接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return customerService.removeByIds(ids);
    }


    //查询所有数据
    @GetMapping
    public List<Customer> findAll(){
            //查询所有的人员信息
    //        return userMapper.findAll();
        return customerService.list();
    }

    //根据id查询
    @GetMapping("/{id}")
    public Customer findOne(@PathVariable Integer id) {
        return customerService.getById(id);
    }

    //分页查询
    @GetMapping("/page")
    public Page<Customer> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String email,
                                @RequestParam Integer pid) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if (!"".equals(email)){
            queryWrapper.like("email",email);
        }
        if (!"".equals(pid)){
            queryWrapper.like("pid",pid);
        }


        return customerService.page(new Page<>(pageNum, pageSize),queryWrapper);

        }

    }







