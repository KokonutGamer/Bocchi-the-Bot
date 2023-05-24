package me.gabelapingcao.bocchi.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * 
 * 
 * @see <a href=
 *      "https://www.baeldung.com/java-find-all-classes-in-package">Finding All
 *      Classes in a Java Package</a>
 *
 */
public class ClassLoaderHelper {

	private final Logger log = (Logger) LoggerFactory.getLogger(ClassLoaderHelper.class);

	private ClassLoaderHelper() {
	}

	private static class SingletonHelper {
		private static final ClassLoaderHelper INSTANCE = new ClassLoaderHelper();
	}

	public static ClassLoaderHelper getInstance() {
		return SingletonHelper.INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public <T> Set<T> newInstanceOfAllClasses(Class<T> clazz, String packageName) {
		Set<Class<?>> setOfClassObjects = findAllClasses(packageName);
		Set<T> newInstances = new CopyOnWriteArraySet<T>();
		try {
			for (Class<?> classObject : setOfClassObjects) {
				if (classObject.isAssignableFrom(clazz)) {
					Constructor<?> constructor = classObject.getConstructor();
					T newInstance = (T) constructor.newInstance();
					newInstances.add(newInstance);
				}
			}
		} catch (NoSuchMethodException e) {
			log.error(
					"Cannot invoke a new instance of this object because a matching constructor of zero parameters was not found");
		} catch (SecurityException e) {
			log.error("Cannot invoke the constructor of this object because the class is not visible from this scope");
		} catch (InvocationTargetException e) {
			log.error("The constructor of this object threw an error");
		} catch (IllegalAccessException e) {
			log.error(
					"IllegalAccessException: cannot invoke a new instance of this object because the class is not visible within this scope");
		} catch (IllegalArgumentException e) {
			log.error(
					"IllegalArgumentException: cannot invoke a new instance of this object because the number of arguments specified differs for this constructor");
		} catch (InstantiationException e) {
			log.error("Cannot instantiate an abstract class");
		} catch (ExceptionInInitializerError e) {
			log.error("The initialization provoked by this method failed");
		} catch (Exception e) {
			log.error("An unknown error occured");
			log.error(e.toString());
		}
		return newInstances;
	}

	public Set<Class<?>> findAllClasses(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class")).map(line -> getClass(line, packageName))
				.collect(Collectors.toSet());
	}

	private Class<?> getClass(String className, String packageName) {
		try {
			return Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			log.error("Cannot load class " + className + " within the package " + packageName);
		}
		return null;
	}

}
