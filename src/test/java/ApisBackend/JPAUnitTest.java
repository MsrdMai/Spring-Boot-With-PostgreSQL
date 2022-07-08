package ApisBackend;
import ApisBackend.repository.TutorialRepository;
import ApisBackend.model.Tutorial;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
public class JPAUnitTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TutorialRepository tutorialRepository;

    @Test
    public void shouldFindNoTutorialIfRepositoryIsEmpty() {
        Iterable<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).isEmpty();
    }

    @Test
    public void shouldStoreATutorial() {
        Tutorial tutorial = tutorialRepository.save(
                new Tutorial("Test title", "Test description", true)
        );
        assertThat(tutorial).hasFieldOrPropertyWithValue("title", "Test title");
        assertThat(tutorial).hasFieldOrPropertyWithValue("description", "Test description");
        assertThat(tutorial).hasFieldOrPropertyWithValue("published", true);
    }

    @Test
    public void shouldFindAllTutorials() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial tutorial3 = new Tutorial("Tutorial#3", "Description#3", true);
        testEntityManager.persist(tutorial3);
        Iterable<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(3).contains(tutorial1, tutorial2, tutorial3);
    }

    @Test
    public void shouldFindTutorialById() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial foundTutorial = tutorialRepository.findById(tutorial2.getId()).orElse(null);
        assertThat(foundTutorial).isEqualTo(tutorial2);
    }

    @Test
    public void shouldFindPublishedTutorials() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial tutorial3 = new Tutorial("Tutorial#3", "Description#3", true);
        testEntityManager.persist(tutorial3);
        Iterable<Tutorial> tutorials = tutorialRepository
                .findByPublished(true, PageRequest.of(0, Integer.MAX_VALUE));
        assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial3);
    }

    @Test
    public void shouldFindTutorialsByTitleContainingString() {
        Tutorial tutorial1 = new Tutorial("Spring Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Java Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial tutorial3 = new Tutorial("Spring Tutorial#3", "Description#3", true);
        testEntityManager.persist(tutorial3);
        Iterable<Tutorial> tutorials = tutorialRepository
                .findByTitleContaining("ring", PageRequest.of(0, Integer.MAX_VALUE));
        assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial3);
    }

    @Test
    public void shouldUpdateTutorialById() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial updatedTutorial = new Tutorial(
                "Updated tutorial#2", "Updated description#2", true
        );
        Tutorial tutorial = tutorialRepository.findById(tutorial2.getId()).orElse(null);
        assert tutorial != null;
        tutorial.setTitle(updatedTutorial.getTitle());
        tutorial.setDescription(updatedTutorial.getDescription());
        tutorial.setPublished(updatedTutorial.isPublished());
        Tutorial checkTutorial = tutorialRepository.findById(tutorial2.getId()).orElse(null);
        assert checkTutorial != null;
        assertThat(checkTutorial.getId()).isEqualTo(tutorial2.getId());
        assertThat(checkTutorial.getTitle()).isEqualTo(updatedTutorial.getTitle());
        assertThat(checkTutorial.getDescription()).isEqualTo(updatedTutorial.getDescription());
        assertThat(checkTutorial.isPublished()).isEqualTo(updatedTutorial.isPublished());
    }

    @Test
    public void shouldDeleteTutorialById() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        Tutorial tutorial3 = new Tutorial("Tutorial#3", "Description#3", true);
        testEntityManager.persist(tutorial3);
        tutorialRepository.deleteById(tutorial2.getId());
        Iterable<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).hasSize(2).contains(tutorial1, tutorial3);
    }

    @Test
    public void shouldDeleteAllTutorials() {
        Tutorial tutorial1 = new Tutorial("Tutorial#1", "Description#1", true);
        testEntityManager.persist(tutorial1);
        Tutorial tutorial2 = new Tutorial("Tutorial#2", "Description#2", false);
        testEntityManager.persist(tutorial2);
        tutorialRepository.deleteAll();
        Iterable<Tutorial> tutorials = tutorialRepository.findAll();
        assertThat(tutorials).isEmpty();
    }

}