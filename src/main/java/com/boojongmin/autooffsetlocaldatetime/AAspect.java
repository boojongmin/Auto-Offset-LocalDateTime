package com.boojongmin.autooffsetlocaldatetime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.support.BindingAwareModelMap;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Aspect
@Component
public class AAspect {
    @Around("@within(org.springframework.stereotype.Controller)")
    public  Object process(final ProceedingJoinPoint joinPoint) {
        Object result = null;
        Supplier<Integer> getTimezoneTypeSupplier = null;
        try {
            Object[] args = joinPoint.getArgs();
            result = joinPoint.proceed();
            for (Object obj: args) {
                if(obj instanceof BindingAwareModelMap) {
                    Map<String, Object> m = ((BindingAwareModelMap) obj).asMap();
                    Set<String> keys = m.keySet();
                    for (String k : keys) {
                        Object o = m.get(k);
                        if(o == null) continue;

                        Field[] arr = o.getClass().getDeclaredFields();
                        for (int i = 0; i < arr.length; i++) {
                            Field f = arr[i];
                            f.setAccessible(true);
                            if(f.getType().getName().equals("java.time.LocalDateTime")) {

                                if(getTimezoneTypeSupplier == null) {
                                    getTimezoneTypeSupplier = getOffsetHour();
                                }

                                LocalDateTime l = (LocalDateTime)f.get(o);
                                if(l == null) continue;

                                LocalDateTime offsetedLocalDateTime = l.plusHours(getTimezoneTypeSupplier.get());
                                f.set(o, offsetedLocalDateTime);
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

    private Supplier<Integer> getOffsetHour() {
        Optional<Integer> closure = Optional.ofNullable(9);
        return () -> closure.orElse(0);
    }
}
