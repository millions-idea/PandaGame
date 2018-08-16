/***
 * @pName management
 * @name DictionaryServiceImpl
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.DictionaryService;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.repository.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements DictionaryService {
    private final DictionaryMapper dictionaryMapper;

    @Autowired
    public DictionaryServiceImpl(DictionaryMapper dictionaryMapper) {
        this.dictionaryMapper = dictionaryMapper;
    }

    /**
     * 获取全部数据 韦德 2018年8月13日13:26:57
     *
     * @return
     */
    @Override
    public List<Dictionary> getList() {
        return dictionaryMapper.selectAll();
    }
}
