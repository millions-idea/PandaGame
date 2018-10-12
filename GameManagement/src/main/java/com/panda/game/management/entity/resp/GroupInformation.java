/***
 * @pName management
 * @name tGroupInformation
 * @user HongWei
 * @date 2018/8/18
 * @desc
 */
package com.panda.game.management.entity.resp;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GroupInformation {
    private List<String> topRunnerAds;
    private String topTextAd;
    private String centerLeftAdTitle;
    private String centerLeftAdDesc;
    private String centerRightAdTitle;
    private String centerRightAdDesc;
    private String centerRightAdButton;
    private String centerBottomAdTitle;
    private String centerBottomAdDesc;
    private String centerBottomAdQCode;
    private String consumeServiceHtml;
    private String payQRCodeUrl;
    private String payCode;
    private String giveAmount;
    private String version;
    private String iosDownload;
    private String androidDownload;
    private String regPackagePrice;
    private String playAwardPrice;
    private String joinCount;
}
