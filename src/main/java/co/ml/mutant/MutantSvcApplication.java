package co.ml.mutant;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class MutantSvcApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(MutantSvcApplication.class, args);
	}
	
	/**
	 * Since the app is enabling async task execution, this bean defines the
	 * behavior of the async task execution and the available resources
	 * @return Executor to execute the async tasks
	 */
    @Bean (name = "taskExecutor")
    public Executor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("PersistenceThread-");
        executor.initialize();
        return executor;
    }
}
