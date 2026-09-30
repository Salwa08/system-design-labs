package ma.xproce.videoservice.dao.repositories;


import ma.xproce.videoservice.dao.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}