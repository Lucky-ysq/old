package com.doup.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {
    private String docDir;
}
