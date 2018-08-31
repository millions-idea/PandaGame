/***
 * @pName management
 * @name NettyBootstrap
 * @user HongWei
 * @date 2018/8/22
 * @desc
 */
package com.panda.game.management;

import com.panda.game.management.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class NettyBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null){
            try{
                WSServer.getInstance().start();
            }catch (Exception e){
                System.err.println("Netty start failing!");
                System.err.println(e);
            }
        }
    }
}
