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
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @JoinColumn(name = "found_user_id", nullable = false)
    private User foundUser;

    private LocalDateTime foundDate;

//    @Lob
    private String memo;
}
