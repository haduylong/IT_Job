package vn.hdl.itjob.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileService {
    @Value("${hdl.upload-dir.base-uri-path}")
    private String baseUri;

    public void createDirectory(String folder) throws URISyntaxException {
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File file = new File(path.toString());

        if (!file.isDirectory()) {
            try {
                Files.createDirectories(file.toPath());
                log.info(">>> Create {} successful", file.toPath().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            log.info(">>> Folder {} already exist", folder);
        }
    }

    public String store(String folder, MultipartFile file) throws URISyntaxException, IOException {
        // generate unique name
        String finalName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // save
        URI uri = new URI(baseUri + folder + "/" + finalName);
        Path path = Paths.get(uri);
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path,
                    StandardCopyOption.REPLACE_EXISTING);
        }

        return finalName;
    }

    public long getFileLength(String fileName, String folder) throws URISyntaxException {
        URI uri = new URI(baseUri + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File file = new File(path.toString());

        if (file.isDirectory() || !file.exists()) {
            return 0;
        }

        return file.length();
    }

    public InputStreamResource getResource(String fileName, String folder)
            throws URISyntaxException, FileNotFoundException {
        URI uri = new URI(baseUri + folder + "/" + fileName);
        Path path = Paths.get(uri);
        File file = new File(path.toString());

        return new InputStreamResource(new FileInputStream(file));
    }

}
