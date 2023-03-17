/**
 * 
 */
package me.gabelapingcao.bocchi.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.utils.ClassWalker;

/**
 * This implementation of {@link net.dv8tion.jda.api.hooks.IEventManager
 * IEventManager} is taken from the built-in
 * {@link net.dv8tion.jda.api.hooks.AnnotatedEventManager AnnotatedEventManager}
 * in the JDA. The only difference occurs on line 84, where the original JDA
 * implementation <i>did not</i> inherent all annotated methods from super classes.
 * 
 * @see net.dv8tion.jda.api.hooks.IEventManager IEventManager
 * @see net.dv8tion.jda.api.hooks.AnnotatedEventManager AnnotatedEventManager
 */
public class InheritedAnnotatedEventManager implements IEventManager {

	private final Set<Object> listeners = ConcurrentHashMap.newKeySet();
	private final Map<Class<?>, Map<Object, List<Method>>> methods = new ConcurrentHashMap<>();

	@Override
	public void register(Object listener) {
		if (listeners.add(listener)) {
			updateMethods();
		}
	}

	@Override
	public void unregister(Object listener) {
		if (listeners.remove(listener)) {
			updateMethods();
		}
	}

	@Override
	public List<Object> getRegisteredListeners() {
		return Collections.unmodifiableList(new ArrayList<>(listeners));
	}

	@Override
	public void handle(GenericEvent event) {
		for (Class<?> eventClass : ClassWalker.walk(event.getClass())) {
			Map<Object, List<Method>> listeners = methods.get(eventClass);
			if (listeners != null) {
				listeners.forEach((key, value) -> value.forEach(method -> {
					try {
						method.setAccessible(true);
						method.invoke(key, event);
					} catch (IllegalAccessException | InvocationTargetException e1) {
						JDAImpl.LOG.error("Couldn't access annotated EventListener method", e1);
					} catch (Throwable throwable) {
						JDAImpl.LOG.error("One of the EventListeners had an uncaught exception", throwable);
						if (throwable instanceof Error)
							throw (Error) throwable;
					}
				}));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void updateMethods() {
		methods.clear();
		for (Object listener : listeners) {
			boolean isClass = listener instanceof Class;
			Class<?> c = isClass ? (Class) listener : listener.getClass();
			Method[] allMethods = c.getMethods(); // This is the only line that is edited; c.getMethods() receives all
													// methods INCLUDING inherited methods
			for (Method m : allMethods) {
				if (!m.isAnnotationPresent(SubscribeEvent.class) || (isClass && !Modifier.isStatic(m.getModifiers()))) {
					continue;
				}
				Class<?>[] pType = m.getParameterTypes();
				if (pType.length == 1 && GenericEvent.class.isAssignableFrom(pType[0])) {
					Class<?> eventClass = pType[0];
					if (!methods.containsKey(eventClass)) {
						methods.put(eventClass, new ConcurrentHashMap<>());
					}

					if (!methods.get(eventClass).containsKey(listener)) {
						methods.get(eventClass).put(listener, new CopyOnWriteArrayList<>());
					}

					methods.get(eventClass).get(listener).add(m);
				}
			}
		}
	}

}
