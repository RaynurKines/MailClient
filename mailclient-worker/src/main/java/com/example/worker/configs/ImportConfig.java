package com.example.worker.configs;

import com.example.store.EnableMailClientStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import({
        EnableMailClientStore.class
})
@EnableScheduling
@Configuration
public class ImportConfig {
}
