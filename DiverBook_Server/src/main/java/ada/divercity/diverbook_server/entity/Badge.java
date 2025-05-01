package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "badge")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {

    @Id
    @Column(length = 50)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    private String imageUrl;
}