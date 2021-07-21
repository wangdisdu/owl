package com.owl.web.provider;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.core.IntegrationCore;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntegrationBuilderService {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationBuilderService.class);

    public List<IntegrationMeta> scan() {
        try {
            return IntegrationCore.scan();
        } catch (Exception e) {
            logger.error("scan", e);
            throw new BizException(ResponseCode.FAILED, e);
        }
    }

    public IntegrationMeta builder(String builder) {
        List<IntegrationMeta> list = scan();
        for (IntegrationMeta meta : list) {
            if (StrUtil.equals(builder, meta.getBuilder())) {
                return meta;
            }
        }
        return null;
    }

    public byte[] icon(String builder) {
        IntegrationMeta meta = builder(builder);
        if (meta == null || StrUtil.isBlank(meta.getIcon())) {
            return null;
        }
        String path = "/Integration-ICON/" + meta.getIcon();
        ClassPathResource resource = new ClassPathResource(path);
        return resource.readBytes();
    }
}
