package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.LoginFormDTO;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.Font;
import com.example.entity.MyFont;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.service.IFontService;
import com.example.service.IMyFontService;
import com.example.service.IUserService;
import com.example.utils.RegexUtils;
import com.example.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.utils.RedisConstants.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IMyFontService myFontService;

    @Resource
    private IFontService fontService;

    @Override
    public Result sendCode(String phone, HttpSession session) {
        //1.判断手机号码格式是否正确
        boolean phoneInvalid = RegexUtils.isPhoneInvalid(phone);
        if (phoneInvalid) {
            return Result.fail("手机格式错误");
        }
        //2.发送验证码

        String code = RandomUtil.randomNumbers(4);
        //3.奖验证码保存到session中
        session.setAttribute("code",code);
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code,2, TimeUnit.MINUTES);
        System.out.println(code);
        //SMSUtils.sendMessage("翰墨书法","SMS_270310556",phone,code);
        return Result.ok();
    }

    /**
     * 查看我的字帖中所有加入的字帖
     * @return
     */
    @Override
    public Result myFont() {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getId();
        LambdaQueryWrapper<MyFont> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MyFont::getUserId,userId).orderByDesc(MyFont::getUpdateTime);
        List<MyFont> list = myFontService.list(lqw);
        List<Font> fontList = list.stream().map((ghy) -> {
            Long fontId = ghy.getFontId();
            return fontService.getById(fontId);
        }).collect(Collectors.toList());
        return Result.ok(fontList);
    }

    @Override
    public Result login(LoginFormDTO loginFormDTO,HttpSession session) {
        String code = loginFormDTO.getCode();
        String phone = loginFormDTO.getPhone();
        if (StrUtil.isBlank(code)) {
            return Result.fail("验证码为空");
        }
//        session.getAttribute()
        String tureCode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + phone);
        if (code.equals(tureCode)) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone,phone);
            User user = this.getOne(lambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setNickName("GHY_" + RandomUtil.randomString(6));
                save(user);
            }
            //保存用户信息到redis中

            String token = UUID.randomUUID().toString();
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) ->
                                    fieldValue.toString()));

            String tokenKey = LOGIN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey,userMap);
            //设置token有效期
            stringRedisTemplate.expire(tokenKey,LOGIN_USER_TTL,TimeUnit.MINUTES);
            System.out.println("UserService Token:" + token);
            return Result.ok(token);
        }
        return Result.fail("验证码错误");
    }
}
