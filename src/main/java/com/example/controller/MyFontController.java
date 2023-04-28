package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.Result;
import com.example.entity.Font;
import com.example.entity.MyFont;
import com.example.service.IFontService;
import com.example.service.IMyFontService;
import com.example.utils.UserHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/myfont")
//@Api(tags = "我的字帖接口")
public class MyFontController {

    @Resource
    private IMyFontService myFontService;

    @Resource
    private IFontService fontService;

    @GetMapping("test")
//    @ApiIgnore
    public void test(){
        System.out.println("zhe shi yi tiao test");
    }

    @PutMapping("/add/{id}")
//    @ApiOperation(value = "新增字帖接口",notes = "在字帖栏中,用户可以点击图标+,实现将字帖添加到我的字帖里面")
//    @ApiImplicitParam(name = "id",value = "添加的字体id")
    public Result addFont(@PathVariable("id") Long fontId){
        return myFontService.addFont(fontId);
    }

    @DeleteMapping("/{id}")
    public Result deleteFont(@PathVariable("id") Long id){
        return myFontService.deleteFont(id);
    }

    @GetMapping("/recent")
    public Result recentFont(){
        LambdaQueryWrapper<MyFont> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MyFont::getUserId, UserHolder.getUser().getId());
        lqw.orderByDesc(MyFont::getUpdateTime).last("limit 1");
        MyFont lastFont = myFontService.getOne(lqw);
        Long fontId = lastFont.getFontId();
        LambdaQueryWrapper<Font> lqw2 = new LambdaQueryWrapper<>();
        lqw2.eq(Font::getId,fontId);
        Font one = fontService.getOne(lqw2);
        return Result.ok(one);
    }
}
