package com.zc.demo.service;

import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * @File：Service1
 * @Version：1.0.0
 * @Author：Neil.Zhou
 * @CreateDate：2016/8/23 15:01
 * @Modify：
 * @ModifyDate：2016/8/23
 * @Descriot：维保建筑物联动统计
 * </pre>
 */
public class Service1 {
    private static Logger logger = LoggerFactory.getLogger(Service1.class);

    public JsonObject replyHello(JsonObject params) {
       System.out.println("接收客户端消息："+params.encode());
        JsonObject result=new JsonObject("{\"code\":200,\"msg\":\"ok\"}");
        result.put("remark","[回复]客户端你好!");
        return result;
    }
}
