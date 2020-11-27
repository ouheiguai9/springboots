package com.byakuya.boot.factory.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created by ganzl on 2020/11/25.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<U> implements Auditable<U, String, LocalDateTime>, Serializable {
    @JsonProperty
    @Override
    public Optional<U> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @JsonIgnore
    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty
    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    @JsonIgnore
    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty
    @Override
    public Optional<U> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @JsonIgnore
    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonProperty
    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    @JsonIgnore
    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public int hashCode() {
        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        if (!getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        }

        AbstractAuditableEntity<?> that = (AbstractAuditableEntity<?>) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Transient
    @Override
    public boolean isNew() {
        return null == getId();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable U createdBy;
    private @Nullable LocalDateTime createdDate;
    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable U lastModifiedBy;
    private @Nullable LocalDateTime lastModifiedDate;
}
