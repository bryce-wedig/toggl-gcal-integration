package brycewedig.integration.services;

import brycewedig.integration.exceptions.TogglException;
import brycewedig.integration.model.TogglProject;
import brycewedig.integration.model.TogglTimeEntry;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static brycewedig.integration.helpers.TogglConstants.*;

public class TogglService {

    private static final String ENDPOINT_GET_PROJECT = "https://api.track.toggl.com/api/" + API_VERSION + "/workspaces/:{workspace_id}/projects/:{project_id}";
    private static final String ENDPOINT_GET_TIME_ENTRIES = "https://api.track.toggl.com/api/" + API_VERSION + "/me/time_entries";

    private final OkHttpClient client;
    private final ObjectMapper mapper;

    public TogglService(OkHttpClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
    }

    // https://developers.track.toggl.com/docs/api/time_entries
    public List<TogglTimeEntry> getTimeEntries(long since) throws TogglException {
        Request request = new Request.Builder()
                .url(ENDPOINT_GET_TIME_ENTRIES + "?since=" + since)
                .build();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            if (response.body() != null) {
                if (response.code() == HTTP_OK) {
                    return mapper.readValue(response.body().string(), new TypeReference<>() {});
                } else throw new TogglException(response.code() + ": " + response.body().string());
            } else throw new TogglException(ERROR_NULL_RESPONSE_BODY);
        } catch (IOException e) { throw new TogglException(e.getMessage(), e); }
    }

    public TogglProject getProject(long workspaceId, long projectId) throws TogglException {
        Request request = new Request.Builder()
                .url(ENDPOINT_GET_PROJECT
                        .replace(TOKEN_PROJECT_ID, String.valueOf(projectId))
                        .replace(TOKEN_WORKSPACE_ID, String.valueOf(workspaceId)))
                .build();
        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            if (response.body() != null) {
                if (response.code() == HTTP_OK) {
                    return mapper.readValue(response.body().string(), TogglProject.class);
                } else throw new TogglException(response.code() + ": " + response.body().string());
            } else throw new TogglException(ERROR_NULL_RESPONSE_BODY);
        } catch (IOException e) { throw new TogglException(e.getMessage(), e); }
    }
}
