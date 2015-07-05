package core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DegreeDAO extends CrudRepository<Degree, String> {

    public Degree findById(String id);

    @Override
    public Iterable<Degree> findAll();

}
