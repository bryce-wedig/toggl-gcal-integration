package brycewedig.integration.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TogglTimeEntry {

    @JsonProperty(required = true) private long id;
    @JsonProperty(required = true) private long project_id;
    @JsonProperty(required = true) private LocalDateTime start;
    @JsonProperty(required = true) private LocalDateTime stop;
    @JsonProperty(required = true) private String server_deleted_at;

    // constructors
    public TogglTimeEntry() {}
    public TogglTimeEntry(long id, long project_id, LocalDateTime start, LocalDateTime stop, String server_deleted_at) {
        this.id = id;
        this.project_id = project_id;
        this.start = start;
        this.stop = stop;
        this.server_deleted_at = server_deleted_at;
    }

    // getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getStop() {
        return stop;
    }

    public void setStop(LocalDateTime stop) {
        this.stop = stop;
    }

    public String getServer_deleted_at() {
        return server_deleted_at;
    }

    public void setServer_deleted_at(String server_deleted_at) {
        this.server_deleted_at = server_deleted_at;
    }

    @Override
    public String toString() {
        return "TogglTimeEntry{" +
                "id=" + id +
                ", project_id=" + project_id +
                ", start=" + start +
                ", stop=" + stop +
                ", server_deleted_at='" + server_deleted_at + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TogglTimeEntry togglTimeEntry = (TogglTimeEntry) o;
        return id == togglTimeEntry.id && project_id == togglTimeEntry.project_id && Objects.equals(start, togglTimeEntry.start) && Objects.equals(stop, togglTimeEntry.stop);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project_id, start, stop);
    }
}
