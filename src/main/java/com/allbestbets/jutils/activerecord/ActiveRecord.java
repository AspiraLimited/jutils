package com.allbestbets.jutils.activerecord;

import com.allbestbets.jutils.NotImplementedException;

import java.sql.ResultSet;
import java.util.Date;

public abstract class ActiveRecord<T> {

    private Long id;
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public Integer getIntId() {
        return id == null ? null : id.intValue();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public T findById(Long id) {
        throw new NotImplementedException();
    }

    public T fromResultSet(ResultSet rs) {
        throw new NotImplementedException();
    }
}
