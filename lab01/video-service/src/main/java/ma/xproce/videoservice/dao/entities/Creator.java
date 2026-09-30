package ma.xproce.videoservice.dao.entities;

import jakarta.persistence.*;
        import lombok.*;

        import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Creator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "creator")
    private List<Video> videos;
}