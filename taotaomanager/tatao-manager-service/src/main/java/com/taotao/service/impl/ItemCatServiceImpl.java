package com.taotao.service.impl;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        // 根据父节点的id查询子节点列表
        TbItemCatExample catExample = new TbItemCatExample();
        // 设置查询条件
        TbItemCatExample.Criteria criteria = catExample.createCriteria();
        // 设置parentId
        criteria.andParentIdEqualTo(parentId);
        // 执行查询
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(catExample);
        // 转换成EasyUITreeNode
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats){
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            // 如果节点下有子节点“closed”，如果没有子节点open
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");
            // 添加到节点列表
            resultList.add(node);
        }
        return resultList;
    }
}
