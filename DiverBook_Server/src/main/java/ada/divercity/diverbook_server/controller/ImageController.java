package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ApiResponse;
import ada.divercity.diverbook_server.service.FileStorageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SecurityRequirement(name = "JWT")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = fileStorageService.saveFile(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/view/")
                .path(filename)
                .toUriString();
        return ResponseEntity.ok(ApiResponse.success(url));
    }

    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> viewImage(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageService.loadFile(filename);
        String contentType = "image/png";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}
