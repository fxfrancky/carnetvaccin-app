package com.carnetvaccin.app.backend.commons;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Column(name = "created_on")
//    @Temporal(TemporalType.DATE)
    private LocalDateTime createdOn;
//    private String createdOn;

    @Column(name = "updated_on")
//    @Temporal(TemporalType.DATE)
//    private String updatedOn;
    private LocalDateTime updatedOn;

    @PrePersist
    public void prePersist(){
      this.setCreatedOn(LocalDateTime.now());
    }

    @PreUpdate
    public void preUpdate(){
        this.setUpdatedOn(LocalDateTime.now());
    }

}
