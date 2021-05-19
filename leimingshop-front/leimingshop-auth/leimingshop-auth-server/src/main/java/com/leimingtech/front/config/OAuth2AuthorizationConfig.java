/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.config;


import com.leimingtech.front.token.MyRedisTokenStore;
import com.leimingtech.front.token.RedisAuthorizationCodeServices;
import com.leimingtech.front.token.SingleTokenServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Author tyl
 * @Date 2019/5/27 15:10
 * @Description OAuth2授权配置类
 **/
@Slf4j
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${oauth.secret}")
    private String secret;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisAuthorizationCodeServices redisAuthorizationCodeServices;

    /**
     * 配置客户端信息
     * 既可以通过授权码类型获取令牌，也可以通过密码类型获取令牌
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置jdbc验证，验证client信息
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
    }

    /**
     * 它就是用来保存token(封装在OAuth2AccessToken中)。
     * TokenStore的实现类，有InMemoryTokenStore、JdbcTokenStore、JwkTokenStore、JwtTokenStore、RedisTokenStore。
     * InMemoryTokenStore：将OAuth2AccessToken保存在内存中，它有很多的ConcurrentHashMap属性。
     * JdbcTokenStore：将OAuth2AccessToken保存在数据库中，其构造方法需要DataSource，用于构造JdbcTemplate，通过JdbcTemplate来操作数据库。
     * RedisTokenStore：将OAuth2AccessToken保存到Reis中，构造方法需要RedisConnectionFactory，之后通过Connection操作Redis。
     * JwtTokenStore：jwt是吧用户信息放在了token里，所有这个tokenstore并没有后端的存储
     * <p>
     * 自定义tokenStore
     *
     * @date 2020/6/2 11:43
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public TokenStore tokenStore() {
        return new MyRedisTokenStore(redisConnectionFactory);
    }


    /**
     * 配置授权Token的节点和Token服务
     * <p>
     * Spring Cloud Security OAuth2通过DefaultTokenServices类来完成token生成、过期等 OAuth2 标准规定的业务逻辑，
     * 而DefaultTokenServices又是通过TokenStore接口完成对生成数据的持久化。
     * <p>
     * OAuth2的主配置信息
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.authenticationManager(this.authenticationManager);
        endpoints.tokenStore(tokenStore());
        endpoints.authorizationCodeServices(redisAuthorizationCodeServices);
        endpoints.setClientDetailsService(new JdbcClientDetailsService(dataSource));
        SingleTokenServices defaultTokenServices = new SingleTokenServices();
        defaultTokenServices.setSupportRefreshToken(false);
        defaultTokenServices.setTokenStore(endpoints.getTokenStore());
        defaultTokenServices.setClientDetailsService(endpoints.getClientDetailsService());
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        // 配置支持refresh_token
        defaultTokenServices.setSupportRefreshToken(true);
        // 配置refresh_token后失效原refresh_token
        defaultTokenServices.setReuseRefreshToken(false);
        endpoints.tokenServices(defaultTokenServices);
        endpoints.accessTokenConverter(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(secret);
        return jwtAccessTokenConverter;
    }

    /**
     * 检测token的策略
     * 开启/oauth/token_key验证端口无权限访问
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                // 允许form表单客户端认证,允许客户端使用client_id和client_secret获取token
                .allowFormAuthenticationForClients();
    }

}
