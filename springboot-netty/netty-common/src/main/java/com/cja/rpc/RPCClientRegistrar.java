package com.cja.rpc;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class RPCClientRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //扫描注解
        Map<String, Object> annotationAttributes = annotationMetadata
                .getAnnotationAttributes(ComponentScan.class.getName());
        String[] basePackages = (String[]) annotationAttributes.get("basePackages");

        //扫描类
        ClassPathScanningCandidateComponentProvider  scanner = getScanner();
        scanner.addIncludeFilter(new AnnotationTypeFilter(RPCClient.class));

        //扫描出class并装配为BeanDefinition
        Set<BeanDefinition> candidateComponents = new HashSet<>();
        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }

        candidateComponents.stream().forEach(beanDefinition -> {
            if (!beanDefinitionRegistry.containsBeanDefinition(beanDefinition.getBeanClassName())) {
                if (beanDefinition instanceof AnnotatedBeanDefinition) {
                    AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
                    AnnotationMetadata am = annotatedBeanDefinition.getMetadata();
                    Map<String, Object> attributes = am.getAnnotationAttributes(RPCClient.class.getCanonicalName());
                    this.registerRpcClient(beanDefinitionRegistry, am,attributes);
                }
            }
        });
    }

    private void registerRpcClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        // 指定工厂，使用@NettyRPC注解的接口，当代码中注入时，是从指定工厂获取，而这里的工厂返回的是代理
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(RpcClientFactoryBean.class);
        // @Autowrie：根据类型注入
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        String name = attributes.get("name") == null ? "" :(String)(attributes.get("name"));

        // 注定type属性
        definition.addPropertyValue("type", className);
        definition.addPropertyValue("serviceName", name);
        // 别名
        String alias = className + "RpcClient";
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(true);
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
                new String[] { alias });
        // 注册BeanDefinition
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

        //添加netty启动服务
//        NettyRPCUtil.putNettyEntity(name);
    }


    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // 判断候选人的条件:必须是独立的，然后是接口
                if (beanDefinition.getMetadata().isIndependent() && beanDefinition.getMetadata().isInterface()){
                    return true;
                }
                return false;
            }
        };
    }


}
