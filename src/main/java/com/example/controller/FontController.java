package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.Result;
import com.example.entity.Font;
import com.example.service.IFontService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/font")
//@Api(tags = "字帖接口")
public class FontController {
    @Resource
    private IFontService fontService;

    @GetMapping("/list")
//    @ApiOperation(value = "展示所有字帖接口",notes = "返回数据库所有字帖数据,前端通过字体ID分类展示"   )
    public Result fontList(){
        return fontService.fontList();
    }

    /**
     * 根据商铺名称关键字分页查询商铺信息
     * @param name 商铺名称关键字
     * @param
     * @return 商铺列表
     */
    @GetMapping("/of/name")
//    @ApiOperation(value = "查询字帖接口",notes = "根据传入的name实现模糊查询")
//    @ApiImplicitParam(name = "name",value = "此name为用户输入要查询的字符串")
    public Result queryShopByName(
            @RequestParam(value = "name", required = false) String name
    ) {
        // 根据类型分页查询
        LambdaQueryWrapper<Font> lqw = new LambdaQueryWrapper<>();
        if(name != "") {
            lqw.like(Font::getName,name);
            List<Font> list = fontService.list(lqw);
            return Result.ok(list);
        }else {
            return fontService.fontList();
        }
//        Page<Font> page = fontService.query()
//                .like(StrUtil.isNotBlank(name), "name", name)
//                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 返回数据
        //return Result.ok(page.getRecords());
    }
//    @PostMapping("/isAdd")
//    public Result isAdd()
}
