package ApisBackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ApisBackend.model.Tutorial;
//Add new
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import ApisBackend.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

   Page<Tutorial> findByPublished(boolean published, Pageable pageable);
   Page<Tutorial> findByTitleContaining(String title, Pageable pageable);
}