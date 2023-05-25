# Integration between Toggl time tracker and Google Calendar

Create Google Calendar events for Toggl time entries

Schedule the jar to be run via a shell script using `cron`: for every hour,
```
0 * * * * /usr/bin/sh /home/ubuntu/toggl-gcal-integration/run_toggl_gcal_integration.sh
```