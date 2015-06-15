package me.ele.bluelakex.serialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializeInstanceHolder {
	private Map<Integer,Serializer> map = new HashMap<Integer,Serializer>();
	
	public SerializeInstanceHolder(List<Serializer> serializerList){
		for(Serializer s:serializerList){
			map.put(s.getSerializeTypeId(), s);
		}
	}
	
	public Serializer getSerializerByTypeId(Integer typeId){
		return map.get(typeId);
	}
}
