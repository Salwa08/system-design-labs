package ma.xproce.videoservice.dto;



import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoRequest {

    private String name;
    private String url;
    private String description;
    private String datePublication;
    private CreatorRequest creator;
}