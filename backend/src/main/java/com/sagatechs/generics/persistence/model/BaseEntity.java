package com.sagatechs.generics.persistence.model;

import java.io.Serializable;

public abstract class BaseEntity<PK extends Serializable> {

    /**
     * @return This method should return the primary key.
     */
    public abstract PK getId();


    @Override
    public String toString() {
        return "BaseEntity [id=" + this.getId() + "]";
    }
}