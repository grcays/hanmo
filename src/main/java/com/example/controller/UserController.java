package com.example.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.Blog;
import com.example.entity.User;
import com.example.service.IBlogService;
import com.example.service.IUserService;
import com.example.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/user")
//@Api(tags = "用户接口 ")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private IBlogService blogService;

    /*
     * @Description: 发送手机验证码
     * @param
     * @return: com.example.dto.Result
     * @Author: 高槐玉
     * @Date: 2023/3/14 20:22
     */
    @PostMapping("code")
//    @ApiOperation(value = "发送验证码接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "phone",value = "用户电话"),
//            @ApiImplicitParam(name = "session",value = "session存储验证码")
//    })
    public Result sendCode(@RequestParam("phone") String phone, HttpSession session){
        //发送手机验证码并且保存到session中
        return userService.sendCode(phone,session);
    }

    @PostMapping("login")
//    @ApiOperation(value = "登录接口")
//    @ApiImplicitParam(name = "loginForm",value = "登录参数表单",required = true)
    public Result login(@RequestBody LoginFormDTO loginForm, HttpSession session){
        return userService.login(loginForm,session);
    }

    @GetMapping("/me")
//    @ApiOperation(value = "查询我的信息接口",notes = "该查询只返回昵称,图片,用户id")
    public Result me(){
        // 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    @GetMapping("myfont")
//    @ApiOperation(value = "我的字帖接口",notes = "返回我的字帖中的数据")
    public Result myFont(){
        return userService.myFont();
    }

    // UserController 根据id查询用户

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable("id") Long userId){
        // 查询详情
        User user = userService.getById(userId);
        if (user == null) {
            return Result.ok();
        }
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        // 返回
        return Result.ok(userDTO);
    }


  //  @GetMapping("/info/{id}")
//    public Result info(@PathVariable("id") Long userId){
//        // 查询详情
//        UserInfo info = userInfoService.getById(userId);
//        if (info == null) {
//            // 没有详情，应该是第一次查看详情
//            return Result.ok();
//        }
//        info.setCreateTime(null);
//        info.setUpdateTime(null);
//        // 返回
//        return Result.ok(info);
//    }

    //@PostMapping
    //@ApiOperation(value = "测试方法(忽略)")
//    @ApiImplicitParam(name = "user",value = "user")
//    public void Test(@RequestBody User user, @RequestBody Follow follow, @RequestBody Font font, @RequestBody WordPost wordPost){
//
//    }
}
