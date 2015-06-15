package me.ele.bluelakex.client;

//import me.ele.bluelakex.client.callback.Callback;
//import me.ele.bluelakex.client.callback.CallbackFuture;
import me.ele.bluelakex.model.RequestEntity;
import me.ele.bluelakex.model.ResponseEntity;
import me.ele.bluelakex.netty.RpcDecoder;
import me.ele.bluelakex.netty.RpcEncoder;
import me.ele.bluelakex.serialize.Serializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.util.AttributeKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient extends SimpleChannelInboundHandler<ResponseEntity>{

	private static final Logger Logger = LoggerFactory.getLogger(RpcClient.class);
	
//	public static AttributeKey<Callback> callbackKey = AttributeKey.valueOf("callback"); 
	
	private String host;

	private int port;

	private ResponseEntity response;
	
	private Serializer serializer;

	private final Object obj = new Object();

	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public RpcClient(String host, int port,Serializer serializer) {
		this.host = host;
		this.port = port;
		this.serializer = serializer;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ResponseEntity response) throws Exception {
		this.response = response;
//		Channel channel = ctx.channel();
//		Callback callback = channel.attr(callbackKey).get(); 
//		if(callback == null)
			sync();
//		else
//			async(callback,response);
	}
	
	private void sync(){
		synchronized (obj) {
			obj.notifyAll();
		}
	}
	
//	private void async(Callback callback, ResponseEntity response){
//		callback.call(response.getResult());
//	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Logger.error("client caught exception", cause);
		ctx.close();
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

	public ResponseEntity send(RequestEntity request) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline()
					.addLast(new RpcEncoder(RequestEntity.class,serializer))
					.addLast(new RpcDecoder(ResponseEntity.class,serializer))
					.addLast(RpcClient.this);
				}
			});

			ChannelFuture future = bootstrap.connect(host, port).sync();
			Channel channel = future.channel();
			channel.writeAndFlush(request).sync();
//			if(async){
//				final CallbackFuture<Object> callFuture = new CallbackFuture<Object>();
//				channel.attr(callbackKey).set(new Callback(){
//					@Override
//					public void call(Object value) {
//						callFuture.done(value);	
//					}
//				});
//				response.setResult(callFuture);
//			}
//			else{
				synchronized (obj) {
					obj.wait();
				}
//			}
			if (response != null) {
				future.channel().closeFuture().sync();
			}
			return response;
		} finally {
			group.shutdownGracefully();
		}
	}

}
