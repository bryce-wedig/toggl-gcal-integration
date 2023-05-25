# Integration between Toggl time tracker and Google Calendar

Creates Google Calendar events for Toggl time entries

## Configuration notes

- Create the jar: `mvn clean package`
- Create a file `toggl-gcal-integration.json` and put it in the same directory as the jar. It should look like
```
{
    "lastSuccessfulRunTime": "2023-05-25T13:38:50.857541",
    "calendarId": "----------------------------@group.calendar.google.com",
    "togglToken": "----Toggl API token----",
    "togglWorkspaceId": "----Toggl Workspace ID----",
    "timezone": "America/Chicago"
}
```
where the `calendarId` is retrieved from the calendar's settings ("Integrate calendar" section).

- [Enable the Google Calendar API](https://developers.google.com/calendar/api/quickstart/java#enable_the_api)
- [Create an OAuth 2.0 Client ID](https://developers.google.com/calendar/api/quickstart/java#authorize_credentials_for_a_desktop_application)
- Because the script is running on a headless server, execute the program locally and grant the application permission to generate the `tokens/StoredCredential`, then move that directory and its contents to the jar directory
- Schedule the jar to be run via a shell script using `cron`: for every hour,
```
0 * * * * /usr/bin/sh /home/ubuntu/toggl-gcal-integration/run_toggl_gcal_integration.sh
```
