package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.CollectionDto;
import ada.divercity.diverbook_server.dto.CollectionRequest;

import java.util.UUID;

public interface CollectionService {
    public CollectionDto createCollection(UUID ownerId, CollectionRequest request);
}
