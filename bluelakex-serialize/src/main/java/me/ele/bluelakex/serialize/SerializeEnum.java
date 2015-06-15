package me.ele.bluelakex.serialize;

public enum SerializeEnum {
	FST(FSTSerializer.getSerializer()),
	JDK(JDKSerializer.getSerializer()),
	Kryo(KryoSerializer.getSerializer()),
	Protostuff(ProtostuffSerializer.getSerializer());
	
	private Serializer serializer;
	
	private SerializeEnum(Serializer serializer){
		this.serializer = serializer;
	}
	
	public Serializer getSerializer(){
		return this.serializer;
	}
}
