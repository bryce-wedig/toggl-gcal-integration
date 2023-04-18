package brycewedig.integration;

import brycewedig.integration.exceptions.TogglException;
import brycewedig.integration.helpers.JsonHelper;
import brycewedig.integration.helpers.TogglAuthInterceptor;
import brycewedig.integration.model.TogglProject;
import brycewedig.integration.model.TogglTimeEntry;
import brycewedig.integration.services.TogglService;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class run {

    private static final String TOKEN = "c09456837aeac76823b1109c29d441f1";

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new TogglAuthInterceptor(TOKEN)).build();

        TogglService togglService = new TogglService(client);

        long workspaceId = 5077210;
        long projectId = 185616065;

        try {
            TogglProject togglProject = togglService.getProject(workspaceId, projectId);
            System.out.println(togglProject);
        } catch (TogglException e) {
            throw new RuntimeException(e);
        }

        File file = new File("toggl-gcal-integration.json");

        try {
            LocalDateTime lastSuccessfulRunTime = JsonHelper.getLastSuccessfulRunTime(file);
            String togglToken = JsonHelper.getTogglToken(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // TODO query toggl for time-tracking entries created since last successful run time
        List<TogglTimeEntry> timeEntriesToCreate = togglService.getTimeEntries();

        // TODO create corresponding events in gcal on specific calendar with specific naming convention
    }
}
