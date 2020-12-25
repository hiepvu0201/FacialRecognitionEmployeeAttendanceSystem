package com.FacialRecognitionEmployeeAttendanceSystem.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Entity(name = "tblUsers")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "pin", nullable = false, unique = true, length = 10)
    private String pin;

    @Column(name = "dob", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dob;

    @Column(name = "home_address", nullable = false)
    private String homeAddress;

    @Column(name = "gross_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double grossSalary;

    @Column(name = "net_salary", nullable = false, columnDefinition="Decimal(19,4)")
    private double netSalary;

    @Lob
    @Column(name = "note", length=512)
    private String note;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_disabled", columnDefinition = "bit default 0")
    private boolean isDisabled;

    @Column(name = "department_id", nullable = false)
    public long departmentId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departments")
    private Departments departments;

    @Column(name = "role_id", nullable = false)
    public long roleId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roles")
    private Roles roles;

    @Column(name = "shift_id", nullable = false)
    public long shiftId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shifts")
    private Shifts shifts;
}
