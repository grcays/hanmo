package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.entity.User;

import javax.servlet.http.HttpSession;

/*
 * @Description: 用户登陆操作
 * @param null
 * @return:
 * @Author: 高槐玉
 * @Date: 2023/3/14 19:59
 */
public interface IUserService extends IService<User> {
    Result login(LoginFormDTO login,HttpSession session);

    Result sendCode(String phone, HttpSession session);

    Result myFont();
}
