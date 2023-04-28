package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.Result;
import com.example.entity.SingleWord;
import com.example.service.ISingleWordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("single")
public class SingleWordController {
    @Resource
    private ISingleWordService singleWordService;

    @GetMapping("/id")
    public Result querySingleWord(@RequestParam("id") Long id){
        System.out.println("SingleWord 的 id是" + id);
        SingleWord singleWord = singleWordService.getById(id);
        return Result.ok(singleWord);
    }

    @GetMapping("/of/name")
    public Result queryNameSingleWord(@RequestParam("name") String name){
        LambdaQueryWrapper<SingleWord> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SingleWord::getName,name);
        List<SingleWord> list = singleWordService.list(lqw);
        return Result.ok(list);
    }

    /**
     * 返回字帖所有的单子
     * @param id
     * @return
     */
    @GetMapping("/all")
    public Result queryAllSingleWord(@RequestParam("id") Long id){
        LambdaQueryWrapper<SingleWord> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SingleWord::getFontId,id);
        List<SingleWord> list = singleWordService.list(lqw);
        return Result.ok(list);
    }
}
