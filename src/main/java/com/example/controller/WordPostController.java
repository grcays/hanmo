package com.example.controller;

import com.example.dto.Result;
import com.example.service.IWordPostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/wordPost")
//@Api(tags = "字体接口")
public class WordPostController {

    @Resource
    private IWordPostService wordPostService;

    @GetMapping("list")
//    @ApiOperation(value = "展示字体接口",notes = "在练字导航栏中中返回所有的字体信息,如楷书,行书等")
    public Result listAll(){
        return wordPostService.listWordPost();
    }
}
