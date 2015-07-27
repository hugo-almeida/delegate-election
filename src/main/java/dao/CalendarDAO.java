package dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import core.Calendar;

@Transactional
public interface CalendarDAO extends CrudRepository<Calendar, Integer> {

    public Calendar findByYear(int year);

    @Override
    public Iterable<Calendar> findAll();

    public Calendar findFirstByOrderByYearDesc();

}
