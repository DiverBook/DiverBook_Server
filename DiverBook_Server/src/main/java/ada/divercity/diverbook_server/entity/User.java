package ada.divercity.diverbook_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String userName;

    private String divisions;

    private String phoneNumber;

    private String interests;

    private String places;

    private String about;

    @Builder.Default
    private Boolean isActivated = false;

    private String profileImageUrl;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Collection> collections = new ArrayList<>();
}
