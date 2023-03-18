package brycewedig.integration;

import brycewedig.integration.exceptions.TogglException;
import brycewedig.integration.helpers.TogglAuthInterceptor;
import brycewedig.integration.model.TogglProject;
import brycewedig.integration.services.TogglService;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.OkHttpClient;

public class run {

    private static final String TOKEN = "c09456837aeac76823b1109c29d441f1";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new TogglAuthInterceptor(TOKEN)).build();

        TogglService togglService = new TogglService(client);

        long workspaceId = 5077210;
        long projectId = 185616065;

        try {
            TogglProject togglProject = togglService.getTogglProject(workspaceId, projectId);
            System.out.println(togglProject);
        } catch (TogglException e) {
            throw new RuntimeException(e);
        }
    }
}
