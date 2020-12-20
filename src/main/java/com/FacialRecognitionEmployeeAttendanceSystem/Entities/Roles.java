package com.FacialRecognitionEmployeeAttendanceSystem.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity(name = "tblRoles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Lob
    @Column(name = "note", length=512)
    private String note;

    @Lob
    @Column(name = "description", length=512)
    private String description;

    @Column(name = "salary_rate", nullable = false, columnDefinition="Decimal(19,4)")
    private double salaryRate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_disabled", columnDefinition = "bit default 0")
    private boolean isDisabled;
}
