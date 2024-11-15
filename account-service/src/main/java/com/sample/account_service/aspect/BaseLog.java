package com.sample.account_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class BaseLog {
    protected Object proceedingLog(ProceedingJoinPoint joinPoint, Logger log) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String argsString;
        if (args == null) {
            argsString = null;
        } else if (args.length == 0) {
            argsString = "[]";
        } else {
            argsString = Arrays.stream(args)
                    .map(String::valueOf)
                    .collect(Collectors.joining(",\n\t\t\t"));
            argsString = "[\n\t\t\t" + argsString + "\n\t\t]";
        }
        Signature signature = joinPoint.getSignature();
        log.debug("BEFORE:\n\t\033[34m{}.{}(\n\t\t\033[32m{}\033[34m\n\t)",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                argsString);
        Object result = joinPoint.proceed(args);


        String resultString;

        if (result == null) {
            resultString = null;
        } else if (result instanceof Collection<?> r) {
            if (r.size() == 0) {
                resultString = "[]";
            } else {
                resultString = r.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",\n\t\t\t"));
                resultString = "[\n\t\t\t" + resultString + "\n\t\t]";
            }
        } else {
            resultString = String.valueOf(result);
        }

        log.debug("AFTER:\n\t\033[34m{}.{}(\n\t\t\033[32m{}\033[34m\n\t)\033[0m = \033[34m{}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                args,
                resultString);
        return result;
    }
}
