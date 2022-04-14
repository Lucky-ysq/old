package com.doup.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public interface FileService {
    Resource loadFileAsResource(String fileName);
}
