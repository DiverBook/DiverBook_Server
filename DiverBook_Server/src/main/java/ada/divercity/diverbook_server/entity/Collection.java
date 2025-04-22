package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "collection", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"owner_id", "found_user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owener_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "found_user_id", nullable = false)
    private User foundUser;

    private LocalDateTime foundDate;

    @Lob
    private String memo;
}
