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

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Lob
    @Column(name = "note", length=512)
    private String note;

    @Lob
    @Column(name = "description", length=512)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_active", columnDefinition = "bit default 0")
    private boolean isActive;
}
