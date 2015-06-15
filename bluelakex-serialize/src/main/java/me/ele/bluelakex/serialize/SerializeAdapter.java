package me.ele.bluelakex.serialize;

import java.io.IOException;

public abstract class SerializeAdapter implements Serializer {

	@Override
	public abstract <T> byte[] serialize(T obj) throws IOException ;

	@Override
	public abstract Object deserialize(byte[] bytes) throws IOException ;

	@SuppressWarnings("unchecked")
	@Override
	public <T> T deserialize(byte[] bytes, Class<T> cls) throws IOException {
		Object obj = deserialize(bytes);
		if(obj != null){
			return (T) obj;
		}
		return null;
	}

}
