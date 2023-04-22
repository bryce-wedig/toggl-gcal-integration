package brycewedig.integration.services;

import brycewedig.integration.model.TogglTimeEntry;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

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
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }
}
