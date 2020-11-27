package com.byakuya.boot.factory.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

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
            ByteBuf byteBuf = (ByteBuf) msg;
            log.info(byteBuf.toString(StandardCharsets.UTF_8));
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
