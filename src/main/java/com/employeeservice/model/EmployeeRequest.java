package com.employeeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRequest {

    @NotNull(message = "Employee Id must be present")
    private int employeeId;
    @NotNull(message = "Employee Name must not be null")
    private String employeeName;
    @NotNull(message = "Employee City must not be null")
    private String employeeCity;
    @NotNull(message = "Employee Phone must not be null")
    private String employeePhone;
    @NotNull(message = "Employee Java Experience must be present")
    private double javaExperience;
    @NotNull(message = "Employee Spring Experience must be present")
    private double springExperience;

}
