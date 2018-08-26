/***
 * @pName management
 * @name DictionaryService
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz;

import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.resp.GroupInformation;

public interface IDictionaryService extends IBaseService<Dictionary> {

    /**
     * 获取聚合广告信息 韦德 2018年8月18日13:11:46
     * @return
     */
    GroupInformation getGroupInformation();
}
