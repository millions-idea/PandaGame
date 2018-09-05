/***
 * @pName management
 * @name ConfigController
 * @user HongWei
 * @date 2018/8/31
 * @desc
 */
package com.panda.game.management.controller;

import com.panda.game.management.biz.IDictionaryService;
import com.panda.game.management.entity.DataDictionary;
import com.panda.game.management.entity.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/management/config")
public class ConfigController extends BaseController {
    @Autowired
    private IDictionaryService dictionaryService;

    @GetMapping("/pay")
    public String pay(final Model model){
        String qrcode = "/images/upload/" + DataDictionary.DATA_DICTIONARY.get("finance.pays.qrcode.image").getValue();
        if(qrcode == null || qrcode.equalsIgnoreCase("#")) qrcode = "/images/qcode_notFound.png";
        model.addAttribute("qrcode", qrcode);
        return "config/pay";
    }

    @GetMapping("/updateQRCode")
    @ResponseBody
    public JsonResult updateQRCode(String url){
        dictionaryService.updateQRCode(url);
        dictionaryService.refresh();
        return JsonResult.successful();
    }

    @GetMapping("/index")
    public String configuration(final Model model){
        model.addAttribute("consumeServiceHtml", dictionaryService.getGroupInformation().getConsumeServiceHtml());
        model.addAttribute("newUserGive", dictionaryService.getGroupInformation().getGiveAmount());
        model.addAttribute("version", dictionaryService.getGroupInformation().getVersion());
        model.addAttribute("ios", dictionaryService.getGroupInformation().getIosDownload());
        model.addAttribute("android", dictionaryService.getGroupInformation().getAndroidDownload());
        return "config/index";
    }

    @PostMapping("/updateConfiguration")
    @ResponseBody
    public JsonResult updateConfiguration(String html, String giveAmount, String version, String iosDownload, String androidDownload){
        dictionaryService.updateConfiguration(html, giveAmount, version, iosDownload, androidDownload);
        dictionaryService.refresh();
        return JsonResult.successful();
    }
}
