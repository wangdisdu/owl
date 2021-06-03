package com.owl.web.controller;

import com.owl.web.common.ContextHolder;
import com.owl.web.common.ResponseHelper;
import com.owl.web.common.annotation.LoginRequired;
import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.Paged;
import com.owl.web.model.ResponseResult;
import com.owl.web.model.user.*;
import com.owl.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@LoginRequired
public class UserController extends V1Controller {
    @Autowired
    private UserService userService;

    @PostMapping("user/list/query")
    public ResponseResult query(@RequestBody UserQueryReq req) {
        Paged<UserResp> page = userService.query(req);
        return new ResponseResult().setTotal(page.getTotal()).setResult(page.getRecords());
    }

    @GetMapping("user")
    public ResponseResult list() {
        Paged<UserResp> page = userService.query(new UserQueryReq());
        return new ResponseResult().setTotal(page.getTotal()).setResult(page.getRecords());
    }

    @PostMapping("user")
    public ResponseResult create(@RequestBody UserCreateReq req) {
        return new ResponseResult().setResult(userService.create(req));
    }

    @PutMapping("user/{account}")
    public ResponseResult update(@PathVariable("account") String account,
                                 @RequestBody UserUpdateReq req) {
        req.setAccount(account);
        return new ResponseResult().setResult(userService.update(req));
    }

    @DeleteMapping("user/{account}")
    public ResponseResult delete(@PathVariable("account") String account) {
        return new ResponseResult().setResult(userService.delete(account));
    }

    @GetMapping("user/{account}")
    public ResponseResult get(@PathVariable("account") String account) {
        return new ResponseResult().setResult(userService.get(account));
    }

    @PutMapping("user/{account}/password")
    public ResponseResult password(@PathVariable("account") String account,
                                   @RequestBody UserUpdatePwdReq req) {
        req.setAccount(account);
        return new ResponseResult().setResult(userService.reset(req));
    }

    @PostMapping("session/login")
    @LoginRequired(required = false)
    public ResponseResult login(@RequestBody UserLoginReq req,
                                HttpServletResponse response) {
        TbUser user = userService.login(req);
        ResponseHelper.refreshCookie(response, user.getSessionToken());
        return new ResponseResult();
    }

    @PostMapping("session/logout")
    public ResponseResult logout(HttpServletResponse response) {
        userService.logout(ContextHolder.loginAccount());
        ResponseHelper.clearCookie(response);
        return new ResponseResult();
    }

    @GetMapping("session/info")
    public ResponseResult session() {
        return new ResponseResult().setResult(userService.get(ContextHolder.loginAccount()));
    }
}
