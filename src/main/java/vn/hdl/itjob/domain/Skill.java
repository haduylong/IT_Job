package vn.hdl.itjob.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "skills")
@Getter
@Setter
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    /* relationship */
    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Subscriber> subscribers;

    /* preprocess */
    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = "hdl";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.createdBy = "hdl";
        this.createdAt = Instant.now();
    }
}
