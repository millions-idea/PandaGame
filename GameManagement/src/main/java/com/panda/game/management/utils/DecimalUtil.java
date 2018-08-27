/***
 * @pName management
 * @name DecimalUtil
 * @user HongWei
 * @date 2018/8/27
 * @desc
 */
package com.panda.game.management.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DecimalUtil {
    public static BigDecimal toDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }
}
