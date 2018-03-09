# MailTester
MailTester is an app meant for quick tests on email servers and email clients that people working on email support have perform several times. 
It also includes a handy a Outlook Email format (MSG) to standar email format converter to help people to investigate issues on sample emails sent by clients.

*We discourage and strongly advise against using this on a real user's account. This should be only used with test accounts.*

## Usage
Download the app, setup an account then test it for IMAP, SMTP through SSL. 

### IMAP test
The IMAP test will check and try to use SASL LOGIN as preffered method, if not available will use the default LOGIN option. 
The app will use IMAPS on port 993 and STARTTLS on any other port provided by the user. 

### SMTP test has a few options
 * EML (for sending copies of EML or MSG messages to another address through JavaMail) 
 * Attachment (you pick any attachment to send to another user, this might render the app unresponsive for some time depending on the size of the attachment and internet connection) 
 * HTML email, we generate a dummy (lorem ipsum) HTML formatted email and send it to the address specified. 
 * Plain, we generate a dummy (lorem ipsum) email and send to the addresss specified. 

### MSG2EML
 Select an MSG file, select the output folder and convert.  *Yes, that simple!* 

### Logs 
The app collect logs from other tests and tasks and will show them to you here. 

## Bug
*Found a bug, do you have a suggestion?* 
Please report it [here](https://github.com/mansueli/MailTester/issues)

## License
This app is licensed under Apache 2.0 

We use the following components:
[lorem](https://github.com/mdeanda/lorem) licensed under [MIT license](https://choosealicense.com/licenses/mit/) to generate plain-text and HTML messages.
[Simple-Java-Mail](http://www.simplejavamail.org/) licensed also with [Apache 2.0](https://choosealicense.com/licenses/apache-2.0/) used for all SMTP and MSG conversion. 
[BoostrapFX](https://github.com/aalmiray/bootstrapfx) licensed under [MIT license](https://choosealicense.com/licenses/mit/) mostly for IMAP testing and the results screen.
[slf4j-simple](https://www.slf4j.org/api/org/slf4j/impl/SimpleLogger.html) for logging results, licensed under [MIT license](https://choosealicense.com/licenses/mit/).
[ControlsFX](https://bitbucket.org/controlsfx/controlsfx) for Dialogs, which is licensed under [BSD-3-Clause](https://choosealicense.com/licenses/bsd-3-clause/)