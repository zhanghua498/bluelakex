package me.ele.bluelakex.server.netty;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ele.bluelakex.model.RequestEntity;
import me.ele.bluelakex.model.ResponseEntity;
import me.ele.bluelakex.server.reflect.RpcServiceInvoker;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler<RequestEntity>{
	
	private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

	private final Map<String, Object> serviceInstanceMap;
	
	public ServerHandler(Map<String, Object> serviceInstanceMap) {
		this.serviceInstanceMap = serviceInstanceMap;
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestEntity request)
			throws Exception {
		
		String className = request.getClassName();
		Object serviceInstance = serviceInstanceMap.get(className);
		ResponseEntity response = new ResponseEntity();
		try {
			Object result = RpcServiceInvoker.invoke(request, serviceInstance);
			response.setResult(result);
		} catch (Throwable e) {
			log.error("RpcService caught exception", e);
			response.setError(e);
		}
		response.setRequestId(request.getRequestId());
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("RpcServer caught exception", cause);
		ctx.close();
	}
}
