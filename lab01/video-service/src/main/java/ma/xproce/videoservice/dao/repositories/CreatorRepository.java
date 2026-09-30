package ma.xproce.videoservice.dao.repositories;

import ma.xproce.videoservice.dao.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
}