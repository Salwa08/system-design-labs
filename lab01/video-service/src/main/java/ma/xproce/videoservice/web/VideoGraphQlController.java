package ma.xproce.videoservice.web;

import ma.xproce.videoservice.dao.entities.Creator;
import ma.xproce.videoservice.dao.entities.Video;
import ma.xproce.videoservice.dao.repositories.CreatorRepository;
import ma.xproce.videoservice.dao.repositories.VideoRepository;
import ma.xproce.videoservice.dto.CreatorRequest;
import ma.xproce.videoservice.dto.VideoRequest;

import org.modelmapper.ModelMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import reactor.core.publisher.Flux;
import java.util.stream.Stream;

import java.util.Random;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class VideoGraphQlController {

    private final CreatorRepository creatorRepository;
    private final VideoRepository videoRepository;
    private final ModelMapper modelMapper;

    public VideoGraphQlController(
            CreatorRepository creatorRepository,
            VideoRepository videoRepository,
            ModelMapper modelMapper
    ) {
        this.creatorRepository = creatorRepository;
        this.videoRepository = videoRepository;
        this.modelMapper = modelMapper;
    }

    @QueryMapping
    public List<Video> videoList() {
        return videoRepository.findAll();
    }

    @QueryMapping
    public Creator creatorById(@Argument Long id) {
        return creatorRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                String.format("Creator %s not found", id)
                        )
                );
    }

    @MutationMapping
    public Creator saveCreator(@Argument CreatorRequest creator) {

        Creator creatorEntity =
                modelMapper.map(creator, Creator.class);

        return creatorRepository.save(creatorEntity);
    }

    @MutationMapping
    public Video saveVideo(@Argument VideoRequest video) throws ParseException {

        Video videoEntity =
                modelMapper.map(video, Video.class);

        Creator creatorEntity =
                modelMapper.map(video.getCreator(), Creator.class);

        Creator savedCreator =
                creatorRepository.save(creatorEntity);

        videoEntity.setCreator(savedCreator);

        videoEntity.setDatePublication(
                new SimpleDateFormat("dd/MM/yyyy")
                        .parse(video.getDatePublication())
        );

        return videoRepository.save(videoEntity);
    }

    @SubscriptionMapping
    public Flux<Video> notifyVideoChange() {
        return Flux.fromStream(
                Stream.generate(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Creator creator = Creator.builder()
                            .name("x" + new Random().nextInt())
                            .email("x@gmail.com")
                            .build();

                    Creator savedCreator = creatorRepository.save(creator);

                    Video video = videoRepository.findById(1L)
                            .orElseThrow(() -> new RuntimeException("Video 1 not found"));

                    video.setCreator(savedCreator);

                    return videoRepository.save(video);
                })
        );
    }
}