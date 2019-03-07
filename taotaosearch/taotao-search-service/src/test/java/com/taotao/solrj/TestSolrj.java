package com.taotao.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class TestSolrj {
    @Test
    public void testAddDocument() throws IOException, SolrServerException {
        // 创建一个SoleServer对象，创建一个HTTPSolrServer对象

        // 需要指定solr服务的url
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.201:8080/solr");
        // 创建一个文档对象SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        // 向文档中添加域，必须有id域，域的名称必须在schema.xml中定义
        document.addField("id", "test001");
        document.addField("item_title", "test1");
        document.addField("item_price", 1000);
        // 把稳定写入索引
        solrServer.add(document);
        // 提交
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.1.201:8080/solr");
        solrServer.deleteById("test001");
        solrServer.commit();
    }
}
