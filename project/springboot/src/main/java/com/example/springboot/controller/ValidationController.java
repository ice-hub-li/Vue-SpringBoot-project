package com.example.springboot.controller;

//自加包
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.service.IValidationService;
import com.example.springboot.entity.Validation;

//自加包




import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 李兆年
 * @since 2022-11-12
 */
@RestController
@RequestMapping("/validation")
public class ValidationController {
    @Resource
    /*
    相当于
     @Autowired
    private UserService userService;
     */
    private IValidationService validationService;

    //新增或者更新
    @PostMapping
    public boolean save(@RequestBody Validation validation){
        return validationService.saveOrUpdate(validation);
    }

    //删除
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return validationService.removeById(id);
    }

    //批量删除接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return validationService.removeByIds(ids);
    }


    //查询所有数据
    @GetMapping
    public List<Validation> findAll(){
            //查询所有的人员信息
    //        return userMapper.findAll();
        return validationService.list();
    }

    //根据id查询
    @GetMapping("/{id}")
    public Validation findOne(@PathVariable Integer id) {
        return validationService.getById(id);
    }

    //分页查询
    @GetMapping("/page")
    public Page<Validation> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<Validation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return validationService.page(new Page<>(pageNum, pageSize),queryWrapper);

        }

    }






