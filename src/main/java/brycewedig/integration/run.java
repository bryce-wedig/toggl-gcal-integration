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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class run {

    public static void main(String[] args) {

        File file = new File("src/main/resources/toggl-gcal-integration.json");

        try {
            LocalDateTime lastSuccessfulRunTime = JsonHelper.getLastSuccessfulRunTime(file);
            String togglToken = JsonHelper.getTogglToken(file);
            long workspaceId = JsonHelper.getWorkspaceId(file);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .addInterceptor(new TogglAuthInterceptor(togglToken)).build();

            TogglService togglService = new TogglService(client);

            // get current localdatetime as unix timestamp
            ZoneId zoneId = ZoneId.systemDefault();
            long since = lastSuccessfulRunTime.atZone(zoneId).toEpochSecond();

            // get time entries since last successful run time
            List<TogglTimeEntry> timeEntriesToCreate = togglService.getTimeEntries(since);

            // ignore any deleted items
            timeEntriesToCreate.removeIf(timeEntry -> timeEntry.getServer_deleted_at() != null);

//            timeEntriesToCreate.forEach(System.out::println);

            // get unique projects from list
            List<Long> projectIdList = new ArrayList<>();
            for (TogglTimeEntry timeEntry : timeEntriesToCreate) {
                if (!projectIdList.contains(timeEntry.getProject_id())) {
                    projectIdList.add(timeEntry.getProject_id());
                }
            }

            // for each projectId, query project and read into pojo
            List<TogglProject> togglProjectList = new ArrayList<>();
            for (Long projectId : projectIdList) {
                togglProjectList.add(togglService.getProject(workspaceId, projectId));
            }

            // populate projectName on time entries
            for (TogglTimeEntry timeEntry : timeEntriesToCreate) {
                TogglProject correspondingProject = null;
                for (TogglProject togglProject : togglProjectList) {
                    if (timeEntry.getProject_id().equals(togglProject.getId())) {
                        correspondingProject = togglProject;
                    }
                }
                timeEntry.setProjectName(correspondingProject.getName());
            }

            for (TogglTimeEntry timeEntry : timeEntriesToCreate) {
                System.out.println(timeEntry.getDescription() + " - " + timeEntry.getProjectName());
            }

            // TODO create corresponding events in gcal on specific calendar with specific naming convention

            // https://developers.google.com/calendar/api/quickstart/java
            // https://developers.google.com/calendar/api/guides/create-events#java

        } catch (IOException | TogglException e) {
            throw new RuntimeException(e);
        }
    }
}
