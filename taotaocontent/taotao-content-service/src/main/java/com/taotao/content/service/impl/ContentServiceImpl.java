package com.taotao.content.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.commons.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    @Override
    public TaotaoResult addContent(TbContent content) {
        // 补全pojo信息
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);

        // 同步缓存
        jedisClient.hdel(INDEX_CONTENT,content.getCategoryId().toString());

        return TaotaoResult.ok();
    }

    @Override
    public EasyUIDataGridResult getContentList(int page, int row) {
        // 设置分页信息
        PageHelper.startPage(page,row);
        TbContentExample contentExample = new TbContentExample();
        List<TbContent> contentList = contentMapper.selectByExample(contentExample);
        // 取出查询结果
        PageInfo<TbContent> pageInfo = new PageInfo<>(contentList);
        EasyUIDataGridResult result = new EasyUIDataGridResult();

        result.setRows(contentList);
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    /*删除*/
    @Override
    public TaotaoResult deleteContent(String ids) {
        //TbContent content = contentMapper.selectByPrimaryKey(id);
        // 将字符串数组中的“，”切掉
        String[] splits = ids.split(",");
        // 字符串数组转换成List<Long>
        List<Long> list = new ArrayList<>();
        for (String s:splits){
            list.add(Long.parseLong(s));
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        // 根据list中的id批量查找
        criteria.andIdIn(list);
        contentMapper.deleteByExample(example);

        /*这里应该也要添加缓存同步，但是不知道为什么根据id没法查询出数据库内容*/

//        // 同步缓存
//        for (String id:splits){
//            TbContentExample example1 = new TbContentExample();
//            TbContentExample.Criteria criteria1 = example1.createCriteria();
//            criteria1.andIdEqualTo(Long.parseLong(id));
//
//            List<TbContent> content =  contentMapper.selectByExample(example1);
//            for (TbContent c:content){
//                jedisClient.hdel(INDEX_CONTENT,c.getCategoryId().toString());
//            }

//        }


        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult editContent(TbContent data) {
//        TbContent content = contentMapper.selectByPrimaryKey(data.getId());
//
//        content.setUpdated(new Date());
        data.setUpdated(new Date());
        //data.setContent(data.getContent());
        //System.out.println(data.getContent());
        contentMapper.updateByPrimaryKey(data);
        // 同步缓存
        jedisClient.hdel(INDEX_CONTENT,data.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    @Override
    public List<TbContent> getContentByCid(long cid) {
        // 先查询缓存，添加缓存不能影响正常业务逻辑
        try {
            // 查询缓存
            String json = jedisClient.hget(INDEX_CONTENT,cid + "");
            // 查询到结果，把Jason转换为List返回
            if (StringUtils.isNotBlank(json)){
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        // 缓存中没有命中，需要查询数据库
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        // 设置查询条件
        criteria.andCategoryIdEqualTo(cid);
        // 执行查询
        List<TbContent> list = contentMapper.selectByExample(example);

        // 把结果添加到缓存
        try {
            jedisClient.hset("INDEX_CONTENT",cid + "", JsonUtils.objectToJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }

        // 返回结果
        return list;
    }
}
