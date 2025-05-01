package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private String image;
}