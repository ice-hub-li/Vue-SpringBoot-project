package com.example.springboot.controller;

//自加包
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.annotation.ReleaseToken;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.utils.TokenUtils;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.service.IUserService;
import com.example.springboot.entity.User;

//自加包



import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 李兆年
 * @since 2022-08-28
 */

@RestController
@RequestMapping("/user")
public class UserController {

    /*
    相当于
     @Autowired
    private UserService userService;
     */

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto){
        //对can没参数进行校验
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }

    @ReleaseToken
    @PostMapping("/loginEmail")
    public Result loginEmail(@RequestBody UserDto userDto){
        /*
        邮箱登录界面，点击登录按钮对应的后台接口
         */
        //对can没参数进行校验
        String email = userDto.getEmail();
        String code = userDto.getCode();
        //如果参数，没有进行输入将会发生报错
        if (StrUtil.isBlank(email) || StrUtil.isBlank(code)){
            return Result.error(Constants.CODE_400,"参数错误");
        }
        //真正实现登录的函数
        UserDto dto = userService.loginEmail(userDto);
        return Result.success(dto);
    }

    @ReleaseToken
    @GetMapping("/email/{email}")
    public Result sendEmailCode(@PathVariable String email) {
        /*
        邮箱登录，向邮箱发送验证码，，发送验证码，
         */
        if (StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");

        }
        //在接口中实现
        userService.sendEmailCode(email);
        return Result.success();
    }

    @ReleaseToken
    @GetMapping("/verifyUserName/{username}")
    public Result verifyUserName(@PathVariable String username) {
        /*
        邮箱登录，向邮箱发送验证码，，发送验证码，
         */
        if (StrUtil.isBlank(username)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        //在接口中实现
        userService.verifyUserName(username);
        //如果没有报错发生异常，就会返回成功；但一般会人为制造异常让前台进行捕获事件；
        return Result.success();
    }


    @ReleaseToken
    @GetMapping("/resetpassword/{email}")
    public Result sendEmailCodeForResetPassword(@PathVariable String email) {
//        /*
//        邮箱登录，向邮箱发送验证码，，发送验证码，
//         */
        if (StrUtil.isBlank(email)){
            throw new ServiceException(Constants.CODE_400,"参数错误");

        }
        //在接口中实现
        userService.sendEmailCodeForResetPassword(email);

        return Result.success();
    }


    @Resource
    private IUserService userService;

    //注册接口
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto){
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return Result.error(Constants.CODE_400,"参数错误");
        }


        return Result.success(userService.register(userDto));
    }

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody User user){
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.saveOrUpdate(user));
    }


    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.removeById(id));
    }



    //批量删除接口
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.removeByIds(ids));
    }


    //查询所有数据
    @GetMapping
    public Result findAll(){
            //查询所有的人员信息
    //        return userMapper.findAll();
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.list());
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.getById(id));
    }

//    根据用户名获取数据库表中该用户的所有的信息
    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
//        flushRedis(Constants.PAGE_KEY);
        return Result.success(userService.getOne(queryWrapper));
    }

    @PostMapping("/password")
    public Result password(@RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
//        flushRedis(Constants.PAGE_KEY);
        return Result.success();
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(username)){
            queryWrapper.like("username",username);
        }
        if (!"".equals(email)){
            queryWrapper.like("email",email);
        }
        if (!"".equals(phone)){
            queryWrapper.like("phone",phone);
        }
//缓存
        //获取当前用户信息，通过token获得登录用户的信息
//        User currentUser = TokenUtils.getCurrentUser();
////        System.out.println("获得当前用户信息******************************" + currentUser.getNickname());
//        System.out.println("************************************************");
//        System.out.println(currentUser.getNickname());
//        System.out.println("************************************************");
//        //1.从缓存区获取数据
//        String jsonstr = stringRedisTemplate.opsForValue().get(Constants.PAGE_KEY);
//        Page<User> page;
//        if (StrUtil.isBlank(jsonstr)){
//            //从数据库中取出数据
//            page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
//            //将取出的数据缓存到redis中
//            stringRedisTemplate.opsForValue().set(Constants.PAGE_KEY, JSONUtil.toJsonStr(page));
//        }else {
//            //假如不为空就从缓存中获得数据；
//            page = JSONUtil.toBean(jsonstr, new TypeReference<Page<User>>() {
//            }, true);
//        }
//        return Result.success(page);

        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));

//缓存
    }

    //文件导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<User> list = userService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("level", "会员");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();

    }
    //文件导入接口
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//         方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来
        List<User> list = reader.readAll(User.class);

//        reader.addHeaderAlias("用户名","username");
//        List<List<Object>> list = reader.read(1);

        userService.saveBatch(list);


        return true;
    }

//    //删除缓存
//    private void flushRedis(String key){
//        stringRedisTemplate.delete(key);
//
//    }




}

