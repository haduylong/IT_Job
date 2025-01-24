package vn.hdl.itjob.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.hdl.itjob.domain.listener.CompanyListener;
import vn.hdl.itjob.util.SecurityUtil;

@Entity
@Table(name = "companies")
@Getter
@Setter
@EntityListeners(CompanyListener.class)
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Company's name can not be null")
    private String name;
    private String logo;
    @NotBlank(message = "Company's address can not be null")
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    /* relationship */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnore
    private List<User> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    @JsonIgnore
    private List<Job> jobs;

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
