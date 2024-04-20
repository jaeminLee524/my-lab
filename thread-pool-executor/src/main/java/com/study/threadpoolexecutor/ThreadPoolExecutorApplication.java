package com.study.threadpoolexecutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class ThreadPoolExecutorApplication {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext context = SpringApplication.run(ThreadPoolExecutorApplication.class, args);

		ThreadPoolRunner runner = context.getBean(ThreadPoolRunner.class);
		for (int i = 0; i < 10; i++) {
			runner.getRemainThreads();
			Thread.sleep(1000);
		}
	}
}
