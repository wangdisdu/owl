package com.owl.web.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.exception.BizException;

import java.io.Serializable;

public interface IServiceExtend<T> extends IService<T> {

    default T getById(Serializable id, boolean throwExists, boolean throwNotExists) {
        T t = getById(id);
        if (t != null && throwExists) {
            throw new BizException(ResponseCode.EXIST);
        }
        if (t == null && throwNotExists) {
            throw new BizException(ResponseCode.NOT_EXIST);
        }
        return t;
    }

    default T exists(Serializable id) {
        return getById(id, false, true);
    }

    default T notExists(Serializable id) {
        return getById(id, true, false);
    }
}
