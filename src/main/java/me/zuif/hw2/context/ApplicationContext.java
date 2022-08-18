package me.zuif.hw2.context;

import lombok.Getter;
import me.zuif.hw2.annotations.Autowired;
import me.zuif.hw2.annotations.Singleton;
import me.zuif.hw2.repository.PenRepository;
import me.zuif.hw2.repository.PhoneRepository;
import me.zuif.hw2.repository.TeaRepository;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationContext {
    private static ApplicationContext instance;
    private final Reflections reflections;
    @Getter
    private final Map<Class<?>, Object> cache;

    private ApplicationContext() {
        cache = new LinkedHashMap<>();
        reflections = new Reflections("me.zuif.hw2");
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public void setCache() {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Singleton.class);
        addRepositories(classSet);
        addServices(classSet);
    }

    private void addRepositories(Set<Class<?>> classes) {
        classes.stream().forEach(aClass -> {
            Arrays.stream(aClass.getDeclaredConstructors())
                    .forEach(constructor -> {
                        if (!(constructor.isAnnotationPresent(Autowired.class) && constructor.getParameterCount() == 0)) {
                            return;
                        }
                        try {
                            constructor.setAccessible(true);
                            Object object = constructor.newInstance();

                            Field field = aClass.getDeclaredField("instance");
                            field.setAccessible(true);
                            field.set(null, object);
                            cache.put(aClass, object);

                        } catch (InvocationTargetException | InstantiationException
                                 | IllegalAccessException | NoSuchFieldException e) {
                            System.out.println("Exception in constructor " + constructor.getName());
                            e.printStackTrace();
                        }

                    });
        });
    }

    private void addServices(Set<Class<?>> classes) {
        classes.forEach(aClass -> {
            Arrays.stream(aClass.getDeclaredConstructors())
                    .forEach(constructor -> {
                        if (!(constructor.isAnnotationPresent(Autowired.class)
                                && constructor.getParameterCount() == 1)
                        ) {
                            return;
                        }
                        Object repository = null;
                        String name = aClass.getSimpleName();
                        if (name.startsWith("Tea")) {
                            repository = cache.get(TeaRepository.class);
                        } else if (name.startsWith("Pen")) {
                            repository = cache.get(PenRepository.class);
                        } else if (name.startsWith("Phone")) {
                            repository = cache.get(PhoneRepository.class);
                        }
                        try {
                            constructor.setAccessible(true);
                            Object object = constructor.newInstance(repository);
                            Field field = aClass.getDeclaredField("instance");
                            field.setAccessible(true);
                            field.set(null, object);
                            cache.put(aClass, object);
                        } catch (InvocationTargetException | InstantiationException
                                 | IllegalAccessException | NoSuchFieldException e) {
                            System.out.println("Exception in constructor " + constructor.getName());
                            e.printStackTrace();

                        }
                    });
        });
    }
}
