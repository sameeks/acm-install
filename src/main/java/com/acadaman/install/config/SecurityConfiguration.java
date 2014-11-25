package com.acadaman.install.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource()).usersByUsernameQuery("SELECT username, password, activated FROM client_account WHERE username=?").authoritiesByUsernameQuery("SELECT username, client_account_id, role FROM client_account_role WHERE username=?");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public DataSource dataSource() {
        try{
            if(dataSource == null){
                BasicDataSource basicDataSource = new BasicDataSource();
                basicDataSource.setUrl("jdbc:postgresql://localhost:5432/edu_oauth2");
                basicDataSource.setUsername("postgres");
                basicDataSource.setPassword("postgres");
                basicDataSource.setDefaultAutoCommit(true);
                basicDataSource.setDriverClassName("org.postgresql.Driver");
                basicDataSource.setMaxActive(25);
                dataSource = basicDataSource;
            }
            return dataSource;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
