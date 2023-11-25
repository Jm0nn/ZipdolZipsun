package com.ssafy.zip.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

	@Around(value = "execution(* com.ssafy.zip.*.model.mapper.*.*(..))")
	public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		log.debug("around call method : {} ", joinPoint.getSignature());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		Object proceed = joinPoint.proceed();

		stopWatch.stop();

		log.debug("summary : {}", stopWatch.shortSummary());
		log.debug("totalTime : {}", stopWatch.getTotalTimeMillis());
		log.debug("pretty : {}", stopWatch.prettyPrint());

		return proceed;
	}

}