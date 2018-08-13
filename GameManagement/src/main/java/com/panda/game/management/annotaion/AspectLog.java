/***
 * @pName proback
 * @name FinanceLog
 * @user HongWei
 * @date 2018/8/5
 * @desc
 */
package com.panda.game.management.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 切面日志
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectLog {
    String description() default "";
}
