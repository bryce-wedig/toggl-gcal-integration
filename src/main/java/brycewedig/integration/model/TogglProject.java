package brycewedig.integration.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TogglProject {

    @JsonProperty(required = true) private long id;
    @JsonProperty(required = true) private String name;

    // constructors
    public TogglProject() {}
    public TogglProject(long id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TogglProject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
