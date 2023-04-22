package brycewedig.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TogglTimeEntry {

    @JsonProperty(required = true) private Long id;
    @JsonProperty(required = true) private Long project_id;
    @JsonProperty(required = true) private String start;
    @JsonProperty(required = true) private String stop;
    @JsonProperty(required = true) private String server_deleted_at;
    @JsonProperty(required = true) private String description;
    @JsonProperty private String projectName;

    // constructors
    public TogglTimeEntry() {}
    public TogglTimeEntry(Long id, Long project_id, String start, String stop, String server_deleted_at, String description, String projectName) {
        this.id = id;
        this.project_id = project_id;
        this.start = start;
        this.stop = stop;
        this.server_deleted_at = server_deleted_at;
        this.description = description;
        this.projectName = projectName;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProject_id() {
        return project_id;
    }

    public void setProject_id(Long project_id) {
        this.project_id = project_id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

    public String getServer_deleted_at() {
        return server_deleted_at;
    }

    public void setServer_deleted_at(String server_deleted_at) {
        this.server_deleted_at = server_deleted_at;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getProjectName() { return projectName; }

    public void setProjectName(String projectName) { this.projectName = projectName; }

    @Override
    public String toString() {
        return "TogglTimeEntry{" +
                "id=" + id +
                ", project_id=" + project_id +
                ", start='" + start + '\'' +
                ", stop='" + stop + '\'' +
                ", server_deleted_at='" + server_deleted_at + '\'' +
                ", description='" + description + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
