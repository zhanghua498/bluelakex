package me.ele.bluelakex.server.reflect;

public class MapMethodKey {
	private final Class<?> objectType;
	private final String methodName;
	private final Class<?>[] parameters;

	public MapMethodKey(Class<?> objectType, String methodName, Class<?>[] parameters) {
		this.objectType = objectType;
		this.methodName = methodName;
		this.parameters = parameters;
	}
	
	public MapMethodKey(Class<?> objectType, String methodName, Class<?> parameters) {
		this.objectType = objectType;
		this.methodName = methodName;
		this.parameters = new Class<?>[] {parameters};
	}
	
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		MapMethodKey that = (MapMethodKey) obj;
		if (!this.objectType.equals(that.objectType))
			return false;
		if (!this.methodName.equals(that.methodName))
			return false;
		if (this.parameters.length != that.parameters.length)
			return false;
		
		for (int i = 0; i < parameters.length; i++) {
			if(parameters[i]!=null&&that.parameters[i]!=null){
				if (!this.parameters[i].equals(that.parameters[i])) {
					return false;
				}
			}
		}
		
		return true;
	}

	public int hashCode() {
		int hashCode=0;
		for (int i = 0; i < parameters.length; i++) {
			if(parameters[i]!=null)
				hashCode += parameters[i].hashCode();
		}
		return objectType.hashCode() + methodName.hashCode() + hashCode;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(objectType);
		buffer.append("(");
		for (int i = 0; i < parameters.length; i++) {
			buffer.append(parameters[i].getName());
			buffer.append(",");
		}
		buffer.append(")");
		return buffer.toString();
	}
}
