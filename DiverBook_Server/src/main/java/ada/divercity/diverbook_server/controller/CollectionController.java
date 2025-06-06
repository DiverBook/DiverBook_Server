package ada.divercity.diverbook_server.controller;

import ada.divercity.diverbook_server.dto.ApiResponse;
import ada.divercity.diverbook_server.dto.CollectionDto;
import ada.divercity.diverbook_server.dto.CollectionRequest;
import ada.divercity.diverbook_server.service.CollectionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@SecurityRequirement(name = "JWT")
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<ApiResponse<CollectionDto>> createCollection(
            @RequestBody CollectionRequest request,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(collectionService.createCollection(userId, request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<CollectionDto>>> getAllCollections(
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(collectionService.getAllCollections(userId)));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<CollectionDto>> patchCollection(
            @RequestBody CollectionRequest request,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());

        CollectionDto collection = collectionService.patchCollection(userId, request);
        return ResponseEntity.ok(ApiResponse.success(collection));
    }
}
