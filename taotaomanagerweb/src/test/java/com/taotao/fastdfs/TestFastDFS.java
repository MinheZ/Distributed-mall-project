package com.taotao.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;

public class TestFastDFS {
    @Test
    public void upLoadFile()throws Exception{
        // 1. 向工程中添加jar包
        // 2. 创建一个配置文件，配置tracker服务器地址
        // 3 加载配置文件
        ClientGlobal.init("E:\\ZMH\\Java\\Java EE\\TaoTaoShop\\taotaomanagerweb\\src\\main\\resources\\resource\\client.conf");
        // 4.创建一个TrackerClient对象获得trackerServer对象
        TrackerClient trackerClient = new TrackerClient();
        // 5.使用TrackerClient对象获得trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        // 6.创建一个StorageServer的引用,null就行
        StorageServer storageServer = null;
        // 7.创建一个StorageClient对象。trackerServer，StorageServer两个参数
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        // 8.使用StorageClient对象上传文件
        String[] strings = storageClient.upload_file("E:\\ZMH\\Java\\Java EE\\TaoTaoShop\\taotaomanagerweb\\src\\test\\testFile\\testFile.png",
                "png", null);
        for (String s:strings)
            System.out.println(s);

    }
}
