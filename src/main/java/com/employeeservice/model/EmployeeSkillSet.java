package com.employeeservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value = "emp_skill")
public class EmployeeSkillSet {

    @PrimaryKeyColumn(name = "emp_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private int employeeId;
    @PrimaryKeyColumn(name = "java_exp", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private double javaExperience;
    @PrimaryKeyColumn(name = "spring_exp", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private double springExperience;

}
