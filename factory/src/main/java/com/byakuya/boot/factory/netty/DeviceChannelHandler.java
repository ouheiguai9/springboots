package com.byakuya.boot.factory.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ganzl on 2020/11/27.
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class DeviceChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            log.warn("接收到状态:{}", msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
