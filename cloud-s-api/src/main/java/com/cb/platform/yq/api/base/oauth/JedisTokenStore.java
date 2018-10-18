package com.cb.platform.yq.api.base.oauth;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;

import java.util.*;

public class JedisTokenStore
        implements TokenStore {
    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";
    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    private String prefix = "";

    public JedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private RedisConnection getConnection() {
        return this.connectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return this.serializationStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return serialize(new StringBuilder().append(this.prefix).append(object).toString());
    }

    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return (OAuth2AccessToken) this.serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return (OAuth2Authentication) this.serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
        return (OAuth2RefreshToken) this.serializationStrategy.deserialize(bytes, OAuth2RefreshToken.class);
    }

    private byte[] serialize(String string) {
        return this.serializationStrategy.serialize(string);
    }

    private String deserializeString(byte[] bytes) {
        return this.serializationStrategy.deserializeString(bytes);
    }

    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = this.authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = serializeKey(new StringBuilder().append("auth_to_access:").append(key).toString());
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        if (accessToken != null) {
            OAuth2Authentication storedAuthentication = readAuthentication(accessToken.getValue());
            if ((storedAuthentication == null) || (!key.equals(this.authenticationKeyGenerator.extractKey(storedAuthentication)))) {
                storeAccessToken(accessToken, authentication);
            }
        }

        return accessToken;
    }

    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(serializeKey(new StringBuilder().append("auth:").append(token).toString()));
        } finally {
            conn.close();
        }
        OAuth2Authentication auth = deserializeAuthentication(bytes);
        return auth;
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(new StringBuilder().append("refresh_auth:").append(token).toString()));
            OAuth2Authentication auth = deserializeAuthentication(bytes);
            OAuth2Authentication localOAuth2Authentication1 = auth;
            return localOAuth2Authentication1;
        }catch (Exception e){
            throw e;
        }finally {
            conn.close();
        }
    }

    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        byte[] serializedAccessToken = serialize(token);
        byte[] serializedAuth = serialize(authentication);
        byte[] accessKey = serializeKey(new StringBuilder().append("access:").append(token.getValue()).toString());
        byte[] authKey = serializeKey(new StringBuilder().append("auth:").append(token.getValue()).toString());
        byte[] authToAccessKey = serializeKey(new StringBuilder().append("auth_to_access:").append(this.authenticationKeyGenerator.extractKey(authentication)).toString());
        byte[] approvalKey = serializeKey(new StringBuilder().append("uname_to_access:").append(getApprovalKey(authentication)).toString());
        byte[] clientId = serializeKey(new StringBuilder().append("client_id_to_access:").append(authentication.getOAuth2Request().getClientId()).toString());

        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(accessKey, serializedAccessToken);

            conn.stringCommands().set(authKey, serializedAuth);

            conn.stringCommands().set(authToAccessKey, serializedAccessToken);

            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, new byte[][]{serializedAccessToken});
            }
            conn.rPush(clientId, new byte[][]{serializedAccessToken});
            if (token.getExpiration() != null) {
                int seconds = token.getExpiresIn();
                conn.expire(accessKey, seconds);
                conn.expire(authKey, seconds);
                conn.expire(authToAccessKey, seconds);
                conn.expire(clientId, seconds);
                conn.expire(approvalKey, seconds);
            }
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if ((refreshToken != null) && (refreshToken.getValue() != null)) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(new StringBuilder().append("refresh_to_access:").append(token.getRefreshToken().getValue()).toString());
                conn.stringCommands().set(refreshToAccessKey, auth);

                byte[] accessToRefreshKey = serializeKey(new StringBuilder().append("access_to_refresh:").append(token.getValue()).toString());
                conn.stringCommands().set(accessToRefreshKey, refresh);

                if ((refreshToken instanceof ExpiringOAuth2RefreshToken)) {
                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                    Date expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
                        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                                .intValue();
                        conn.expire(refreshToAccessKey, seconds);
                        conn.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication
                .getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return new StringBuilder().append(clientId).append(userName == null ? "" : new StringBuilder().append(":").append(userName).toString()).toString();
    }

    public void removeAccessToken(OAuth2AccessToken accessToken) {
        removeAccessToken(accessToken.getValue());
    }

    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = serializeKey(new StringBuilder().append("access:").append(tokenValue).toString());
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        return accessToken;
    }

    public void removeAccessToken(String tokenValue) {
        byte[] accessKey = serializeKey(new StringBuilder().append("access:").append(tokenValue).toString());
        byte[] authKey = serializeKey(new StringBuilder().append("auth:").append(tokenValue).toString());
        byte[] accessToRefreshKey = serializeKey(new StringBuilder().append("access_to_refresh:").append(tokenValue).toString());
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(accessKey);
            conn.get(authKey);
            conn.del(new byte[][]{accessKey});
            conn.del(new byte[][]{accessToRefreshKey});

            conn.del(new byte[][]{authKey});
            List results = conn.closePipeline();
            byte[] access = (byte[]) (byte[]) results.get(0);
            byte[] auth = (byte[]) (byte[]) results.get(1);

            OAuth2Authentication authentication = deserializeAuthentication(auth);
            if (authentication != null) {
                String key = this.authenticationKeyGenerator.extractKey(authentication);
                byte[] authToAccessKey = serializeKey(new StringBuilder().append("auth_to_access:").append(key).toString());
                byte[] unameKey = serializeKey(new StringBuilder().append("uname_to_access:").append(getApprovalKey(authentication)).toString());
                byte[] clientId = serializeKey(new StringBuilder().append("client_id_to_access:").append(authentication.getOAuth2Request().getClientId()).toString());
                conn.openPipeline();
                conn.del(new byte[][]{authToAccessKey});
                conn.lRem(unameKey, 1L, access);
                conn.lRem(clientId, 1L, access);
                conn.del(new byte[][]{serialize(new StringBuilder().append("access:").append(key).toString())});
                conn.closePipeline();
            }
        } finally {
            conn.close();
        }
    }

    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = serializeKey(new StringBuilder().append("refresh:").append(refreshToken.getValue()).toString());
        byte[] refreshAuthKey = serializeKey(new StringBuilder().append("refresh_auth:").append(refreshToken.getValue()).toString());
        byte[] serializedRefreshToken = serialize(refreshToken);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(refreshKey, serializedRefreshToken);

            conn.stringCommands().set(refreshAuthKey, serialize(authentication));

            if ((refreshToken instanceof ExpiringOAuth2RefreshToken)) {
                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                Date expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                            .intValue();
                    conn.expire(refreshKey, seconds);
                    conn.expire(refreshAuthKey, seconds);
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        byte[] key = serializeKey(new StringBuilder().append("refresh:").append(tokenValue).toString());
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        OAuth2RefreshToken refreshToken = deserializeRefreshToken(bytes);
        return refreshToken;
    }

    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = serializeKey(new StringBuilder().append("refresh:").append(tokenValue).toString());
        byte[] refreshAuthKey = serializeKey(new StringBuilder().append("refresh_auth:").append(tokenValue).toString());
        byte[] refresh2AccessKey = serializeKey(new StringBuilder().append("refresh_to_access:").append(tokenValue).toString());
        byte[] access2RefreshKey = serializeKey(new StringBuilder().append("access_to_refresh:").append(tokenValue).toString());
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(new byte[][]{refreshKey});
            conn.del(new byte[][]{refreshAuthKey});
            conn.del(new byte[][]{refresh2AccessKey});
            conn.del(new byte[][]{access2RefreshKey});
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = serializeKey(new StringBuilder().append("refresh_to_access:").append(refreshToken).toString());
        List results = null;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(new byte[][]{key});
            results = conn.closePipeline();
        } finally {
            conn.close();
        }
        if (results == null) {
            return;
        }
        byte[] bytes = (byte[]) (byte[]) results.get(0);
        String accessToken = deserializeString(bytes);
        if (accessToken != null)
            removeAccessToken(accessToken);
    }

    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] approvalKey = serializeKey(new StringBuilder().append("uname_to_access:").append(getApprovalKey(clientId, userName)).toString());
        List<byte[]> byteList = null;
        RedisConnection conn = getConnection();
        try {
            byteList = conn.lRange(approvalKey, 0L, -1L);
        } finally {
            conn.close();
        }
        if ((byteList == null) || (byteList.size() == 0)) {
            return Collections.emptySet();
        }
        Object accessTokens = new ArrayList(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            ((List) accessTokens).add(accessToken);
        }
        return (Collection<OAuth2AccessToken>) Collections.unmodifiableCollection((Collection) accessTokens);
    }

    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        byte[] key = serializeKey(new StringBuilder().append("client_id_to_access:").append(clientId).toString());
        List<byte[]> byteList = null;
        RedisConnection conn = getConnection();
        try {
            byteList = conn.lRange(key, 0L, -1L);
        } finally {
            conn.close();
        }
        if ((byteList == null) || (byteList.size() == 0)) {
            return Collections.emptySet();
        }
        Object accessTokens = new ArrayList(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            ((List) accessTokens).add(accessToken);
        }
        return (Collection<OAuth2AccessToken>) Collections.unmodifiableCollection((Collection) accessTokens);
    }
}
