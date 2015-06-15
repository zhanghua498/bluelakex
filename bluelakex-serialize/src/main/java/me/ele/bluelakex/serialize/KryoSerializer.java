package me.ele.bluelakex.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo序列化
 * @author zhanghua
 *
 */
public class KryoSerializer extends SerializeAdapter {

	private final static Kryo kryo = new Kryo();
	
	private volatile static KryoSerializer instance;
	
	private KryoSerializer(){}
	
	public static KryoSerializer getSerializer(){
		if(instance == null){
			synchronized(KryoSerializer.class){
				if(instance == null){
					instance = new KryoSerializer();
				}
			}
		}
		return instance;
	}

	@Override
	public byte[] serialize(Object obj) throws IOException {
		Output output = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			output = new Output(bos);
			kryo.writeClassAndObject(output, obj);
			output.flush();
			return bos.toByteArray();
		}finally{
			bos.close();
			if(output != null)
				output.close();
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		if(bytes == null || bytes.length == 0)
			return null;
		Input ois = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ois = new Input(bis);
			return kryo.readClassAndObject(ois);
		} finally {
			bis.close();
			if(ois != null)
				ois.close();
		}
	}
	
	@Override
	public int getSerializeTypeId() {
		return 2;
	}
	
}
