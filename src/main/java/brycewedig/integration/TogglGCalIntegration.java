package brycewedig.integration;

import brycewedig.integration.exceptions.TogglException;
import brycewedig.integration.helpers.JsonHelper;
import brycewedig.integration.helpers.TogglAuthInterceptor;
import brycewedig.integration.model.TogglProject;
import brycewedig.integration.model.TogglTimeEntry;
import brycewedig.integration.services.GCalService;
import brycewedig.integration.services.TogglService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TogglGCalIntegration {

    private static final String APPLICATION_NAME = "toggl-gcal-integration";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // toggl-gcal-integration keys
    private static final String TOGGL_TOKEN = "togglToken";
    private static final String LAST_SUCCESSFUL_RUN_TIME = "lastSuccessfulRunTime";
    private static final String TOGGL_WORKSPACE_ID = "togglWorkspaceId";
    private static final String CALENDAR_ID = "calendarId";
    private static final String TIMEZONE = "timezone";
    // http://www.timezoneconverter.com/cgi-bin/zonehelp.tzc?cc=US&ccdesc=United%20States

    public static void main(String[] args) {

        File file = new File("src/main/resources/toggl-gcal-integration.json");

        try {
            LocalDateTime lastSuccessfulRunTime = JsonHelper.getLocalDateTime(file, LAST_SUCCESSFUL_RUN_TIME);
            String togglToken = JsonHelper.getString(file, TOGGL_TOKEN);
            long workspaceId = JsonHelper.getLong(file, TOGGL_WORKSPACE_ID);

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
                if (correspondingProject != null) {
                    timeEntry.setProjectName(correspondingProject.getName());
                }
            }

            // https://developers.google.com/calendar/api/guides/create-events#java
            // https://developers.google.com/calendar/api/v3/reference/events

            // build a new authorized API client service
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service =
                    new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, GCalService.getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            String calendarId = JsonHelper.getString(file, CALENDAR_ID);
            String timezone = JsonHelper.getString(file, TIMEZONE);

            // TODO query for calendar events created in the last week

            // TODO compare their descriptions against ids of time entries

            // TODO for any time entries that don't yet have calendar events, create them

            // TODO for any time entries that have calendar events, make the appropriate update to the calendar event

            for (TogglTimeEntry timeEntry : timeEntriesToCreate) {
                System.out.println(timeEntry.getDescription() + " - " + timeEntry.getProjectName());

                GCalService.createEvent(service, timeEntry, calendarId, timezone);
            }

        } catch (IOException | TogglException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}
