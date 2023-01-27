package com.employeeservice.service;

import com.employeeservice.constant.AppConstants;
import com.employeeservice.model.*;
import com.employeeservice.repository.EmployeeRepository;
import com.employeeservice.repository.EmployeeSkillSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements IEmployeeService {

    private final EmpKafkaProducer empKafkaProducer;
    private final EmployeeRepository employeeRepository;
    private final EmployeeSkillSetRepository employeeSkillSetRepository;

    @Override
    public Mono<EmployeeResponse> createEmployee(EmployeeRequest employeeRequest) {

        log.info("In createEmployee(-) - Id: " + employeeRequest.getEmployeeId());

        Employee employee = new Employee(
                employeeRequest.getEmployeeId(),
                employeeRequest.getEmployeeName(),
                employeeRequest.getEmployeeCity(),
                employeeRequest.getEmployeePhone());
        EmployeeSkillSet employeeSkillSet = new EmployeeSkillSet(
                employeeRequest.getEmployeeId(),
                employeeRequest.getJavaExperience(),
                employeeRequest.getSpringExperience());

        empKafkaProducer.sendMessages(employeeRequest);

        if(!isFullOfNulls(employeeRequest)) {
            log.info("Employee request body with required values");
            return employeeRepository.existsByEmployeeId(employeeRequest.getEmployeeId())
                    .flatMap(exists -> {
                        if (Boolean.TRUE.equals(exists)) {
                            log.info("Employee already exists");
                            return Mono.zip(
                                    Mono.just(employee),
                                    Mono.just(employeeSkillSet)
                            ).map(t -> new EmployeeResponse(
                                    t.getT1().getEmployeeId(),
                                    t.getT1().getEmployeeName(),
                                    t.getT1().getEmployeeCity(),
                                    t.getT1().getEmployeePhone(),
                                    t.getT2().getJavaExperience(),
                                    t.getT2().getSpringExperience(),
                                    AppConstants.EMP_ALREADY_EXITS));
                        } else {
                            log.info("New Employee Create");
                            return Mono.zip(Mono.just(employee)
                                                    .flatMap(employeeRepository::save)
                                                    .log("Employee object"),
                                            Mono.just(employeeSkillSet)
                                                    .flatMap(employeeSkillSetRepository::save)
                                                    .log("Employee skill set object"))
                                    .map(t -> new EmployeeResponse(
                                            t.getT1().getEmployeeId(),
                                            t.getT1().getEmployeeName(),
                                            t.getT1().getEmployeeCity(),
                                            t.getT1().getEmployeePhone(),
                                            t.getT2().getJavaExperience(),
                                            t.getT2().getSpringExperience(),
                                            AppConstants.EMP_CREATED));
                        }
                    });
        }
        log.info("Employee request body contains null/empty values");
        return Mono.just(new EmployeeResponse(employeeRequest.getEmployeeId(),
                employeeRequest.getEmployeeName(),
                employeeRequest.getEmployeeCity(),
                employeeRequest.getEmployeePhone(),
                employeeRequest.getJavaExperience(),
                employeeRequest.getSpringExperience(),
                AppConstants.NULL_OR_EMPTY_CHECK_MSG));
    }

    @Override
    public Flux<EmployeeRequest> findByJavaExp(EmpJavaExperience javaExperience) {

        log.info("In findByJavaExp(-): "+javaExperience.getJavaExperience());
        Flux<EmployeeSkillSet> skillSetFlux =
                employeeSkillSetRepository.findByJavaExperienceGreaterThanEqual(
                        javaExperience.getJavaExperience()
                );
        Flux<Employee> employeeFlux = skillSetFlux.concatMap(
                emp-> employeeRepository.findByEmployeeId(emp.getEmployeeId()));

        return Flux.zip(employeeFlux,skillSetFlux)
                .map(t-> new EmployeeRequest(t.getT1().getEmployeeId(),
                        t.getT1().getEmployeeName(),
                        t.getT1().getEmployeeCity(),
                        t.getT1().getEmployeePhone(),
                        t.getT2().getJavaExperience(),
                        t.getT2().getSpringExperience()));
    }

    private boolean isFullOfNulls(EmployeeRequest request) {
        boolean empName = request.getEmployeeName() == null || request.getEmployeeName().isEmpty();
        boolean empCity = request.getEmployeeCity() == null || request.getEmployeeCity().isEmpty();
        boolean empPhone = request.getEmployeePhone() == null || request.getEmployeePhone().isEmpty();
        log.info("isFullOfNulls: {}", (empName || empCity || empPhone));

        return empName || empCity || empPhone;
    }

}
