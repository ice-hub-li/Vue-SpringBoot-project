package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Constants;
import com.example.springboot.common.ValidationEnum;
import com.example.springboot.controller.dto.UserDto;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.Menu;
import com.example.springboot.entity.User;
import com.example.springboot.entity.Validation;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.service.IValidationService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 李兆年
 * @since 2022-08-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
//    private static final Log LOG = log.get();
    @Resource
    private RoleMapper roleMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    //引入的是接口
    @Resource
    private IMenuService menuService;

    //引入邮箱验证的接口
    @Resource
    private IValidationService validationService;

    @Resource
    private IUserService userService;

    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null){
//                BeanUtil.copyProperties(one,userDto,true);
            BeanUtils.copyProperties(one,userDto);

            //设置token
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);
//            #根据关系表显示不同的UI页面
            String role = one.getRole();
            List<Menu> roleMenus = getRoleMenus(role);
            userDto.setMenus(roleMenus);
            return userDto;
        }else {
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");

        }


    }

    @Override
    public User register(UserDto userDto) {
        //验证用户名必须唯一
        String username = userDto.getUsername();
        //获得用户表的querywrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //根据用户的邮箱查询到用户
        queryWrapper.eq("username", username);
        User queryUserbyUserName = getOne(queryWrapper);
        //查询user表中是否有此人，有的话，说明邮箱已经注册，不可以再次注册，请使用邮箱进行登录
        if (queryUserbyUserName != null){
            throw new ServiceException("-1","该用户名已存在，请更换用户名！！！");
        }


        //获得用户的邮箱
        String email = userDto.getEmail();
        //获得用户表的querywrapper
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //根据用户的邮箱查询到用户
        queryWrapper.or();
        queryWrapper.eq("email", email);
        User queryUserByEmail = userService.getOne(queryWrapper);
//        User queryUserByEmail = getOne(queryWrapper);
        //查询user表中是否有此人，有的话，说明邮箱已经注册，不可以再次注册，请使用邮箱进行登录
        if (queryUserByEmail != null){
            throw new ServiceException("-1","该邮箱已注册，请使用邮箱登录！！！");
        }

        String code = userDto.getCode();
        int code1 = Integer.parseInt(code);
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("code",code1);
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("type",3);
        validationQueryWrapper.ge("time",new Date());//数据库的时间大于当前时间，说明还没有过期
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation == null){
            throw new ServiceException("-1","验证码已过期，请重新获取验证码");
        }

        User one = getUserInfo(userDto);

        if (one == null ){
            one = new User();
//            BeanUtil.copyProperties(userDto,one,true);
            BeanUtils.copyProperties(userDto,one);
            save(one);
        }else {
            throw new ServiceException(Constants.CODE_600,"用户已经存在");
        }
        return one;

    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update<1){
            throw new ServiceException(Constants.CODE_600,"密码错误");
        }

    }

    @Override
    public UserDto loginEmail(UserDto userDto) {
        //获取到前端接口传递进行的表的信息
        String email = userDto.getEmail();
        String code = userDto.getCode();

        //与数据库中的信息进行校验，如果合法，就进行登录，如果不合法就无法进行登录
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("code",code);
        validationQueryWrapper.ge("time",new Date());//数据库的时间大于当前时间，说明还没有过期
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation == null){
            throw new ServiceException("-1","验证码已过期，请重新获取验证码");
        }
        //如果验证通过了，就查询用户的信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //根据用户的邮箱查询到用户
        queryWrapper.eq("email", email);
        User user = getOne(queryWrapper);
        //查询user表中是否有此人，有的话，进行登录，否则进行报错
        if (user == null){
            throw new ServiceException("-1","该邮箱未注册，请注册后登录！！！");
        }

        BeanUtils.copyProperties(user,userDto);
        //设置token
        String token = TokenUtils.getToken(user.getId().toString(), user.getPassword());
        userDto.setToken(token);


//      查询完以后，根据用户设置菜单；
        String role = user.getRole();
        List<Menu> roleMenus = getRoleMenus(role);
        userDto.setMenus(roleMenus);
        return userDto;


    }

    //实现该接口，发送邮件的具体的接口
    @Override
    public void sendEmailCode(String email) {
        Date currentDate = new Date();

        //先查询同类型的code
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("type",ValidationEnum.LOGIN.getCode());
        validationQueryWrapper.ge("time",currentDate);//数据库的时间大于当前时间，说明还没有过期
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation != null){
            throw new ServiceException("-1","当前您的验证码仍然有效，请不要重复发送");
        }
        //创建邮箱对象，通过邮箱对象设置信息发送短信
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from); //发送人
        message.setSentDate(new Date());
        message.setSubject("【客多多】登录邮箱验证");
        //hutool提供的随机四位数值
        String code = RandomUtil.randomNumbers(4);
        message.setText("您本次邮箱验证码为："+ code+",妥善保管，切勿泄露！");
        //将验证码信息放入到短信中，
        message.setTo(email);
        javaMailSender.send(message);

        //发送完验证码以后就可以将验证码存入到数据库中
        //向数据库中存入邮箱，验证码，type:储存 的验证码的类型；过期时间，使用Hutoll工具产生一个过期时间

        validationService.saveCode(email,code,
                ValidationEnum.LOGIN.getCode(), DateUtil.offsetMinute(currentDate,1));

    }

    @Override
    public void sendEmailCodeForResetPassword(String email) {
        Date currentDate = new Date();
        //先查询同类型的code
        QueryWrapper<Validation> validationQueryWrapper = new QueryWrapper<>();
        validationQueryWrapper.eq("email",email);
        validationQueryWrapper.eq("type",ValidationEnum.USER_REGIST.getCode());
        validationQueryWrapper.ge("time",currentDate);//数据库的时间大于当前时间，说明还没有过期
        Validation validation = validationService.getOne(validationQueryWrapper);
        if (validation != null){
            throw new ServiceException("-1","当前您的验证码仍然有效，请不要重复发送");
        }

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(from); //发送人
        message.setSentDate(new Date());
        message.setSubject("【客多多】密码重置邮箱验证");
        //hutool提供的随机四位数值
        String code = RandomUtil.randomNumbers(4);
        message.setText("您本次邮箱验证码为："+ code+",妥善保管，切勿泄露！");
        //将验证码信息放入到短信中，
        message.setTo(email);
        javaMailSender.send(message);

        //发送完验证码以后就可以将验证码存入到数据库中
        //向数据库中存入邮箱，验证码，type:储存 的验证码的类型；过期时间，使用Hutoll工具产生一个过期时间
        validationService.saveCode(email,code,
                ValidationEnum.USER_REGIST.getCode(), DateUtil.offsetMinute(currentDate,1));

    }

    @Override
    public void verifyUserName(String username) {
        QueryWrapper<User> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("username",username);

        User one = userService.getOne(userqueryWrapper);
        if (one != null){
            //抛出事件，会被前台进行捕获
            throw new ServiceException(Constants.CODE_600,"用户名已经存在，请更换用户名进行注册");
        }


    }


    private User getUserInfo(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);//从数库里边查询用户信息

        } catch (Exception e) {
//            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            boolean isSucces = children.removeIf(child -> !menuIds.contains(child.getId()));
            if (children.size() != 0 && isSucces){
                roleMenus.add(menu);
            }
        }
        return roleMenus;
    }


}
