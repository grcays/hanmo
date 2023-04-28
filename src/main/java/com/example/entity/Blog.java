package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tb_blog")
//@ApiModel("创建动态(博客)对象")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
//    @ApiModelProperty("动态ID")
    private Long id;
    /**
     * 商户id
     */
//    @ApiModelProperty("字帖ID")
    private Long fontId;
    /**
     * 用户id
     */
//    @ApiModelProperty("用户ID")
    private Long userId;
    /**
     * 用户图标
     */
//    @ApiModelProperty("头像")
    @TableField(exist = false)
    private String icon;
    /**
     * 用户姓名
     */
//    @ApiModelProperty("用户名称")
    @TableField(exist = false)
    private String name;
    /**
     * 是否点赞过了
     */
//    @ApiModelProperty("是否已经点赞过")
    @TableField(exist = false)
    private Boolean isLike;

    /**
     * 标题
     */
//    @ApiModelProperty("动态标题")
    private String title;

    /**
     * 探店的照片，最多9张，多张以","隔开
     */
//    @ApiModelProperty("动态图片")
    private String images;

    /**
     * 探店的文字描述
     */
//    @ApiModelProperty("动态描述文本")
    private String content;

    /**
     * 点赞数量
     */
//    @ApiModelProperty("点赞数量")
    private Integer liked;

    /**
     * 评论数量
     */
//    @ApiModelProperty("评论数量")
    private Integer comments;

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
