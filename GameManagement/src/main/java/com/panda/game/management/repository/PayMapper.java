/***
 * @pName management
 * @name PayMapper
 * @user HongWei
 * @date 2018/8/16
 * @desc 财务交易记录表
 */
package com.panda.game.management.repository;

import com.panda.game.management.entity.db.Pays;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayMapper extends  MyMapper<Pays>{
}
