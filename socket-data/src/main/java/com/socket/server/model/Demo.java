package com.socket.server.model;


import java.io.Serializable;

public class Demo implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hehe{" +
                "name='" + name + '\'' +
                '}';
    }
}
