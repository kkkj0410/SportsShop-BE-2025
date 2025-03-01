//package com.teamproject.back.config;
//
//import org.junit.platform.commons.util.ClassFilter;
//import org.junit.platform.commons.util.ReflectionUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
//import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
//import org.springframework.beans.factory.support.AbstractBeanDefinition;
//import org.springframework.beans.factory.support.BeanDefinitionBuilder;
//import org.springframework.beans.factory.support.BeanDefinitionRegistry;
//import org.springframework.stereotype.Repository;
//import org.springframework.stereotype.Service;
//
//import static org.mockito.Mockito.mock;
//
//public class MockAllRepository implements BeanFactoryPostProcessor {
//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
//        ClassFilter classFilter = ClassFilter.of(clazz -> clazz.isAnnotationPresent(Repository.class));
//        ReflectionUtils.findAllClassesInPackage("com.teamproject.back", classFilter)
//                .forEach(clazz -> {
//                    AbstractBeanDefinition bean = BeanDefinitionBuilder
//                            .genericBeanDefinition(clazz)
//                            .getBeanDefinition();
//                    registry.registerBeanDefinition(clazz.getSimpleName(), bean);
//                    beanFactory.registerSingleton(clazz.getSimpleName(), mock(clazz));
//                });
//    }
//}
