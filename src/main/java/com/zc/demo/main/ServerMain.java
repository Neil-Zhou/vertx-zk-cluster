package com.zc.demo.main;

import com.zc.demo.service.Service1;
import com.zc.demo.utils.NetworkUtil;
import com.zc.demo.verticle.BaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miaoch on 2016/1/6.
 */
public class ServerMain {
    private static Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        ClusterManager mgr = new ZookeeperClusterManager();
        //mgr.setVertx(vertx);
        VertxOptions options = new VertxOptions().setClusterManager(mgr).setClusterHost(NetworkUtil.getInterface());
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                logger.debug("-------------------start deploy clustered event bus------");
                res.result().deployVerticle(new BaseVerticle(new Service1(),"vertx.cluster.replyHello"),new DeploymentOptions());
            } else {
                logger.debug("Failed: " + res.cause());
            }
        });

    }
}
