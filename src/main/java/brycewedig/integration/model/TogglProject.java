package brycewedig.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TogglProject {

    @JsonProperty(required = true) private Long id;
    @JsonProperty(required = true) private String name;

    // constructors
    public TogglProject() {}
    public TogglProject(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // getters and setters
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

    @Override
    public String toString() {
        return "TogglProject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
