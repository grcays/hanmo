package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.Blog;
import com.example.entity.User;
import com.example.service.IBlogService;
import com.example.service.IUserService;
import com.example.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/blog")
//@Api(tags = "发现页接口")
public class BlogController {
    @Resource
    private IBlogService blogService;

    @Resource
    private IUserService userService;


    @PostMapping
//    @ApiOperation(value = "发布动态")
//    @ApiImplicitParam(name = "blog",value = "博客实体",required = true)
    public Result saveBlog(@RequestBody Blog blog) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        if(blog.getTitle() == null){
            return Result.fail("标题不能为空");
        }
        blog.setUserId(user.getId());
        String images = blog.getImages();
//        String[] split = images.split(",");
//        blog.setImages(split);
        // 保存探店博文
        blogService.save(blog);
        // 返回id
        return Result.ok(blog.getId());
    }

    @PutMapping("/like/{id}")
//    @ApiOperation(value = "修改点赞数量")
//    @ApiImplicitParam(name = "id",value = "动态实体的ID")
    public Result likeBlog(@PathVariable("id") Long id) {
        // 修改点赞数量
        return blogService.likeBlog(id);
    }

    /**
     * 查询我发表的帖子
     * @param
     * @return
     */
    @GetMapping("/of/me")
//    @ApiOperation(value = "我的动态接口",notes = "在我的作品中展示我发表过的动态")
    public Result queryMyBlog() {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        LambdaQueryWrapper<Blog> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Blog::getUserId,user.getId());
        List<Blog> list = blogService.list(lqw);
//        Page<Blog> page = blogService.query()
//                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
       // List<Blog> records = page.getRecords();
        return Result.ok(list);
    }

//    @GetMapping("/hot2")
//    @ApiIgnore()
//    public Result queryHotBlog2(@RequestParam(value = "current", defaultValue = "1") Integer current) {
//        // 根据用户查询
////        Page<Blog> page = blogService.query()
////                //.orderByDesc("liked")
////                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
////        // 获取当前页数据
////        List<Blog> records = page.getRecords();
////        // 查询用户
////        records.forEach(blog ->{
////            Long userId = blog.getUserId();
////            User user = userService.getById(userId);
////            blog.setName(user.getNickName());
////            blog.setIcon(user.getIcon());
////        });
////        return Result.ok(records);
//        // 根据用户查询
//        Page<Blog> BlogPage = new Page<>(current, SystemConstants.MAX_PAGE_SIZE);
//        LambdaQueryWrapper<Blog> lqw = new LambdaQueryWrapper<>();
//        lqw.orderByDesc(Blog::getLiked);
//        blogService.page(BlogPage,lqw);
//        System.out.println("BlogPage是十三点零分:" + BlogPage);
//        //获取当前页数据
//        List<Blog> records = BlogPage.getRecords();
//        records.forEach(blog -> {
//            Long userId = blog.getUserId();
//            User user = userService.getById(userId);
//            blog.setName(user.getNickName());
//            blog.setIcon(user.getIcon());
//        });
//        return Result.ok(records);
//    }

    @GetMapping("/hot")
//    @ApiOperation(value = "所有动态展示接口",notes = "展示所有用户发表过的动态")
    public Result queryHotBlog() {
        // 根据用户查询
        //获取当前页数据
        List<Blog> list = blogService.list();
        List<Blog> records = list.stream().map((ghy)->{
            Long userId = ghy.getUserId();
            User user = userService.getById(userId);
            ghy.setName(user.getNickName());
            ghy.setIcon(user.getIcon());
            return ghy;
        }).collect(Collectors.toList());
        return Result.ok(records);
    }

    @GetMapping("/{id}")
  //  @ApiOperation(value = "根据ID查动态接口")
    public Result checkDetails(@PathVariable("id") String id){
//        Blog one = blogService.getById(id);
//        if (one == null) {
//            return Result.fail("来到了没有知识的荒漠");
//        }
//        User user = userService.getById(one.getUserId());
//        one.setName(user.getNickName());
//        one.setIcon(user.getIcon());
//        return Result.ok(one);
        return blogService.queryBlogById(id);
    }

    @GetMapping("/of/user")
    public Result queryBlogByUserId(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam("id") Long id) {
        // 根据用户查询
//        Page<Blog> page = blogService.query()
//                .eq("user_id", id).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
//        List<Blog> records = page.getRecords();
        LambdaQueryWrapper<Blog> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Blog::getUserId,id);
        List<Blog> list = blogService.list(lqw);
        return Result.ok(list);
    }
}
