package core;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StudentDAO extends CrudRepository<Student, String> {

    public Student findByUsername(String username);

    @Override
    public Iterable<Student> findAll();

}
