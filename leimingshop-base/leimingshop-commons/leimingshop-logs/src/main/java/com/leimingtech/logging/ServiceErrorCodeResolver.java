////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
///package com.leimingtech.logging;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.leimingtech.exception.ServiceStatusCode;
//import com.youcash.foundation.sdk.util.service.ServiceErrorCodeMapper;
//import com.youcash.foundation.sdk.util.service.mapper.*;
//import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
//import org.springframework.util.ClassUtils;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ServiceErrorCodeResolver {
//    private static final boolean isMethodArgumentNotValidExceptionAvailable = ClassUtils.isPresent("org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException", (ClassLoader)null);
//    private static final boolean isConstraintViolationExceptionAvailable = ClassUtils.isPresent("javax.validation.ConstraintViolationException", (ClassLoader)null);
//    private static final boolean isSecurityContextAvailable = ClassUtils.isPresent("org.springframework.security.core.context.SecurityContext", (ClassLoader)null);
//    private static final boolean isObjectMapperAvailable = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", (ClassLoader)null);
//    private static final boolean isWebApplicationExceptionAvailable = ClassUtils.isPresent("javax.ws.rs.WebApplicationException", (ClassLoader)null);
//    private Map<Class<? extends Throwable>, ServiceErrorCodeMapper<?>> exceptionMappers = new HashMap();
//    private static final ServiceErrorCodeResolver INSTANCE = new ServiceErrorCodeResolver();
//
//    protected ServiceErrorCodeResolver() {
//        this.init();
//    }
//
//    private void init() {
//        this.register(new ServiceExceptionServiceErrorCodeMapper());
//        if(isObjectMapperAvailable) {
//            this.register(JsonMappingException.class, ServiceStatusCode.CLIENT_BAD_REQUEST_ERROR);
//            this.register(JsonParseException.class, ServiceStatusCode.CLIENT_BAD_REQUEST_ERROR);
//        }
//
//        if(isConstraintViolationExceptionAvailable) {
//            this.register(new ConstraintViolationExceptionServiceErrorCodeMapper());
//        }
//
//        if(isMethodArgumentNotValidExceptionAvailable) {
//            this.register(MethodArgumentNotValidException.class, ServiceStatusCode.CLIENT_VALIDATION_ERROR);
//        }
//
//        if(isSecurityContextAvailable) {
//            this.register(new AuthenticationExceptionServiceErrorCodeMapper());
//            this.register(new AccessDeniedExceptionServiceErrorCodeMapper());
//        }
//
//        if(isWebApplicationExceptionAvailable) {
//            this.register(new WebApplicationExceptionServiceErrorCodeMapper());
//        }
//
//    }
//
//    public static ServiceErrorCodeResolver getInstance() {
//        return INSTANCE;
//    }
//
//    private ServiceErrorCodeMapper<Throwable> findServiceErrorCodeMapper(Class<? extends Throwable> clazz) {
//        ServiceErrorCodeMapper mapper = (ServiceErrorCodeMapper)this.exceptionMappers.get(clazz);
//        return (ServiceErrorCodeMapper)(mapper != null?mapper:(clazz.equals(Throwable.class)?ServiceErrorCodeMapper.DEFAULT:this.findServiceErrorCodeMapper(clazz.getSuperclass())));
//    }
//
//    public ServiceErrorCode resolve(Throwable throwable) {
//        return this.findServiceErrorCodeMapper(throwable.getClass()).map(throwable);
//    }
//
//    public ServiceErrorCodeResolver register(ServiceErrorCodeMapper<?> mapper) {
//        this.exceptionMappers.put(mapper.getSupportedExceptionClass(), mapper);
//        return this;
//    }
//
//    public <T extends Throwable> ServiceErrorCodeResolver register(Class<T> clazz, ServiceErrorCode errorCode) {
//        return this.register(new SimpleServiceErrorCodeMapper(clazz, errorCode));
//    }
//}
