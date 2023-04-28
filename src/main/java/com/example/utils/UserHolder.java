package com.example.utils;

import com.example.dto.UserDTO;

public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
//        User user = new User();
//        user.setPhone("13107178555");
//        user.setId(666L);
//        user.setNickName("我是测试用户");
//        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(LocalDateTime.now());

//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(666L);
//        userDTO.setNickName("我是测试用户");
//        saveUser(userDTO);
        UserDTO userDTO = tl.get();
        //System.out.println("用户昵称是:" + userDTO.getNickName());
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
