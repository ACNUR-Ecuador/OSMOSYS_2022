package com.sagatechs.generics.webservice.webModel;

import com.sagatechs.generics.persistence.model.State;

public class RoleWeb {

    private Long id;

    private String name;

    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
