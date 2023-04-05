# toggl/gcal integration design

objective: every hour between 6a-midnight, run a script that looks for new toggl time-tracking entries and creates corresponding gcal events

toggl is the source of truth, only push updates to gcal

TODO need unique identifier for gcal entries

1. retrieve last successful run time from a json file
2. retrieve authentication information from a json file
3. query toggl for time-tracking entries created since last successful run time
4. also query toggl for entries modified since last successful run time, and grab corresponding information (definitely name, but also any other properties included in naming convention?)
5. create corresponding events in gcal on specific calendar with specific naming convention
6. update any corresponding events in gcal?

architecture:
* toggl java client
    * authenticate
    * query time-tracking entries
* gcal client
    * authenticate
    * create events on a specific calendar
* main java class to do the stuff
* [shell script which invokes jar](https://stackoverflow.com/questions/7855666/cron-job-for-a-java-program)
* cron job to run the shell script