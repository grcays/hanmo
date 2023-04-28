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
 * 我的字帖存储
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_myfont")
//@ApiModel("我的字帖实体(关系型实体)")
public class MyFont implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty("关系ID")
    private Long id;

//    @ApiModelProperty("关联用户ID")
    private Long userId;

    //字帖id
//    @ApiModelProperty("关联字帖ID")
    private Long fontId;


    @TableField(fill = FieldFill.INSERT)
//    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;


    //是否删除
//    private Integer isDeleted;

}
