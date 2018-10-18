package com.cb.platform.yq.api.base.oauth.server;

import com.cb.platform.yq.base.filepath.constant.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;

/**
 * 加载客户端权限服务
 */
public class LoadClientPermissionService extends JdbcClientDetailsService {
    public static Logger logger = LoggerFactory.getLogger(LoadClientPermissionService.class);

    public LoadClientPermissionService(DataSource dataSource) {
        super(dataSource);
    }


    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        if(clientDetails == null){
            if(SystemProperties.loggerFalg){
                logger.debug("客户端资料（ClientDetails）查询不到");
            }
        }
        return clientDetails;
    }
}
