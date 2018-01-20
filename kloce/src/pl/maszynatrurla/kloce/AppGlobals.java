package pl.maszynatrurla.kloce;

import java.util.HashMap;
import java.util.Map;

public final class AppGlobals 
{
	private static final AppGlobals INSTANCE = new AppGlobals();
	
	private final Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();
	
	private AppGlobals() {}
	
	public static AppGlobals getInstance()
	{
		return INSTANCE;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz)
	{
		return (T) context.get(clazz);
	}
	
	public <T extends Object> void set(T object)
	{
		context.put(object.getClass(), object);
	}
	
	public <T> void set(Class<T> clazz, T object)
	{
		context.put(clazz, object);
	}
}
