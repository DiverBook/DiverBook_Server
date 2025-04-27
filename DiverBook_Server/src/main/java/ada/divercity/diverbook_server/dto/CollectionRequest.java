package ada.divercity.diverbook_server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CollectionRequest {
    private UUID foundUserId;
    private String memo;
}
