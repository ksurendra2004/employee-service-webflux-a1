package com.employeeservice.repository;

import com.employeeservice.model.Employee;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EmployeeRepository extends ReactiveCassandraRepository<Employee, Integer> {
    @AllowFiltering
    Flux<Employee> findByEmployeeId(Integer empId);
    @AllowFiltering
    Mono<Boolean> existsByEmployeeId(Integer empId);
}
