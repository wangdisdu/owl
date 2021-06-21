package com.owl.web.provider;

import com.owl.api.IntegrationConnection;
import com.owl.api.IntegrationContext;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.api.schema.DataFrame;
import com.owl.api.schema.IntegrationSchema;
import com.owl.common.JsonUtil;
import com.owl.core.BuilderConfig;
import com.owl.core.IntegrationCore;
import com.owl.core.IntegrationPool;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.exception.BizException;
import com.owl.web.dao.entity.TbIntegration;
import com.owl.web.model.integration.IntegrationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.List;

@Service
public class IntegrationPoolService {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationPoolService.class);

    public List<IntegrationMeta> scan() {
        try {
            return IntegrationCore.scan();
        } catch (Exception e) {
            logger.error("scan", e);
            throw new BizException(ResponseCode.FAILED, e);
        }
    }

    public IntegrationSchema schema(TbIntegration integration) {
        IntegrationConnection connection = connect(integration, null);
        return connection.getSchema();
    }

    public DataFrame query(TbIntegration integration, IntegrationQuery query) {
        LoadDataFrameHandler handler = new LoadDataFrameHandler(query);
        connect(integration, handler);
        return handler.getDataFrame();
    }

    private IntegrationConnection connect(TbIntegration integration, ConnectionHandler handler) {
        IntegrationConnection connection = null;
        try {
            BuilderConfig config = new BuilderConfig();
            config.setBuilder(integration.getBuilder());
            config.setConfig(JsonUtil.decode2Map(integration.getConfig()));
            String connectionId = DigestUtils.md5DigestAsHex(JsonUtil.encode(integration).getBytes());
            IntegrationContext context = new IntegrationContext(integration.getName(), connectionId);
            connection = IntegrationPool.connect(context, config);
        } catch (Exception e) {
            logger.error("connect", e);
            if(connection != null) {
                try { connection.close(); } catch (IOException ignore) {}
            }
            throw new BizException(ResponseCode.FAILED, e);
        }
        if(handler != null) {
            try {
                handler.handle(connection);
            } catch (Exception e) {
                throw new BizException(ResponseCode.FAILED, e);
            }
        }
        return connection;
    }
}
