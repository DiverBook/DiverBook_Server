package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invalid_refresh_token")
    private String token;

    @Column
    private Timestamp invalidAt;
}
