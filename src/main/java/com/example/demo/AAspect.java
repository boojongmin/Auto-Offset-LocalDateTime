package com.example.demo;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;

@Aspect
@Component
public class AAspect {
    @Around("@within(org.springframework.stereotype.Controller)")
    public  Object process(final ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            Object[] args = joinPoint.getArgs();
            result = joinPoint.proceed();
            for (Object obj: args) {
                if(obj instanceof BindingAwareModelMap) {
                    Map<String, Object> m = ((BindingAwareModelMap) obj).asMap();
                    Set<String> keys = m.keySet();
                    for (String k : keys) {
                        Object o = m.get(k);
                        Field[] arr = o.getClass().getDeclaredFields();
                        for (int i = 0; i < arr.length; i++) {
                            Field f = arr[i];
                            f.setAccessible(true);
                            if(f.getType().getName().equals("java.time.LocalDateTime")) {
                                LocalDateTime l = (LocalDateTime)f.get(o);
                                OffsetDateTime offsetDateTime = l.atOffset(ZoneOffset.ofHours(-9));
                                var bm = (BindingAwareModelMap)obj;
                                f.set(o, offsetDateTime.toLocalDateTime());
                            }
                        }

                    }


                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }
}
