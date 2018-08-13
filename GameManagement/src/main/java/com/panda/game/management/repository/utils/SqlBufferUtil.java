/***
 * @pName proback
 * @name BatchOperationUtil
 * @user HongWei
 * @date 2018/8/5
 * @desc
 */
package com.panda.game.management.repository.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * sql扩展工具
 */
public class SqlBufferUtil {
    /**
     * 根据db类字段生成columns数据
     * @param clazz
     * @param excludePatterns
     * @return
     */
    public static String getColumns(Class clazz, List<String> excludePatterns)  {
        StringBuffer buffer = new StringBuffer();
        Field[] aClassFields = clazz.getDeclaredFields();
        Arrays.stream(aClassFields)
                .forEach(item -> {
                    item.setAccessible(true);
                    if(!excludePatterns.contains(item.getName())){
                        buffer.append(item.getName() + ",");
                    }
                });
        return buffer.toString().substring(0, buffer.toString().length()-1);
    }
}
