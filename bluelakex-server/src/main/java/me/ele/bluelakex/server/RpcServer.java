package me.ele.bluelakex.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;

import me.ele.bluelakex.model.RequestEntity;
import me.ele.bluelakex.model.ResponseEntity;
import me.ele.bluelakex.netty.RpcDecoder;
import me.ele.bluelakex.netty.RpcEncoder;
import me.ele.bluelakex.serialize.Serializer;
import me.ele.bluelakex.server.annotation.RpcService;
import me.ele.bluelakex.server.netty.ServerHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcServer implements ApplicationContextAware, InitializingBean{
	
	private static final Logger log = LoggerFactory.getLogger(RpcServer.class);
	
	private Map<String, Object> serviceInstanceMap = new HashMap<String, Object>(); 
	
	private int port;
	private final Serializer serializer;
	
	public RpcServer(int port,Serializer serializer){
		this.port = port;
		this.serializer = serializer;
	}

	public void afterPropertiesSet() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                            .addLast(new RpcDecoder(RequestEntity.class, serializer))
                            .addLast(new RpcEncoder(ResponseEntity.class,serializer))
                            .addLast(new ServerHandler(serviceInstanceMap));
                    }
                });

            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("RpcServer started on port {}", port);
            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
		
	}

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		Map<String, Object> beanMap = ctx.getBeansWithAnnotation(RpcService.class);
		for (Object instanceBean : beanMap.values()) {
            String interfaceName = instanceBean.getClass().getAnnotation(RpcService.class).value().getName();
            serviceInstanceMap.put(interfaceName, instanceBean);
        }		
	}

}
