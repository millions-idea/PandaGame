/***
 * @pName management
 * @name DictionaryConfiguration
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.config;

import com.panda.game.management.biz.DictionaryService;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DictionaryConfiguration {
    @Autowired
    private DictionaryService dictionaryService;

    @Bean
    public List<Dictionary> loadDictionary(){
        List<Dictionary> list = dictionaryService.getList();
        if(list == null || list.isEmpty()) throw new InfoException("");
        return list;
    }

    @Bean
    public Map<String,Dictionary> initDictionary(){
        List<Dictionary> dictionaryList = loadDictionary();
        Map<String,Dictionary> dataDictionaryList = new HashMap<>();
        dictionaryList.forEach(dictionary -> dataDictionaryList.put(dictionary.getKey() , dictionary));
        DataDictionary.DATA_DICTIONARY = dataDictionaryList;
        return dataDictionaryList;
    }
}
