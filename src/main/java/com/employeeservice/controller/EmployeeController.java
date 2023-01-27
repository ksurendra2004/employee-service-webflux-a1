package com.employeeservice.controller;

import com.employeeservice.model.EmpJavaExperience;
import com.employeeservice.model.EmployeeRequest;
import com.employeeservice.model.EmployeeResponse;
import com.employeeservice.service.IEmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService iEmployeeService;

    @PostMapping("/createEmployee")
    Mono<EmployeeResponse> createEmployee(@RequestBody @Valid EmployeeRequest employeeRequest){
        return iEmployeeService.createEmployee(employeeRequest);
    }

    @PostMapping("/findEmpSkillSet")
    Flux<EmployeeRequest> findAllGreater(@RequestBody EmpJavaExperience javaExperience){
        return iEmployeeService.findByJavaExp(javaExperience);
    }
}
