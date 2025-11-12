package com.hsebank.finance.di;

import com.hsebank.finance.di.annotations.Inject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Простой DI-контейнер (Dependency Injection Container)
 * Управляет созданием объектов и автоматическим внедрением зависимостей
 * Аналог: Spring IoC Container (упрощённая версия)
 */
public class DIContainer {

    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Map<Class<?>, Class<?>> registrations = new HashMap<>();

    /**
     * Регистрация связи интерфейс → реализация
     */
    public <T> void register(Class<T> interfaceClass, Class<? extends T> implementationClass) {
        registrations.put(interfaceClass, implementationClass);
        System.out.println("📦 Зарегистрирован: " + interfaceClass.getSimpleName() +
                " → " + implementationClass.getSimpleName());
    }

    /**
     * Регистрация конкретного класса
     */
    public <T> void register(Class<T> concreteClass) {
        registrations.put(concreteClass, concreteClass);
        System.out.println("📦 Зарегистрирован: " + concreteClass.getSimpleName());
    }

    /**
     * Получение экземпляра с автоматическим созданием и внедрением зависимостей
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> clazz) {
        if (singletons.containsKey(clazz)) {
            return (T) singletons.get(clazz);
        }

        Class<?> implementationClass = registrations.getOrDefault(clazz, clazz);

        try {
            T instance = (T) createInstance(implementationClass);
            singletons.put(clazz, instance);
            System.out.println("✅ Создан бин: " + clazz.getSimpleName());
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать экземпляр: " + clazz.getName(), e);
        }
    }

    /**
     * Создание экземпляра с рекурсивным внедрением зависимостей
     */
    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?> injectConstructor = findInjectConstructor(clazz);

        if (injectConstructor != null) {
            Class<?>[] parameterTypes = injectConstructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            // Рекурсивно создаём зависимости
            for (int i = 0; i < parameterTypes.length; i++) {
                parameters[i] = resolve(parameterTypes[i]);
            }

            return injectConstructor.newInstance(parameters);
        }

        Constructor<?> defaultConstructor = clazz.getDeclaredConstructor();
        return defaultConstructor.newInstance();
    }

    /**
     * Поиск конструктора с аннотацией @Inject
     */
    private Constructor<?> findInjectConstructor(Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Inject.class)) {
                return constructor;
            }
        }

        // Если нет @Inject, берём единственный конструктор
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length == 1) {
            return constructors[0];
        }

        return null;
    }

    /**
     * Регистрация готового синглтона
     */
    public <T> void registerSingleton(Class<T> clazz, T instance) {
        singletons.put(clazz, instance);
        System.out.println("✅ Зарегистрирован синглтон: " + clazz.getSimpleName());
    }

    public boolean isRegistered(Class<?> clazz) {
        return registrations.containsKey(clazz) || singletons.containsKey(clazz);
    }

    public void clear() {
        singletons.clear();
        registrations.clear();
    }

    public void printBeans() {
        System.out.println("\n═══ Зарегистрированные бины ═══");
        registrations.forEach((key, value) ->
                System.out.println("  • " + key.getSimpleName() + " → " + value.getSimpleName())
        );
        System.out.println("Всего: " + registrations.size() + " бинов\n");
    }
}