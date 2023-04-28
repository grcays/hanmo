package com.example.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 菜品
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_font")
//@ApiModel("字帖实体")
public class Font implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "字帖ID")
    private Long id;

    //字帖名称
//    @ApiModelProperty(value = "字帖名称")
    private String name;


    //字帖分类id
//    @ApiModelProperty(value = "字帖关联的字帖")
    private Long wordpostId;

    //商品码
//    @ApiModelProperty(hidden = true)
    private String code;


    //图片
//    @ApiModelProperty(value = "字帖图片")
    private String image;


    //描述信息
//    @ApiModelProperty(value = "字帖作者")
    private String description;


    @TableField(fill = FieldFill.INSERT)
//    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


//    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
//    @ApiModelProperty(value = "创建用户")
    private Long createUser;


//    @ApiModelProperty(value = "更新用户")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
//    private Integer isDeleted;

}
