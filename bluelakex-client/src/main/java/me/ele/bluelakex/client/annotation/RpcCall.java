package me.ele.bluelakex.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.ele.bluelakex.serialize.SerializeEnum;
import me.ele.bluelakex.loadbalance.LoadBalanceEnum;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcCall {
	/**
	 * 远程服务地址
	 */
	String server() default "";
	/**
	 * ZK服务(集群)地址
	 * ZK服务地址与远程服务地址两者必须设置一个
	 * 如两个都设置则使用ZK服务(集群)地址
	 */
	String zkServer() default "";
	/**
	 * 序列化方式
	 */
	SerializeEnum serializer() default SerializeEnum.Protostuff;
	/**
	 *  负载均衡策略
	 */
	LoadBalanceEnum loadbalance() default LoadBalanceEnum.Random;
	/**
	 * 是否使用连接池
	 */
	boolean isPool() default false;
	/**
	 * 是否启用压缩
	 */
	boolean compress() default false;
}
