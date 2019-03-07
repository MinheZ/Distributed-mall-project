package com.taotao.controller;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

/*内容分类管理Controller*/

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    /*展示所有的节点*/
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(
            @RequestParam(value="id", defaultValue="0")Long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    /*新添加节点*/
    @RequestMapping("/content/category/create")
    @ResponseBody
    public TaotaoResult addContentCategory(Long parentId, String name){
        return contentCategoryService.addContentCategory(parentId,name);
    }

    /*重命名内容分类列表*/
    @RequestMapping("/content/category/update")
    @ResponseBody
    public TaotaoResult updateContentCategory(Long id, String name){
        return contentCategoryService.updateContentCategory(id,name);
    }

    /*递归删除节点下的所有节点*/
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public TaotaoResult deleteContentCategory(Long id){
        contentCategoryService.deleteContentCategory(id);
        return TaotaoResult.ok();
    }

}
