package com.zc.demo.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Name: BaseVerticle
 * @Author: Neil.Zhou
 * @Version: V1.00
 * @CreateDate: 2016/05/14
 * @Description: Definition of BaseVerticle for system
 */
public class BaseVerticle extends AbstractVerticle {
    private static final Logger log = LoggerFactory.getLogger(BaseVerticle.class);
    private final Object service;
    private String busAddress;

    public BaseVerticle(Object service,String eventBusAddress) {
        this.service=service;
        this.busAddress=eventBusAddress;
    }

    /**
     * 系统
     * @return
     */
    private Handler<Message<JsonObject>> msgHandler() {
        return msg -> {
            String m = msg.headers().get("method");
            JsonObject job = msg.body();
            try {
                msg.reply(service.getClass().getMethod(m, JsonObject.class).invoke(service, job));
            } catch (Exception e) {
                e.printStackTrace();
                JsonObject error=new JsonObject();
                error.put("code",500);
                error.put("msg","操作失败！");
                error.put("ob",e.getMessage());
                msg.reply(error);
            }
        };

    }
    /**
     * 注册事件驱动并
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        super.start();
        vertx.eventBus().<JsonObject>consumer(busAddress).handler(msgHandler());
    }
}
