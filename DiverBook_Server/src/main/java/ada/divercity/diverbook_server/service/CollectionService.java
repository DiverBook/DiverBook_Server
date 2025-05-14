package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.CollectionDto;
import ada.divercity.diverbook_server.dto.CollectionRequest;

import java.util.List;
import java.util.UUID;

public interface CollectionService {
    public CollectionDto createCollection(UUID ownerId, CollectionRequest request);

    public List<CollectionDto> getAllCollections(UUID ownerId);

    public CollectionDto patchCollection(UUID id, CollectionRequest request);

    public void deleteAllCollections(UUID ownerId);
}
