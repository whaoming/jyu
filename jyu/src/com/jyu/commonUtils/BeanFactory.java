package com.jyu.commonUtils;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import android.content.Context;

import com.jyu.GlobalParams;

/**
 * 工厂类
 * 
 * 
 */
public class BeanFactory {
	private static final Properties properties;

	private BeanFactory() {

	}

	private static BeanFactory instance = new BeanFactory();

	public static BeanFactory getInstance() {
		return instance;
	}

	// 静态加载配置文件
	static {
		properties = new Properties();
		try {
			InputStream is = BeanFactory.class.getClassLoader()
					.getResourceAsStream("config.properties");
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得业务类的实例对象
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getImpl(Class<T> clazz) {
		try {
			String simpleName = clazz.getSimpleName();
			String className = properties.getProperty(simpleName);
			return (T) Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getImplByContext(Class<T> targetClazz) {
		Constructor<T> constructor;// 定义一个构造器对象
		try {
			String simpleName = targetClazz.getSimpleName();// 传入的字节码文件的类名
			String className = properties.getProperty(simpleName);// 根据接口的类名从配置文件中读取实现类的全类名
			// 根据实现类的群类名获取实现类的字节码文件，在根据字节码文件获取带上下文参数构造方法。
			constructor = (Constructor<T>) Class.forName(className)
					.getConstructor(Context.class);
			// 根据上面带上下文参数的构造方法，获取实现类对象
			return (T) constructor.newInstance(GlobalParams.CONTEXT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
