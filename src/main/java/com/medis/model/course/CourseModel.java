package com.medis.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")
public class CourseModel {
    @Id
    @Column(length = 50)
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String uuid;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "slug_name", nullable = false, unique = true)
    private String slugName;

    @Lob
    @NotNull
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", nullable = true)
    private String image_url;

    @Column(name = "image_token", nullable = true)
    private String image_token;

    @NotNull
    @Column(name = "price", nullable = false)
    private long price;

    @NotNull
    @Column(name = "total_participant", nullable = false)
    private int totalParticipant;

    @Column(name = "is_released", nullable = false)
    private boolean isReleased;

    @Column(name = "rating", nullable = false)
    private float rating;

    @Column(name = "total_material", nullable = true)
    private int totalMaterial;

    @NotNull
    @Column(name = "exam_link", nullable = true)
    private String examLink;

    @ElementCollection
    @CollectionTable(name = "rating_distribution", joinColumns = @JoinColumn(name = "course_id"))
    @MapKeyColumn(name = "rating_value")
    @Column(name = "count")
    private Map<Integer, Integer> ratingDistribution;

    @Column(name = "created_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SubCourseModel> listSubCourse;

    @OneToMany(mappedBy = "course")
    private List<ReviewModel> listReview;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<CourseEnrollmentModel> listEnrollment;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<SubCourseEnrollmentModel> subcourseEnrollment;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<MaterialEnrollmentModel> materialEnrollment;

    @OneToMany(mappedBy = "course")
    @JsonIgnore
    private List<CourseTransactionModel> listTransaction;
}
