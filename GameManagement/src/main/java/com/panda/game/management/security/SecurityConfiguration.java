/***
 * @pName mi-ocr-web-app
 * @name SpringSecurityConfiguration
 * @user HongWei
 * @date 2018/7/21
 * @desc security安全信息框架配置类
 */
package com.panda.game.management.security;


import com.google.common.base.Joiner;
import com.panda.game.management.biz.IPermissionService;
import com.panda.game.management.entity.db.Permission;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.utils.JsonUtil;
import com.panda.game.management.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityDetailsService securityDetailsService;

    @Autowired
    private IPermissionService permissionService;

    @Bean
    public Md5PasswordEncoder md5PasswordEncoder(){
        return new SecurityPasswordEncoder();
    }

    @Bean
    public ReflectionSaltSource reflectionSaltSource(){
        ReflectionSaltSource reflectionSaltSource = new ReflectionSaltSource();
        reflectionSaltSource.setUserPropertyToUse("salt");
        return reflectionSaltSource;
    }

    /**
     * 密码加密
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new LoginAuthenticationProvider();
        provider.setSaltSource(reflectionSaltSource());
        provider.setUserDetailsService(securityDetailsService);
        provider.setPasswordEncoder(md5PasswordEncoder());
        return provider;
    }


    /**
     * 保护机制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers( "/api/**"
                ).permitAll().and();


        List<Permission> list = permissionService.getList();
        if(list == null || list.isEmpty()) throw new InfoException("加载权限列表失败");
        Stream<Permission> permissionStream = list.stream();
        Map<String, List<Permission>> groupMap = permissionStream.collect(Collectors.groupingBy(permission -> permission.getPermissionName()));


        // 多个角色共同拥有的权限
        List<String> permissionPages = new ArrayList<>();
        List<String> uniquePermissionPages = new ArrayList<>();

        for (Map.Entry<String, List<Permission>> entry : groupMap.entrySet()) {
            List<Permission> permissionList = entry.getValue();
            permissionList.forEach(permission -> {
                permissionPages.add(permission.getTargetUrl());
                long count = permissionPages.stream().filter(item -> item.equalsIgnoreCase(permission.getTargetUrl())).count();
                if(count > 1) uniquePermissionPages.add(permission.getTargetUrl());
            });

        }

        // 筛除重复数据，提取共用特性
        List<String> commonPermissionNames = groupMap.entrySet().stream().map(member -> member.getKey()).collect(Collectors.toList());
        String commonPermissionNameJoin = Joiner.on(",").join(commonPermissionNames);

        // 向新map中添加旧数据，并追加共用数据
        Map<String, List<String>> permissionMap = new HashMap<>();
        permissionMap.put(commonPermissionNameJoin, uniquePermissionPages);

        // 获取权限差集
        permissionPages.removeAll(uniquePermissionPages);
        permissionMap.put("ADMIN", permissionPages);

        for (Map.Entry<String, List<String>> entry : permissionMap.entrySet()){
            String[] pages = new String[entry.getValue().size()];
            entry.getValue().toArray(pages);
            if(entry.getKey().contains(",")) {
                String[] names = entry.getKey().split(",");
                http.authorizeRequests()
                        .antMatchers(pages)
                        .hasAnyRole(names).and();
            }else{
                http.authorizeRequests()
                        .antMatchers(pages)
                        .hasRole(entry.getKey()).and();
            }

        }


        /*http.authorizeRequests()
                .antMatchers( "/management/index"
                    ,"/management/finance/accounts", "/management/finance/accounts/*"
                    ,"/management/finance/room/*"
                    ,"/management/finance/withdraw/*" )
                .hasAnyRole("STAFF", "ADMIN").and();

        http.authorizeRequests()
                .antMatchers("/management/finance/pay", "management/finance/pay/*"
                        , "/management/finance/recharge", "/management/finance/recharge/*"
                        , "/management/config/*"
                        , "/management/member", "/management/member/*")
                .hasRole("ADMIN").and();*/



        http
                .formLogin()
                .usernameParameter("username").passwordParameter("password").permitAll()
                .loginPage("/management/bootstrap/login")  // 登录入口
                .loginProcessingUrl("/management/bootstrap/api/login")    // 登录验证接口
                .permitAll()
                .successForwardUrl("/management/index")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        String msg = "SUCCESS";
                        String role = authentication.getAuthorities().stream().findFirst().get().toString();
                        if(role != null && role.length() > 0) {
                            role = role.substring(role.indexOf("_") + 1,  role.length());
                        }
                        out.write("{\"error\":0,\"msg\":\"SUCCESS\",\"role\":\"" + role  + "\"}");
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write("{\"error\":1,\"msg\":\"ERROR\"}");
                        out.flush();
                        out.close();
                    }
                })
                .and().logout().permitAll();;  // 设置无保护机制的路由或页面

        System.out.println("加载安全配置完成");
    }

    /**
     * 排除静态资源
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/frame/**")
                .antMatchers("/css/**")
                .antMatchers("/js/**")
                .antMatchers("/images/**")
                .antMatchers("/layui/**")
                .antMatchers("/fonts/**")
                .antMatchers("/noamd-js/**");
    }

}
