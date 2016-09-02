# officer-rainbow
This is the source for Officer Rainbow, an android app that checks certain websites
for a users Color, as assigned by the State for purposes of drug or alcohol testing.
If offers custom text or email messages to multiple destinations to notify if today
is a testing day, it also has the option of setting off the phone's alarm and display
a notification.

It stores information regarding probation agent and office contact information, with
a 'Call Now' button for easily making a connection to either.

It also gives reminders for probation meetings and provides a countdown timer to the
end of a users probation.

The apk is pushed to an endpoint web server, for download by end users.

If you have asterisk running and it's configured properly, this will also call a testing
site that does not provide website updates, record the testing message, feed it back
to Google speech and provide the text.
