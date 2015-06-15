package me.ele.bluelakex.serialize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class JDKSerializer extends SerializeAdapter  {

	private volatile static JDKSerializer instance;
	
	private JDKSerializer(){}
	
	public static JDKSerializer getSerializer(){
		if(instance == null){
			synchronized(JDKSerializer.class){
				if(instance == null){
					instance = new JDKSerializer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public byte[] serialize(Object obj) throws IOException {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(bos));
			oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
			
		} finally {
			try {
				bos.close();
				if(oos != null) oos.close();
			} catch (IOException e) {}
		}
	}


	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		if(bytes == null || bytes.length == 0)
			return null;
		ObjectInputStream ois = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		try {
			ois = new ObjectInputStream(new BufferedInputStream(bis));
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				bis.close();
				if(ois != null) ois.close();
			} catch (IOException e) {}
		}
	}
	
	@Override
	public int getSerializeTypeId() {
		return 0;
	}
	
}
