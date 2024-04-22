package mk.ukim.finki.nbafantasy.repository.jpa;

import mk.ukim.finki.nbafantasy.model.Group;
import mk.ukim.finki.nbafantasy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Jpa repository for {@link Group}.
 */
public interface GroupRepository extends JpaRepository<Group, Long> {

    /**
     * Finds all groups in which the given user is participant.
     *
     * @param user given user
     * @return list of {@link Group}
     */
    List<Group> findAllByUsersContains(User user);
}
