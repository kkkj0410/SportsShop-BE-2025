//package com.teamproject.back.config;
//
//import org.junit.platform.commons.util.ClassFilter;
//import org.junit.platform.commons.util.ReflectionUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.stereotype.Service;
//
//import static org.mockito.Mockito.mock;
//
//public class MockAllService implements BeanFactoryPostProcessor {
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        ClassFilter classFilter = ClassFilter.of(clazz -> clazz.isAnnotationPresent(Service.class));
//        ReflectionUtils.findAllClassesInPackage("com.teamproject.back", classFilter)
//                .forEach(clazz -> {
//                    beanFactory.registerSingleton(clazz.getSimpleName(), mock(clazz));
//                });
//    }
//}
