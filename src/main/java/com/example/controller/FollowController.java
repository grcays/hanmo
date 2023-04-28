package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.dto.Result;
import com.example.entity.Follow;
import com.example.entity.User;
import com.example.service.IFollowService;
import com.example.service.IUserService;
import com.example.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/follow")
//@Api(tags = "关注与取关接口")
public class FollowController {
    @Autowired
    private IFollowService followService;

    @Resource
    private IUserService userService;

    @PutMapping("/{followUserId}/{isFollow}")
//    @ApiOperation(value = "关注与取关接口",notes = "如果未关注点一下为关注,标签改变")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "followUserId",value = "偶像ID"),
//            @ApiImplicitParam(name = "isFollow",value = "是否关注,true为是")
//    })
    public Result followSomebody(@PathVariable Long followUserId, @PathVariable Boolean isFollow){
        return followService.followSomebody(followUserId,isFollow);
    }

    @GetMapping("/or/not/{followUserId}")
//    @ApiOperation(value = "判断是否关注接口",notes = "查看一个帖子时,判断该用户是否被当前登录用户关注")
//    @ApiImplicitParam(name = "followUserId",value = "偶像ID")
    public Result isFollow(@PathVariable Long followUserId){
        return followService.isFollow(followUserId);
    }


    @GetMapping("/common/{id}")
//    @ApiOperation(value = "共同关注接口(暂未实现)",notes = "暂未实现,实现后可查看共同关注")
    public Result followCommon(@PathVariable Long id){
        return followService.followCommon(id);
    }

    @GetMapping("/myFollow")
    public Result muFollow( @RequestParam(value = "letteringName", required = false) String name){
        Long id = UserHolder.getUser().getId();
        LambdaQueryWrapper<Follow> LQW = new LambdaQueryWrapper<>();
            LQW.eq(Follow::getUserId, id);
            List<Follow> list = followService.list(LQW);
        if(name == "") {
            List<User> userList = list.stream().map((ghy) -> {
                Long followUserId = ghy.getFollowUserId();
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.eq(User::getId, followUserId);
                User user = userService.getOne(lqw);
                return user;
            }).collect(Collectors.toList());
            return Result.ok(userList);
        }else {
            List<User> userList = list.stream().map((ghy) -> {
                Long followUserId = ghy.getFollowUserId();
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.eq(User::getId, followUserId).like(User::getNickName,name);
                User user = userService.getOne(lqw);
                return user;
            }).collect(Collectors.toList());
            return Result.ok(userList);
        }
    }
}
