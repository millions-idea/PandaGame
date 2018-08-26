/***
 * @pName mi-ocr-web-app
 * @name UserDetailsServiceEx
 * @user HongWei
 * @date 2018/7/22
 * @desc
 */
package com.panda.game.management.security;


import com.panda.game.management.biz.IUserService;
import com.panda.game.management.entity.db.Users;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.utils.JsonUtil;
import com.panda.game.management.utils.MD5Util;
import com.panda.game.management.utils.RequestUtil;
import com.panda.game.management.utils.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class SecurityDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        // 通过用户名查询此用户是否为合法用户
        String ip = RequestUtil.getIp(ServletUtil.getRequest());
        Users param = new Users();
        param.setPhone(username);
        param.setIp(ip);
        Users detail = userService.login(param);
        // 检查权限
        if(detail.getLevel() == null || detail.getLevel() != 10) throw new UsernameNotFoundException("您无权访问系统，请向有关部分申请工号！");
        logger.debug(JsonUtil.getJson(detail));
        return new SecurityUserDetails(detail, detail.getPhone(), detail.getPhone(), detail.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
    }


}
