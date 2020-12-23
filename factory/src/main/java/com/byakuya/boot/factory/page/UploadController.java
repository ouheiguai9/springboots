package com.byakuya.boot.factory.page;

import com.byakuya.boot.factory.ConstantUtils;
import com.byakuya.boot.factory.exception.FileIOException;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Created by ganzl on 2020/12/23.
 */
@RestController
@Validated
public class UploadController {
    @PostMapping(value = "/upload", headers = {"avatar=true"})
    public ResponseEntity<ResponseFile> uploadAvatar(@NotNull MultipartFile file) {
        return ResponseEntity.ok(storeFile(file, "avatar"));
    }

    /**
     * 文件存储
     *
     * @param file        源文件
     * @param subLocation 子目录
     * @return 存储名
     */
    @SuppressWarnings("SameParameterValue")
    private ResponseFile storeFile(MultipartFile file, String subLocation) {
        try {
            String fileName = String.format("%s_%s", uuid16(), StringUtils.cleanPath(file.getOriginalFilename()));
            Path targetLocation = uploadLocation.resolve(subLocation);
            File dir = targetLocation.toFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.deleteOnExit();
                if (!dir.mkdirs()) {
                    throw new FileIOException();
                }
            }
            Files.copy(file.getInputStream(), targetLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(ConstantUtils.ADDITIONAL_RESOURCE_PATH)
                    .path(subLocation)
                    .path("/")
                    .path(fileName).toUriString();
            ResponseFile rtnVal = new ResponseFile();
            rtnVal.setFileDownloadUri(fileDownloadUri);
            rtnVal.setFileName(fileName);
            rtnVal.setFileSize(file.getSize());
            rtnVal.setFileType(file.getContentType());
            return rtnVal;
        } catch (IOException e) {
            throw new FileIOException(e);
        }
    }

    /**
     * 生成16位UUID
     *
     * @return UUID
     */
    private String uuid16() {
        UUID id = UUID.randomUUID();
        String[] idd = id.toString().split("-");
        return idd[0] + idd[1] + idd[2];
    }

    private final Path uploadLocation = Paths.get(ConstantUtils.UPLOAD_DIR).toAbsolutePath().normalize();

    @Data
    private static class ResponseFile {
        private String fileDownloadUri;
        private String fileName;
        private long fileSize;
        private String fileType;
    }
}
