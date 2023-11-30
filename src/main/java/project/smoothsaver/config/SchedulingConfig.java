package project.smoothsaver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import project.smoothsaver.repository.SallingStoreRepository;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    final SallingStoreRepository sallingStoreRepository;

    public SchedulingConfig(SallingStoreRepository sallingStoreRepository) {
        this.sallingStoreRepository = sallingStoreRepository;
    }

    @Scheduled(cron = "0 0 8 * * ?") // koden bliver kørt hver dag kl 8.00
    @Transactional
    public void clearDatabase() {
        System.out.println("Clearing database");
       sallingStoreRepository.deleteAll();
    }
}
