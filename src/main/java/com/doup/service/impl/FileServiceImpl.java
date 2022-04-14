package com.doup.service.impl;

import com.doup.domain.FileProperties;
import com.doup.error.FileException;
import com.doup.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileServiceImpl implements FileService {

    private final Path filePath;

    @Autowired
    public FileServiceImpl(FileProperties fileProperties) {
        filePath = Paths.get(fileProperties.getDocDir()).toAbsolutePath().normalize();
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        System.out.println("Path " + filePath);
        Path path = filePath.resolve(fileName).normalize();
        System.out.println("FilePath" + path);
        try {
            UrlResource resource = new UrlResource(path.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new FileException("file " + fileName + " not found");
        } catch (MalformedURLException e) {
            throw new FileException("file " + fileName + " not found", e);
        }
    }
}
