package com.example.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 单字实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_singleword")
public class SingleWord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer fontId;

    private String name;

    private String image;

    private String author;
}
