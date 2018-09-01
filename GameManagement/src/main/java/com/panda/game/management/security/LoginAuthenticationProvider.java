/***
 * @pName management
 * @name LoginAuthenticationProvider
 * @user HongWei
 * @date 2018/9/1
 * @desc
 */
package com.panda.game.management.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class LoginAuthenticationProvider extends DaoAuthenticationProvider {
    private SecurityPasswordEncoder passwordEncoder;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String presentedPassword = authentication.getCredentials().toString();
        Object salf = super.getSaltSource().getSalt(userDetails);
        if(!super.getPasswordEncoder().isPasswordValid(userDetails.getPassword(), presentedPassword, salf)){
            logger.debug("Authentication failed: password does not match stored value");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "密码错误"));
        }

    }
}
