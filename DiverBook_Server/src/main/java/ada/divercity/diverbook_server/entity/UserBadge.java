package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_badge", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "badge_code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "badge_code", nullable = false)
    private Badge badge;

    @Builder.Default
    private LocalDateTime acquiredDate = LocalDateTime.now();
}
