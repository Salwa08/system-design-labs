package ma.xproce.videoservice;

import ma.xproce.videoservice.dao.entities.Creator;
import ma.xproce.videoservice.dao.entities.Video;
import ma.xproce.videoservice.dao.repositories.CreatorRepository;
import ma.xproce.videoservice.dao.repositories.VideoRepository;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class VideoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public CommandLineRunner start(
			CreatorRepository creatorRepository,
			VideoRepository videoRepository
	) {
		return args -> {

			List<Creator> creators = List.of(
					Creator.builder()
							.name("x")
							.email("x@gmail.com")
							.build(),

					Creator.builder()
							.name("y")
							.email("y@gmail.com")
							.build(),

					Creator.builder()
							.name("z")
							.email("z@gmail.com")
							.build()
			);

			creatorRepository.saveAll(creators);

			List<Video> videos = List.of(
					Video.builder()
							.name("Video 1")
							.url("video1.com")
							.description("First video")
							.datePublication(new Date())
							.creator(creators.get(0))
							.build(),

					Video.builder()
							.name("Video 2")
							.url("video2.com")
							.description("Second video")
							.datePublication(new Date())
							.creator(creators.get(1))
							.build(),

					Video.builder()
							.name("Video 3")
							.url("video3.com")
							.description("Third video")
							.datePublication(new Date())
							.creator(creators.get(2))
							.build()
			);

			videoRepository.saveAll(videos);
		};
	}
}