package com.example.store;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.example.store.dao")
@EntityScan("com.example.store.entities")
@EnableJpaRepositories("com.example.store.repositories")
public class EnableMailClientStore {
}
