/***
 * @pName management
 * @name DictionaryServiceImpl
 * @user HongWei
 * @date 2018/8/16
 * @desc
 */
package com.panda.game.management.biz.impl;

import com.panda.game.management.biz.IDictionaryService;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.resp.GroupInformation;
import com.panda.game.management.repository.DictionaryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements IDictionaryService {
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

    /**
     * 获取聚合广告信息 韦德 2018年8月18日13:11:46
     *
     * @return
     */
    @Override
    public GroupInformation getGroupInformation() {
        GroupInformation groupInformation = new GroupInformation();
        List<Dictionary> list = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("home.group.info")).collect(Collectors.toList());
        List<String> runnerAds = list.stream().filter(d -> d.getKey().contains("top.runner"))
                .map(d -> d.getValue()).collect(Collectors.toList());
        groupInformation.setTopRunnerAds(runnerAds);

        String textAd = list.stream().filter(d -> d.getKey().contains("home.group.info.top.text-ad")).findFirst().get().getValue();
        groupInformation.setTopTextAd(textAd);

        String leftAdTitle = list.stream().filter(d -> d.getKey().contains("home.group.info.center.left-ad.title")).findFirst().get().getValue();
        groupInformation.setCenterLeftAdTitle(leftAdTitle);

        String leftAdDesc = list.stream().filter(d -> d.getKey().contains("home.group.info.center.left-ad.desc")).findFirst().get().getValue();
        groupInformation.setCenterLeftAdDesc(leftAdDesc);

        String rightAdTitle = list.stream().filter(d -> d.getKey().contains("home.group.info.center.right-ad.title")).findFirst().get().getValue();
        groupInformation.setCenterRightAdTitle(rightAdTitle);

        String rightAdDesc = list.stream().filter(d -> d.getKey().contains("home.group.info.center.right-ad.desc")).findFirst().get().getValue();
        groupInformation.setCenterRightAdDesc(rightAdDesc);

        String bottomAdTitle = list.stream().filter(d -> d.getKey().contains("home.group.info.center.bottom-ad.title")).findFirst().get().getValue();
        groupInformation.setCenterBottomAdTitle(bottomAdTitle);

        String bottomAdDesc = list.stream().filter(d -> d.getKey().contains("home.group.info.center.bottom-ad.desc")).findFirst().get().getValue();
        groupInformation.setCenterBottomAdDesc(bottomAdDesc);

        String bottomQcode = list.stream().filter(d -> d.getKey().contains("home.group.info.center.bottom-ad.qcode")).findFirst().get().getValue();
        groupInformation.setCenterBottomAdQCode(bottomQcode);

        String consumeServiceHtml = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("my.consume.service.html")).findFirst().get().getValue();
        groupInformation.setConsumeServiceHtml(consumeServiceHtml);
        return groupInformation;
    }
}
