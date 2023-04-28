package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SingleWord;
import com.example.mapper.SingleWordMapper;
import com.example.service.ISingleWordService;
import org.springframework.stereotype.Service;

@Service
public class SingleWordServiceImpl extends ServiceImpl<SingleWordMapper, SingleWord> implements ISingleWordService {
}
