package com.taotao.pageHelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {
    @Test
    public void testPageHelper(){
        // 1. 在MyBatis的配置文件中配置分页插件
        // 2. 在执行查询之前配置分页条件。使用PageHelper的静态方法
        PageHelper.startPage(1,10);
        // 3. 执行查询
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/spring/ApplicationContext-dao.xml");
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        //创建example对象
        TbItemExample tbItemExample = new TbItemExample();
//        TbItemExample.Criteria criteria = tbItemExample.createCriteria();
//        criteria.andBarcodeBetween();
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);
        // 4. 取分页信息，使用PageInfo获取
        PageInfo<TbItem> pageInfo = new PageInfo<>(tbItems);
        System.out.println("查询总记录数：" + pageInfo.getTotal());
        System.out.println("查询总页数" + pageInfo.getPages());
        System.out.println("返回总记录数:" + tbItems.size());
    }

}
