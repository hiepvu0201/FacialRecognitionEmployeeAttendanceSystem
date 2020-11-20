package com.FacialRecognitionEmployeeAttendanceSystem.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "tblPayslips")
public class Payslips {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "pay_date", nullable = false)
    private Date payDate;

    @Column(name = "working_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double workingSalary;

    @Column(name = "public_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double publicSalary;

    @Column(name = "other_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double otherSalary;

    @Column(name = "annual_leave_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double annualLeaveSalary;

    @Column(name = "overtimve_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double overtimeSalary;

    // phu cap
    @Column(name = "allowance", nullable = false)
    private int allowance;

    @Column(name = "bonus", nullable = false, columnDefinition="Decimal(19,4)")
    private double bonus;

    @Column(name = "tax", nullable = false, columnDefinition="Decimal(19,4)")
    private double tax;

    @Column(name = "deduction_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double deductionSalary;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_disabled", columnDefinition = "bit default 0")
    private boolean isDisabled;
}
