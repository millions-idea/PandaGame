/***
 * @pName mi-ocr-web-app
 * @name PasswordEncoderEx
 * @user HongWei
 * @date 2018/7/28
 * @desc
 */
package com.panda.game.management.security;

import com.panda.game.management.utils.MD5Util;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class SecurityPasswordEncoder extends Md5PasswordEncoder {
    /**
     * Used by subclasses to generate a merged password and salt <code>String</code>.
     * <P>
     * The generated password will be in the form of <code>password{salt}</code>.
     * </p>
     * <p>
     * A <code>null</code> can be passed to either method, and will be handled correctly.
     * If the <code>salt</code> is <code>null</code> or empty, the resulting generated
     * password will simply be the passed <code>password</code>. The <code>toString</code>
     * method of the <code>salt</code> will be used to represent the salt.
     * </p>
     *
     * @param password the password to be used (can be <code>null</code>)
     * @param salt     the salt to be used (can be <code>null</code>)
     * @param strict   ensures salt doesn't contain the delimiters
     * @return a merged password and salt <code>String</code>
     * @throws IllegalArgumentException if the salt contains '{' or '}' characters.
     */
    @Override
    protected String mergePasswordAndSalt(String password, Object salt, boolean strict) {
        if (password == null) {
            password = "";
        }

        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1)
                    || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException("Cannot use { or } in salt.toString()");
            }
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        }
        else {
            return salt.toString() + password;
        }
    }

    /**
     * Takes a previously encoded password and compares it with a rawpassword after mixing
     * in the salt and encoding that value
     *
     * @param encPass previously encoded password
     * @param rawPass plain text password
     * @param salt    salt to mix into password
     * @return true or false
     */
    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        return encPass.equalsIgnoreCase(MD5Util.md5(salt + rawPass));
        //return super.isPasswordValid(encPass, rawPass, salt);
    }
}
