package com.owl.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.owl.web.common.ContextHolder;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.config.AppConfig;
import com.owl.web.common.exception.BizException;
import com.owl.web.dao.entity.TbUser;
import com.owl.web.dao.service.TbUserService;
import com.owl.web.model.Paged;
import com.owl.web.model.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private TbUserService tbUserService;

    public Paged<UserResp> query(UserQueryReq req) {
        IPage<TbUser> page = tbUserService.page(req.page(), req.query());
        List<UserResp> items = page.getRecords().stream()
                .map(i -> (UserResp) new UserResp().copyFrom(i))
                .collect(Collectors.toList());
        return new Paged<>(page.getTotal(), items);
    }

    public UserResp get(String account) {
        TbUser user = tbUserService.exists(account);
        return (UserResp) new UserResp().copyFrom(user);
    }

    public UserResp create(UserCreateReq req) {
        tbUserService.notExists(req.getPassword());
        Long now = System.currentTimeMillis();
        TbUser user = req.copyTo(new TbUser());
        user.setPassword(md5(req.getPassword()));
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setCreateBy(ContextHolder.loginAccount());
        user.setUpdateBy(ContextHolder.loginAccount());
        tbUserService.save(user);
        return get(user.getAccount());
    }

    public UserResp update(UserUpdateReq req) {
        Long now = System.currentTimeMillis();
        TbUser user = tbUserService.exists(req.getAccount());
        user = req.copyTo(user);
        user.setSessionToken(null);
        user.setSessionExpire(null);
        user.setUpdateTime(now);
        user.setUpdateBy(ContextHolder.loginAccount());
        tbUserService.updateById(user);
        return get(user.getAccount());
    }

    public UserResp delete(String account) {
        UserResp resp = get(account);
        tbUserService.removeById(account);
        return resp;
    }

    public UserResp reset(UserUpdatePwdReq req) {
        Long now = System.currentTimeMillis();
        TbUser user = tbUserService.exists(req.getAccount());
        user.setPassword(md5(req.getPassword()));
        user.setSessionToken(null);
        user.setSessionExpire(null);
        user.setUpdateTime(now);
        user.setUpdateBy(ContextHolder.loginAccount());
        tbUserService.updateById(user);
        return get(user.getAccount());
    }

    public TbUser login(UserLoginReq req) {
        TbUser user = tbUserService.getOne(
                new LambdaQueryWrapper<TbUser>()
                        .eq(TbUser::getAccount, req.getAccount())
                        .eq(TbUser::getPassword, md5(req.getPassword()))
        );
        if (user == null) {
            throw new BizException(ResponseCode.FAILED);
        }
        Long now = System.currentTimeMillis();
        Long expire = now + AppConfig.COOKIE_TTL_MS;
        user.setSessionTime(now);
        user.setSessionExpire(expire);
        user.setSessionToken(UUID.randomUUID().toString());
        tbUserService.updateById(user);
        return user;
    }

    public TbUser logout(String account) {
        tbUserService.update(new LambdaUpdateWrapper<TbUser>()
                .set(TbUser::getSessionToken, null)
                .set(TbUser::getSessionExpire, null)
                .set(TbUser::getSessionTime, null)
                .eq(TbUser::getAccount, account)
        );
        return tbUserService.getById(account);
    }

    public TbUser getUserByToken(String token) {
        Long now = System.currentTimeMillis();
        TbUser user = tbUserService.getOne(
                new LambdaQueryWrapper<TbUser>()
                        .eq(TbUser::getSessionToken, token));
        if (user == null || user.getSessionExpire() == null) {
            return null;
        }
        if (user.getSessionExpire() >= now) {
            Long expire = now + AppConfig.COOKIE_TTL_MS;
            user.setSessionExpire(expire);
            tbUserService.updateById(user);
            return user;
        }
        return null;
    }

    private String md5(String password) {
        return DigestUtils.md5DigestAsHex((password + AppConfig.SALT).getBytes());
    }
}
