package dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import core.Period;

@Transactional
public interface PeriodDAO extends CrudRepository<Period, Integer> {

    public Period findById(Integer id);

    @Override
    public Iterable<Period> findAll();

}
