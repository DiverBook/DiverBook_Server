package ada.divercity.diverbook_server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static java.nio.file.Paths.get;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = get(uploadDir).resolve(filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public Resource loadFile(String filename) throws MalformedURLException {
        Path path = get(uploadDir).resolve(filename).normalize();
        return new UrlResource(path.toUri());
    }
}
