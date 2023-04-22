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

    private static final String TOKEN = "c09456837aeac76823b1109c29d441f1";

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }

    public static void main(String[] args) {

        long workspaceId = 5077210L;

        File file = new File("toggl-gcal-integration.json");

        try {
            LocalDateTime lastSuccessfulRunTime = JsonHelper.getLastSuccessfulRunTime(file);
            String togglToken = JsonHelper.getTogglToken(file);

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
                System.out.println(timeEntry.getProjectName() + ": " + timeEntry.getDescription());
            }

            // TODO create corresponding events in gcal on specific calendar with specific naming convention
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service =
                    new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            // https://developers.google.com/calendar/api/quickstart/java
            // https://developers.google.com/calendar/api/guides/create-events#java

        } catch (IOException | TogglException e) {
            throw new RuntimeException(e);
        }
    }
}
