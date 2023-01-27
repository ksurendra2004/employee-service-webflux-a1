package com.employeeservice.service;

import com.employeeservice.model.EmpJavaExperience;
import com.employeeservice.model.EmployeeRequest;
import com.employeeservice.model.EmployeeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEmployeeService {

    public Mono<EmployeeResponse> createEmployee(EmployeeRequest employeeRequest);
    public Flux<EmployeeRequest> findByJavaExp(EmpJavaExperience javaExperience);
}
