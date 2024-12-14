package com.medis.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.medis.model.user.DokterModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "material_enrollment")
public class MaterialEnrollmentModel {
    @Id
    @Column(length = 50)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "is_finished", nullable = false)
    private boolean isFinished;

    @ManyToOne
    private CourseModel course;

    @ManyToOne
    private SubCourseModel subcourse;

    @ManyToOne
    private MaterialModel material;

    @ManyToOne
    @JsonIgnore
    private DokterModel user;
}
