package com.panda.game.management.repository;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
    //TODO 特别注意，该接口不能被扫描到，否则会出错
}
