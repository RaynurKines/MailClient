package com.example.clientapi.config;

import com.example.store.EnableMailClientStore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
        EnableMailClientStore.class
})
@Configuration
public class ImportConfig {
}
