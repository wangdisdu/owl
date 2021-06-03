package com.owl.web.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.owl.web.dao.entity.TbUser;
import com.owl.web.dao.mapper.TbUserMapper;
import com.owl.web.dao.service.TbUserService;
import org.springframework.stereotype.Service;

@Service
public class TbUserServiceImpl extends ServiceImpl<TbUserMapper, TbUser> implements TbUserService {

}