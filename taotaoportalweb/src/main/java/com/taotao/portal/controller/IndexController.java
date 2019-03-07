package com.taotao.portal.controller;

import com.taotao.commons.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/*首页展示*/

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @Value(value = "${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;

    @Value(value = "${AD1_WIDTH}")
    private Integer AD1_WIDTH;

    @Value(value = "${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;

    @Value(value = "${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;

    @Value(value = "${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

    @RequestMapping("/index")
    public String showIndex(Model model){
        // 根据cid查询轮播图内容列表
        List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
        // 把列表转换为Ad1Node列表
        List<AD1Node> ad1Nodes = new ArrayList<>();
        for (TbContent content:contentList){
            AD1Node ad1Node = new AD1Node();
            ad1Node.setAlt(content.getTitle());
            ad1Node.setHeight(AD1_HEIGHT);
            ad1Node.setHeightB(AD1_HEIGHT_B);
            ad1Node.setWidth(AD1_WIDTH);
            ad1Node.setWidthB(AD1_WIDTH_B);
            ad1Node.setSrc(content.getPic());
            ad1Node.setSrcB(content.getPic2());
            ad1Node.setHref(content.getUrl());
            // 添加到节点列表
            ad1Nodes.add(ad1Node);
        }
        // 把列表转换为Jason数据
        String ad1Jason = JsonUtils.objectToJson(ad1Nodes);
        // 把Jason数据传递给页面
        model.addAttribute("ad1",ad1Jason);
        return "index";
    }
}
