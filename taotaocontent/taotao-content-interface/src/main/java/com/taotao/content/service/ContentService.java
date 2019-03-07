package com.taotao.content.service;

import com.taotao.commons.pojo.EasyUIDataGridResult;
import com.taotao.commons.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

import java.util.List;


public interface ContentService {

    TaotaoResult addContent(TbContent content);

    EasyUIDataGridResult getContentList(int page, int row);

    TaotaoResult deleteContent(String id);

    TaotaoResult editContent(TbContent data);

    List<TbContent> getContentByCid(long cid);

}
