package brycewedig.integration.services;

import brycewedig.integration.model.TogglTimeEntry;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

public class GCalService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_EVENTS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static void createEvent(Calendar service, TogglTimeEntry togglTimeEntry, String calendarId, String timezone) {
        Event event = new Event()
                .setSummary(togglTimeEntry.getDescription() + " - " + togglTimeEntry.getProjectName());

        // set start time
        DateTime startDateTime = new DateTime(togglTimeEntry.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(timezone);
        event.setStart(start);

        // set end time
        DateTime endDateTime = new DateTime(togglTimeEntry.getStop());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(timezone);
        event.setEnd(end);

        // set description with toggl time entry id
        String timeEntryId = String.valueOf(togglTimeEntry.getId());
        event.setDescription(timeEntryId);

        try {
            event = service.events().insert(calendarId, event).execute();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // load client secrets
        InputStream in = GCalService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
