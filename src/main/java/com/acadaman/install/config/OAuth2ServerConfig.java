package com.acadaman.install.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
public class OAuth2ServerConfig {

    private static final String ACADAMAN_RESOURCE_ID = "acminit";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(ACADAMAN_RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
        
            http.requestMatchers().antMatchers("/install/**", "/oauth/users/**", "/oauth/clients/**","/me")
                .and()
                .authorizeRequests()
                .antMatchers("/install").access("hasRole('ROLE_USER')")
                .antMatchers("/install/**").access("#oauth2.hasScope('read')");
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private UserApprovalHandler userApprovalHandler;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private DataSource dataSource;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
 
            clients.jdbc(dataSource).withClient("system")
                    .resourceIds(ACADAMAN_RESOURCE_ID)
                    .authorizedGrantTypes("authorization_code", "implicit")
                    .authorities("ROLE_CLIENT")
                    .scopes("read", "write")
                    .secret("secret")
                .and()
                    .withClient("trusted-acadaman-local")
                        .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                        .scopes("read", "write", "trust")
                        .accessTokenValiditySeconds(60)
                .and()
                    .withClient("trusted-acadaman-core-with-secret")
                        .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
                        .scopes("read", "write", "trust")
                        .secret("somesecret");
        }

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        public DataSource dataSource() {
            return dataSource;   
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception
        {
            oauthServer.realm("acminit/client");
        }
    }
}
