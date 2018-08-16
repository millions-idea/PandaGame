/***
 * @pName management
 * @name ExceptionServiceImpl
 * @user HongWei
 * @date 2018/8/13
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.BaseService;
import com.panda.game.management.biz.ExceptionService;
import com.panda.game.management.entity.db.Exceptions;
import com.panda.game.management.repository.ExceptionMapper;
import com.panda.game.management.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ExceptionServiceImpl extends BaseServiceImpl<Exceptions> implements ExceptionService {
    private final ExceptionMapper exceptionMapper;

    @Autowired
    public ExceptionServiceImpl(ExceptionMapper exceptionMapper) {
        this.exceptionMapper = exceptionMapper;
    }


    /**
     * 异步插入记录 韦德 2018年8月13日13:33:29
     *
     * @param exceptions
     * @return
     */
    @Override
    @Async
    public void asyncInsert(Exceptions exceptions) {
        exceptionMapper.insert(exceptions);
    }
}
