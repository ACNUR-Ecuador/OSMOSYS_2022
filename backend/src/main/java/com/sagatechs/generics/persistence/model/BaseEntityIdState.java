package com.sagatechs.generics.persistence.model;

public abstract class BaseEntityIdState extends BaseEntity<Long> {

    /**
     * @return This method should return the primary key.
     */
    public abstract Long getId();
    public abstract State getState();


    @Override
    public String toString() {
        return "BaseEntity [id=" + this.getId() + "]";
    }
}