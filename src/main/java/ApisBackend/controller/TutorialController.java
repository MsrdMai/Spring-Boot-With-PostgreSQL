package ApisBackend.controller;

import java.util.*;

import org.springframework.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ApisBackend.model.Tutorial;
import ApisBackend.repository.TutorialRepository;

// Add new
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")

public class TutorialController {

        @Autowired
        TutorialRepository tutorialRepository;


        @GetMapping("/tutorials")
        public ResponseEntity<Map<String, Object>> getAllTutorials (
                @RequestParam(required = false) String title,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "3") int size
        ) {

        try {
            Pageable paging = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Tutorial> pageTutorials;
            if (title == null) {
                pageTutorials = tutorialRepository.findAll(paging);
            } else {
                pageTutorials = tutorialRepository.findByTitleContaining(title, paging);
            }
            List<Tutorial> tutorials = pageTutorials.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        }

        @GetMapping("/tutorials/{id}")
        public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
            Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
            return tutorialData.map(tutorial -> new ResponseEntity<>(tutorial, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }

        @GetMapping("/tutorials/published")
        public ResponseEntity<Map<String, Object>> findByPublished(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "3") int size
        ) {

        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Tutorial> pageTutorials = tutorialRepository.findByPublished(true, paging);
            List<Tutorial> tutorials = pageTutorials.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("tutorials", tutorials);
            response.put("currentPage", pageTutorials.getNumber());
            response.put("totalItems", pageTutorials.getTotalElements());
            response.put("totalPages", pageTutorials.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        }

        @PostMapping("/tutorials")
        public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial this_tutorial) {

            try{
            Tutorial repo_tutorial = tutorialRepository
                .save(new Tutorial(this_tutorial.getTitle(), this_tutorial.getDescription(), false));
                return new ResponseEntity<>(repo_tutorial, HttpStatus.CREATED);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }

        @PutMapping("/tutorials/{id}")
        public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
            Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
            if (tutorialData.isPresent()) {
                Tutorial this_tutorial = tutorialData.get();
                this_tutorial.setTitle(tutorial.getTitle());
                this_tutorial.setDescription(tutorial.getDescription());
                this_tutorial.setPublished(tutorial.isPublished());
                return new ResponseEntity<>(tutorialRepository.save(this_tutorial), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        @DeleteMapping("/tutorials/{id}")
        public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
            try{
                tutorialRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }

        @DeleteMapping("/tutorials")
        public ResponseEntity<HttpStatus> deleteAllTutorials() {
            try{
                tutorialRepository.deleteAll();
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            catch (Exception e){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        }

    }

