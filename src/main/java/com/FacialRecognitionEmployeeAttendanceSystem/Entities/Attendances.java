package com.FacialRecognitionEmployeeAttendanceSystem.Entities;

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
@Entity(name = "tblAttendances")
public class Attendances {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "date_check", nullable = false)
    private Date dateCheck;

    @Column(name = "status", nullable = false, columnDefinition = "bit default 1")
    private boolean status;

    @Lob
    @Column(name = "note", length=512)
    private String note;

    @Column(name = "working_hours", nullable = false)
    private float workingHours;

    @Column(name = "checkin_at")
    private LocalDateTime checkinAt;

    @Column(name = "checkout_at")
    private LocalDateTime checkoutAt;

    @Column(name = "is_disabled", columnDefinition = "bit default 0")
    private boolean isDisabled;

    @Column(name = "user_id", nullable = false)
    public long userId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users")
    private Users users;

    @Column(name = "shift_id", nullable = false)
    public long shiftId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shifts")
    private Shifts shifts;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public boolean getStatus() {
        return true;
    }
}
