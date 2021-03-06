package com.zscat.mallplus.config;


import com.zscat.mallplus.bo.AdminUserDetails;
import com.zscat.mallplus.component.JwtAuthenticationTokenFilter;
import com.zscat.mallplus.component.RestAuthenticationEntryPoint;
import com.zscat.mallplus.component.RestfulAccessDeniedHandler;
import com.zscat.mallplus.enums.StatusEnum;
import com.zscat.mallplus.sys.entity.SysPermission;
import com.zscat.mallplus.sys.entity.SysStore;
import com.zscat.mallplus.sys.entity.SysUserVo;
import com.zscat.mallplus.sys.mapper.SysStoreMapper;
import com.zscat.mallplus.sys.mapper.SysUserMapper;
import com.zscat.mallplus.sys.service.ISysUserService;
import com.zscat.mallplus.ums.service.RedisService;
import com.zscat.mallplus.util.JsonUtil;
import com.zscat.mallplus.vo.Rediskey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;
import java.util.List;
//import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;


/**
 * SpringSecurity的配置
 * https://github.com/shenzhuan/mallplus on 2018/4/26.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysStoreMapper storeMapper;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Resource
    private RedisService redisService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()// 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement()// 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, // 允许对于网站静态资源的无授权访问
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                )
                .permitAll()
                .antMatchers("/admin/login", "/admin/register")// 对登录注册要允许匿名访问
                .permitAll()
                /* .and()
                 .logout()
                 .invalidateHttpSession(true)
                 .permitAll()*/
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .antMatchers("/**")//测试时全部运行访问
                .permitAll()
                .anyRequest()// 除上面外的所有请求全部需要鉴权认证
                .authenticated();
        //访问 /logout 表示用户注销，并清空session
        httpSecurity.logout().logoutSuccessUrl("/logoutSuccess");
        // 禁用缓存
        httpSecurity.headers().cacheControl();
        // 添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登录结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        //以下这句就可以控制单个用户只能创建一个session，也就只能在服务器登录一次
//        httpSecurity.sessionManagement().maximumSessions(1);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 应该是在这里做多张用户表的校验
     * @return
     */
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> {
            SysUserVo admin = new SysUserVo();
            if (!redisService.exists(String.format(Rediskey.user, username))) {
                admin = userMapper.selectByUserName(username);
                redisService.set(String.format(Rediskey.user, username), JsonUtil.objectToJson(admin));
            } else {
                admin =JsonUtil.fromJson(redisService.get(String.format(Rediskey.user, username)), SysUserVo.class);
            }
            if (admin==null){
                admin = userMapper.selectByUserName(username);
                //在这里检索第二张表
                if (admin==null){
                    try {
                        List<SysUserVo> admins = jdbcTemplate.queryForList("select\n" +
                                "         id as \"id\"\n" +
                                "         ,username as \"username\",\n" +
                                "         password as \"password\",\n" +
                                "         phone as \"phone\",\n" +
                                "         device_group_id as \"deviceGroupId\",\n" +
                                "         staff_name as \"realname\",\n" +
                                "        create_time as \"createTime\",\n" +
                                "        login_time as \"loginTime\",\n" +
                                "        status as \"status\",\n" +
                                "        supply_id as \"supplyId\",\n" +
                                "        store_id as \"storeId\",\n" +
                                "        store_name as \"storeName\"\n" +
                                "        from sys_user_staff\n" +
                                "        where username =" +"'"+username +"'",SysUserVo.class);
                        if (admins!=null&&admins.size()==1){
                            admin = admins.get(0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                if (admin==null){
                    throw new UsernameNotFoundException("用户名或密码错误");
                }
                redisService.set(String.format(Rediskey.user, username), JsonUtil.objectToJson(admin));
            }
            //  apiContext.setCurrentProviderId(admin.getStoreId());
            if (admin != null) {
                if (admin.getSupplyId() != null && admin.getSupplyId() == 1L) {
                    List<SysPermission> permissionList = sysUserService.listPerms();
                    return new AdminUserDetails(admin, permissionList);
                }
                SysStore store = storeMapper.selectById(admin.getStoreId());
                if (store == null) {
                    throw new UsernameNotFoundException("商户不存在");
                } else {
                    if (store.getStatus() == StatusEnum.AuditType.FAIL.code()) {
                        throw new UsernameNotFoundException("商户审核失败");
                    } else if (store.getStatus() == StatusEnum.AuditType.INIT.code()) {
                        throw new UsernameNotFoundException("商户审核中");
                    }
                }
                List<SysPermission> permissionList = sysUserService.listUserPerms(admin.getId());
                return new AdminUserDetails(admin, permissionList);
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }


    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * 允许跨域调用的过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return new CorsFilter(source);
    }

//    @Bean
//    HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

}
