package com.taotao.content.service.impl;

import com.taotao.commons.pojo.EasyUITreeNode;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*内容分类管理Service*/

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /*显示内容分类列表*/
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据parentId查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到结果列表
            resultList.add(node);
        }
        return resultList;
    }
    /*添加内容分类测试*/
    @Override
    public TaotaoResult addContentCategory(Long parentId, String name) {
        // 创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        // 补全对象的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        // 状态，可选值：1（正常）2（删除）
        contentCategory.setStatus(1);
        // 排序，默认为1
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        // 插入到数据库
        contentCategoryMapper.insert(contentCategory);
        // 判断父节点的状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            // 如果父节点不是根节点，则设置为根节点
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        // 返回结果

        return TaotaoResult.ok();
    }
    /*内容分类重命名*/
    @Override
        public TaotaoResult updateContentCategory(Long id, String name) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategory.setUpdated(new Date());
        //contentCategoryMapper.updateByExample(contentCategory);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return TaotaoResult.ok();
    }

    /*递归删除节点下的所有节点*/
    @Override
    public TaotaoResult deleteContentCategory(Long id) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        // 如果id是根节点，则递归删除所有子节点
        if (contentCategory.getIsParent()){
            TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = contentCategoryExample.createCriteria();
            criteria.andParentIdEqualTo(contentCategory.getId());
            List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(contentCategoryExample);
            for(TbContentCategory category:contentCategoryList){
                deleteContentCategory(category.getId());
                //contentCategoryMapper.deleteByPrimaryKey(category.getId());
            }

        }else
            contentCategoryMapper.deleteByPrimaryKey(id);
        return TaotaoResult.ok();
    }
}
