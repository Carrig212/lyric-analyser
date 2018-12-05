package me.william.anderson.lyricanalyser.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("api.properties")
public class Consumer {
    @Value("${api.key}")
    private String apiKey;
}
