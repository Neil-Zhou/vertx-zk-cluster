package com.zc.demo.main;

import com.zc.demo.service.Service1;
import com.zc.demo.utils.NetworkUtil;
import com.zc.demo.verticle.BaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2016/12/29.
 */
public class ClientMain {
    private static Logger logger = LoggerFactory.getLogger(ClientMain.class);
    private static Vertx ClusterVertx=null;
    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        ClusterManager mgr = new ZookeeperClusterManager();
        //mgr.setVertx(vertx);
        VertxOptions options = new VertxOptions().setClusterManager(mgr).setClusterHost(NetworkUtil.getInterface());
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                ClusterVertx=res.result();
                logger.debug("-------------------start deploy clustered event bus------");
                ClusterVertx.setPeriodic(2 * 1000,callBack->{
                    ClusterVertx.eventBus().<JsonObject>send("vertx.cluster.replyHello",new JsonObject(){{put("input","向你打招呼...");}},new DeliveryOptions().addHeader("method","replyHello").setSendTimeout(60000), resultBody -> {
                        if (resultBody.failed()) {
                            logger.error("Fail for the process!");
                            return;
                        }
                        System.out.println("服务端响应结果："+ resultBody.result().body().encode());
                    });
                });
            } else {
                logger.debug("Failed: " + res.cause());
            }
        });

    }
}
