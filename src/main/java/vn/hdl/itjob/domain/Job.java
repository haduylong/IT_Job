package vn.hdl.itjob.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hdl.itjob.util.SecurityUtil;
import vn.hdl.itjob.util.constant.LevelEnum;

@Entity
@Table(name = "jobs")
@Getter
@Setter
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Job's name can not be null")
    private String name;
    private boolean active;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private LevelEnum level;

    @NotBlank(message = "Job's location can not be null")
    private String location;
    private Integer quantity;
    private Double salary;
    private Instant startDate;
    private Instant endDate;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    /* relationship */
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "job")
    @JsonIgnore
    private List<Resume> resumes;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    @JsonIgnoreProperties(value = { "jobs" })
    private List<Skill> skills;

    /* preprocess */
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse("");
        this.updatedAt = Instant.now();
    }
}
