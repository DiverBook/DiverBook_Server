package ada.divercity.diverbook_server.service;

import ada.divercity.diverbook_server.dto.CollectionDto;
import ada.divercity.diverbook_server.dto.CollectionRequest;
import ada.divercity.diverbook_server.entity.Collection;
import ada.divercity.diverbook_server.entity.User;
import ada.divercity.diverbook_server.exception.CustomException;
import ada.divercity.diverbook_server.exception.ErrorCode;
import ada.divercity.diverbook_server.repository.CollectionRepository;
import ada.divercity.diverbook_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    private final CollectionRepository collectionRepository;
    private final UserRepository userRepository;

    public CollectionDto createCollection(UUID ownerId, CollectionRequest request) {
        if (ownerId.equals(request.getFoundUserId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
        }

        if (collectionRepository.existsByOwnerIdAndFoundUserId(ownerId, request.getFoundUserId())) {
            throw new CustomException(ErrorCode.DUPLICATE_COLLECTION);
        }

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        User foundUser = userRepository.findById(request.getFoundUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Collection collection = Collection.builder()
                .owner(owner)
                .foundUser(foundUser)
                .foundDate(LocalDateTime.now())
                .memo(request.getMemo())
                .build();

        Collection savedCollection = collectionRepository.save(collection);
        return CollectionDto.fromEntity(savedCollection);
    }


    @Transactional(readOnly = true)
    public List<CollectionDto> getAllCollections(UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Collection> collections = collectionRepository.findByOwnerId(ownerId);

        return collections.stream()
                .map(CollectionDto::fromEntity)
                .toList();
    }

    public CollectionDto patchCollection(UUID ownerId, CollectionRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!collectionRepository.existsByOwnerIdAndFoundUserId(ownerId, request.getFoundUserId())) {
            throw new CustomException(ErrorCode.COLLECTION_NOT_FOUND);
        }

        Collection collection = collectionRepository.findByOwnerIdAndFoundUserId(ownerId, request.getFoundUserId());
        collection.setMemo(request.getMemo());

        Collection savedCollection = collectionRepository.save(collection);

        return CollectionDto.fromEntity(savedCollection);
    }

    public void deleteAllCollections(UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        collectionRepository.deleteByOwnerId(ownerId);
    }
}
