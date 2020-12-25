package com.byakuya.boot.factory.netty;

import com.byakuya.boot.factory.config.property.NettyProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by ganzl on 2020/11/27.
 */
@Slf4j
@Component
public class NettyServer {

    public NettyServer(NettyProperties nettyProperties, DeviceChannelHandler deviceChannelHandler) {
        this.nettyProperties = nettyProperties;
        this.deviceChannelHandler = deviceChannelHandler;
    }

    @PreDestroy
    public void close() {
        log.info("关闭服务器....");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @PostConstruct
    public void run() {
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @SuppressWarnings("RedundantThrows")
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(deviceChannelHandler);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);


            ChannelFuture channelFuture = server.bind(nettyProperties.getPort()).sync();
            if (channelFuture.isSuccess()) {
                log.info("netty 服务器监听端口[{}]!", nettyProperties.getPort());
            }
//            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("netty 出错,释放资源!", e);
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private DeviceChannelHandler deviceChannelHandler;
    private NettyProperties nettyProperties;
}
