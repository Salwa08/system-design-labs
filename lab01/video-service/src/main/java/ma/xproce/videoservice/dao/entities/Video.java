package ma.xproce.videoservice.dao.entities;

import jakarta.persistence.*;
        import lombok.*;

        import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;
    private String description;

    private Date datePublication;

    @ManyToOne
    private Creator creator;
}