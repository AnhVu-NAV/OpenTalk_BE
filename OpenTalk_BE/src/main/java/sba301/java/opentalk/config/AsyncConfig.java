package sba301.java.opentalk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig implements AsyncConfigurer {
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();

        return runnable -> {
            LocaleContext localeContext = LocaleContextHolder.getLocaleContext();
            executor.execute(() -> {
                try {
                    LocaleContextHolder.setLocaleContext(localeContext);
                    runnable.run();
                } finally {
                    LocaleContextHolder.resetLocaleContext();
                }
            });
        };
    }

    @Override
    public Executor getAsyncExecutor() {
        return taskExecutor();
    }
}
