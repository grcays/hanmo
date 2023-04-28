package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
//@ApiModel("用户实体对象")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
//    @ApiModelProperty("用户ID")
    private Long id;

    /**
     * 手机号码
     */
//    @ApiModelProperty("用户手机号")
    private String phone;

    /**
     * 密码，加密存储
     */
//    @ApiModelProperty("用户密码,暂时未用")
    private String password;

    /**
     * 昵称，默认是随机字符
     */
//    @ApiModelProperty("用户昵称")
    private String nickName;

//    @ApiModelProperty("我的字帖")
    private Long myfont;

    /**
     * 用户头像
     */
//    @ApiModelProperty("头像")
    private String icon = "";

    /**
     * 创建时间
     */
//    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
//    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

}
