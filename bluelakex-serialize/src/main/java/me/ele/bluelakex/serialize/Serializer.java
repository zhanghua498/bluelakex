package me.ele.bluelakex.serialize;

import java.io.IOException;

public interface Serializer {

	public <T> byte[] serialize(T obj) throws IOException ;
	
	public Object deserialize(byte[] bytes) throws IOException ;
	
	public <T> Object deserialize(byte[] bytes,Class<T> cls) throws IOException ;
	
	public int getSerializeTypeId();
	
}
