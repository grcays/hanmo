package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@ApiModel("返回对象实体")
public class Result {
//    @ApiModelProperty("成功")
    private Boolean success;
//    @ApiModelProperty("错误信息")
    private String errorMsg;
//    @ApiModelProperty("返回数据")
    private Object data;
//    @ApiModelProperty("总计")
    private Long total;
//    private String msg;

    public static Result ok(){
        return new Result(true, null, null, null);
    }
    public static Result ok(Object data){
        return new Result(true, null, data, null);
    }
    public static Result ok1(Object data){
        return new Result(true, null, data, null);
    }
    public static Result ok(List<?> data, Long total){
        return new Result(true, null, data, total);
    }
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null, null);
    }
}
