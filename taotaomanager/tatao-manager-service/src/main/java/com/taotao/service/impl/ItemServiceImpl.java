package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper descMapper;

    @Override
    public TbItem getItemById(long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int row) {
        // 设置分页信息
        PageHelper.startPage(page, row);
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
        // 取查询结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(tbItems);
        result.setTotal(pageInfo.getTotal());
        // 返回结果
        return result;
    }

    @Override
    public TaotaoResult addItem(TbItem tbItem, String desc) {
        // 生成商品ID
        long itemId = IDUtils.genItemId();
        // 补全Item的属性
        tbItem.setId(itemId);
        // 商品状态，1-正常，2-下架，3-删除
        tbItem.setStatus((byte) 1);
        // 上传时间
        tbItem.setCreated(new Date());
        // 修改时间
        tbItem.setUpdated(new Date());

       /*
       乱码测试：最后发现是向数据库写入中文乱码
        try {
            tbItem.setTitle(new String(tbItem.getTitle().getBytes(),"utf-8"));
            System.out.println(tbItem.getTitle());
        }catch (Exception e){
            e.printStackTrace();
        }
        最后发现数据库连接配置文件字符集单词写错了==！
        */

        // 向商品表插入数据
        itemMapper.insert(tbItem);
        // 创建一个商品描述表对应的pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全pojo的属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        // 向商品描述表插入数据
        descMapper.insert(itemDesc);
        // 返回结果

        return TaotaoResult.ok();
    }
}
