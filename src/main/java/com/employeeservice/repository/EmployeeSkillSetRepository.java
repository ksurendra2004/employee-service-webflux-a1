package com.employeeservice.repository;

import com.employeeservice.model.EmployeeSkillSet;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EmployeeSkillSetRepository extends ReactiveCassandraRepository<EmployeeSkillSet, Integer> {
    @AllowFiltering
    Flux<EmployeeSkillSet> findByJavaExperienceGreaterThanEqual(double javaExp);
    /*@AllowFiltering
    Flux<EmployeeSkillSet> findBySpringExperience(double springExp);*/
}
