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
import com.panda.game.management.entity.DynamicConfiguration;
import com.panda.game.management.entity.db.Dictionary;
import com.panda.game.management.entity.resp.GroupInformation;
import com.panda.game.management.exception.InfoException;
import com.panda.game.management.exception.MsgException;
import com.panda.game.management.repository.DictionaryMapper;
import com.panda.game.management.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DictionaryServiceImpl extends BaseServiceImpl<Dictionary> implements IDictionaryService {
    private final DictionaryMapper dictionaryMapper;
    @Autowired
    private DynamicConfiguration dynamicConfiguration;

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

        String payQCodeUrl = dynamicConfiguration.getDomain() + "/images/upload/" + DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("finance.pays.qrcode.image")).findFirst().get().getValue();
        groupInformation.setPayQRCodeUrl(payQCodeUrl);

        String payCode = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("finance.pays.qrcode.payCode")).findFirst().get().getValue();
        groupInformation.setPayCode(payCode);

        String giveAmount = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("finance.give.amount")).findFirst().get().getValue();
        groupInformation.setGiveAmount(giveAmount);

        String version = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("version")).findFirst().get().getValue();
        groupInformation.setVersion(version);

        String iosDownload = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("ios.download")).findFirst().get().getValue();
        groupInformation.setIosDownload(iosDownload);

        String androidDownload = DataDictionary.DATA_DICTIONARY.values().stream().filter(d -> d.getKey().contains("android.download")).findFirst().get().getValue();
        groupInformation.setAndroidDownload(androidDownload);
        return groupInformation;
    }

    /**
     * 上传二维码 韦德 2018年8月31日13:31:29
     *
     * @param url
     */
    @Override
    @Transactional
    public void updateQRCode(String url) {
        Dictionary dictionary = DataDictionary.DATA_DICTIONARY.get("finance.pays.qrcode.image");
        int count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), url);
        if(count == 0) throw new MsgException("更新二维码失败");

        dictionary = DataDictionary.DATA_DICTIONARY.get("finance.pays.qrcode.payCode");
        String absPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
        String path = absPath.substring(1, absPath.length()) + "static/images/upload/" + url;
        String payCode = null;
        try {
            payCode = QRCodeUtil.decode(path);
        } catch (Exception e) {
            System.err.println(e);
            throw new InfoException("解析二维码失败");
        }
        payCode = payCode.substring(22, payCode.length());
        count = 0;
        count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), payCode);
        if(count == 0) throw new MsgException("更新二维条码失败");
    }

    /**
     * 刷新本地字典 韦德 2018年8月31日13:46:02
     */
    @Override
    public void refresh() {
        List<Dictionary> list = this.getList();
        if(list != null  && !list.isEmpty()){
            List<Dictionary> dictionaryList = list;
            DataDictionary.DATA_DICTIONARY.clear();
            Map<String,Dictionary> dataDictionaryList = new HashMap<>();
            dictionaryList.forEach(dictionary -> dataDictionaryList.put(dictionary.getKey() , dictionary));
            DataDictionary.DATA_DICTIONARY = dataDictionaryList;
        }
    }

    /**
     * 更新联系方式 韦德 2018年9月2日01:44:08
     *
     * @param html
     */
    @Override
    @Transactional
    public void updateConfiguration(String html, String giveAmount, String version, String iosDownload, String androidDownload) {
        Dictionary dictionary = DataDictionary.DATA_DICTIONARY.get("my.consume.service.html");
        int count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), html);
        if(count == 0) throw new MsgException("更新失败[A01]");

        dictionary = DataDictionary.DATA_DICTIONARY.get("finance.give.amount");
        count = 0;
        count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), giveAmount);
        if(count == 0) throw new MsgException("更新失败[A02]");

        dictionary = DataDictionary.DATA_DICTIONARY.get("version");
        count = 0;
        count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), version);
        if(count == 0) throw new MsgException("更新失败[A03]");

        dictionary = DataDictionary.DATA_DICTIONARY.get("ios.download");
        count = 0;
        count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), iosDownload);
        if(count == 0) throw new MsgException("更新失败[A04]");

        dictionary = DataDictionary.DATA_DICTIONARY.get("android.download");
        count = 0;
        count = dictionaryMapper.updateUrlById(dictionary.getDictionaryId(), androidDownload);
        if(count == 0) throw new MsgException("更新失败[A05]");
    }
}
