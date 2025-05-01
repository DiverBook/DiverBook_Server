package ada.divercity.diverbook_server.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface FileStorageService {
    public String saveFile(MultipartFile file) throws IOException;
    public Resource loadFile(String filename) throws MalformedURLException;
}
