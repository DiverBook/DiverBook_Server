package ada.divercity.diverbook_server.dto;

import ada.divercity.diverbook_server.entity.Collection;
import ada.divercity.diverbook_server.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionDto {
    private Long id;
    private User owner;
    private User foundUser;
    private LocalDateTime foundDate;
    private String memo;

    public static CollectionDto fromEntity(Collection collection) {
        return CollectionDto.builder()
                .id(collection.getId())
                .owner(collection.getOwner())
                .foundUser(collection.getFoundUser())
                .foundDate(collection.getFoundDate())
                .memo(collection.getMemo())
                .build();
    }
}
