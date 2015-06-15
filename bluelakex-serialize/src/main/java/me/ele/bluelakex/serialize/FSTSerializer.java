package me.ele.bluelakex.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;

public class FSTSerializer extends SerializeAdapter {
	
	private volatile static FSTSerializer instance;
	
	private FSTSerializer(){}
	
	public static FSTSerializer getSerializer(){
		if(instance == null){
			synchronized(FSTSerializer.class){
				if(instance == null){
					instance = new FSTSerializer();
				}
			}
		}
		return instance;
	}
	
	@Override
	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = null;
		FSTObjectOutput fout = null;
		try {
			out = new ByteArrayOutputStream();
			fout = new FSTObjectOutput(out);
			fout.writeObject(obj);
			return out.toByteArray();
		} finally {
			if(fout != null)
			try {
				fout.close();
			} catch (IOException e) {}
		}
	}

	@Override
	public Object deserialize(byte[] bytes) throws IOException {
		if(bytes == null || bytes.length == 0)
			return null;
		FSTObjectInput in = null;
		try {
			in = new FSTObjectInput(new ByteArrayInputStream(bytes));
			return in.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(in != null)
			try {
				in.close();
			} catch (IOException e) {}
		}
	}

	@Override
	public int getSerializeTypeId() {
		return 1;
	}
}
