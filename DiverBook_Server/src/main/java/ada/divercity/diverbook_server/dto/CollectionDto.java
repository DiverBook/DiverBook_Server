package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.Collection;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionDto {
    private Integer id;
    private UUID ownerId;
    private UUID foundUserId;
    private LocalDateTime foundDate;
    private String memo;

    public static CollectionDto fromEntity(Collection collection) {
        return CollectionDto.builder()
                .id(collection.getId())
                .ownerId(collection.getOwner().getId())
                .foundUserId(collection.getFoundUser().getId())
                .foundDate(collection.getFoundDate())
                .memo(collection.getMemo())
                .build();
    }
}
