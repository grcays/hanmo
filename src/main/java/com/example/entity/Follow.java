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
@TableName("tb_follow")
//@ApiModel("关注的关系实体")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
//    @ApiModelProperty("此ID是关系性ID")
    private Long id;

    /**
     * 用户id
     */
//    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 关联的用户id
     */
//    @ApiModelProperty("偶像ID")
    private Long followUserId;

    /**
     * 创建时间
     */
//    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


}
