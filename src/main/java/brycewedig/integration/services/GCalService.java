package brycewedig.integration.services;

import brycewedig.integration.model.TogglTimeEntry;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.IOException;
import java.util.List;

public class GCalService {

    public void createEvent(TogglTimeEntry togglTimeEntry) {
        Event event = new Event()
                .setSummary(togglTimeEntry.getProjectName() + " - " + togglTimeEntry.getDescription());

        DateTime startDateTime = new DateTime(togglTimeEntry.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String calendarId = "primary";
//        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    private void create() {
        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Calendar service =
//                new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                        .setApplicationName(APPLICATION_NAME)
//                        .build();
    }

//    /**
//     * Application name.
//     */
//    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
//    /**
//     * Global instance of the JSON factory.
//     */
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    /**
//     * Directory to store authorization tokens for this application.
//     */
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private static final List<String> SCOPES =
//            Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
//
//    /**
//     * Creates an authorized Credential object.
//     *
//     * @param HTTP_TRANSPORT The network HTTP Transport.
//     * @return An authorized Credential object.
//     * @throws IOException If the credentials.json file cannot be found.
//     */
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
//        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//        //returns an authorized Credential object.
//        return credential;
//    }
}
