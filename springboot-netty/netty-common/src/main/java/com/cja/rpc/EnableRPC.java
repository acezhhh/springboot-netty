package com.cja.rpc;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Import({RPCClientRegistrar.class})
public @interface EnableRPC {

    //扫描服务包名
    String[] basePackages() default {"com.cja.service"};
}
