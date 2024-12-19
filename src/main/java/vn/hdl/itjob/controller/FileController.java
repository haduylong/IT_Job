package vn.hdl.itjob.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import vn.hdl.itjob.domain.response.ApiResponse;
import vn.hdl.itjob.domain.response.file.RespFileUploadDTO;
import vn.hdl.itjob.service.FileService;
import vn.hdl.itjob.util.exception.StorageException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {
    @Value("${hdl.upload-dir.base-uri-path}")
    private String baseUri;

    private final FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<ApiResponse<RespFileUploadDTO>> upload(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        // validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty, please upload a file");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream()
                .anyMatch(item -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException("File type not allowed");
        }

        // create directory
        this.fileService.createDirectory(baseUri + folder);
        // save file
        String fileUploadedName = this.fileService.store(folder, file);

        ApiResponse<RespFileUploadDTO> res = ApiResponse.<RespFileUploadDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Upload file successful")
                .data(new RespFileUploadDTO(fileUploadedName, Instant.now()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/files")
    public ResponseEntity<InputStreamResource> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {
        // validate
        if (fileName == null || folder == null) {
            throw new StorageException("Missing params required ,file name or folder is null");
        }

        // check file exist or not a directory
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File with name " + fileName + " not found");
        }

        InputStreamResource resource = this.fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentLength(fileLength)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
