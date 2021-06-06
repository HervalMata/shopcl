package com.shopcl.shopclbackend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        LOGGER.info("UFileUploadUtil | saveFile is started");
        Path uploadPath = Paths.get(uploadDir);
        LOGGER.info("UFileUploadUtil | saveFile | uploadPath : " + uploadPath);
        LOGGER.info("UFileUploadUtil | saveFile | Files.exists(uploadPath) : " + Files.exists(uploadPath));
        if (!Files.exists(uploadPath)) {
            LOGGER.info("UFileUploadUtil | saveFile | Files.createDirectories is called");
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            LOGGER.info("UFileUploadUtil | saveFile | filePath : " + filePath);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("UFileUploadUtil | saveFile | Files.copy is called");
        } catch (IOException ex) {
            LOGGER.error("UFileUploadUtil | saveFile | ex.getMessage() : " + ex.getMessage());
            throw new IOException("Não foi possível salvar o arquivo: " + fileName, ex);
        }
    }

    public static void cleanDir(String dir) {
        LOGGER.info("UFileUploadUtil | cleanDir is started");
        LOGGER.info("UFileUploadUtil | cleanDir | dir : " + dir);
        Path dirPath = Paths.get(dir);
        LOGGER.info("UFileUploadUtil | cleanDir | dirPath : " + dirPath);
        try {
            Files.list(dirPath).forEach(file -> {
                LOGGER.info("UFileUploadUtil | cleanDir | file : " + file.toString());
                LOGGER.info("UFileUploadUtil | cleanDir | Files.isDirectory(file) : " + Files.isDirectory(file));
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("UFileUploadUtil | cleanDir | delete is completed");
                    } catch (IOException ex) {
                        LOGGER.error("UFileUploadUtil | cleanDir | ex.getMessage() : " + ex.getMessage());
                        LOGGER.error("Não foi possível apagar o arquivo: " + file);
                    }
                }
            });
        } catch (IOException ex) {
            LOGGER.error("UFileUploadUtil | cleanDir | ex.getMessage() : " + ex.getMessage());
            LOGGER.error("Não foi possível listar diretório: " + dirPath);
        }
    }
}
