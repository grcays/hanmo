package com.example.dto;

import lombok.Data;

@Data
//@ApiModel("登录表达信息实体")
public class LoginFormDTO {
//    @ApiModelProperty("登录用户手机号")
    private String phone;
//    @ApiModelProperty("登录用户验证码")
    private String code;
//    @ApiModelProperty("登录用户密码,暂时未实现密码登录功能")
    private String password;
}
