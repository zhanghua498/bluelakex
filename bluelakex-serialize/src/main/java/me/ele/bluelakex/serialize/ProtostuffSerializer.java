package me.ele.bluelakex.serialize;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class ProtostuffSerializer implements Serializer {
	
	private volatile static ProtostuffSerializer instance;
	
	private ProtostuffSerializer(){}
	
	public static ProtostuffSerializer getSerializer(){
		if(instance == null){
			synchronized(ProtostuffSerializer.class){
				if(instance == null){
					instance = new ProtostuffSerializer();
				}
			}
		}
		return instance;
	}

	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }
        return schema;
    }
    

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> byte[] serialize(T obj) throws IOException {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {         
            if(obj instanceof List){
            	Schema<ProtosufferObj> schema = getSchema(ProtosufferObj.class);
            	ProtosufferObj pobj = new ProtosufferObj();
            	pobj.setList((List)obj);
            	return ProtostuffIOUtil.toByteArray(pobj, schema, buffer);
            }
            else if(obj instanceof Map){
            	Schema<ProtosufferObj> schema = getSchema(ProtosufferObj.class);
            	ProtosufferObj pobj = new ProtosufferObj();
            	pobj.setMap((Map)obj);
            	return ProtostuffIOUtil.toByteArray(pobj, schema, buffer);
            }
            else{
            	Schema<T> schema = getSchema(cls);
            	return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
	}

	@Override
	public <T> Object deserialize(byte[] bytes, Class<T> cls) throws IOException {
        try {
        	if(List.class.isAssignableFrom(cls)){
        		ProtosufferObj message = getProtosufferObj(bytes);
        		return message.getList();
        	}
        	else if( Map.class.isAssignableFrom(cls)){
        		ProtosufferObj message = getProtosufferObj(bytes);
        		return message.getMap();
        	}
        	else{
	            T message = (T) objenesis.newInstance(cls);
	            Schema<T> schema = getSchema(cls);
	            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
	            return message;
        	}
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

	private ProtosufferObj getProtosufferObj(byte[] bytes){
		ProtosufferObj message = new ProtosufferObj();
		Schema<ProtosufferObj> schema = getSchema(ProtosufferObj.class);
		ProtostuffIOUtil.mergeFrom(bytes, message, schema);
		return message;
	}


	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getSerializeTypeId() {
		return 4;
	}

}


