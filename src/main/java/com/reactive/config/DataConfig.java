package com.reactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value="local")
@Configuration
public class DataConfig {
}
