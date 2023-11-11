1. Open the project in IntelliJ
2. In application.yaml file, replace /*use your mysql root password*/ beside password with you mysql root password
3. Add new Configuration, choose Springboot and give Calendar Application as Main class. Make sure jdk17 has been selected as I developed on jdk17
4. Do mvn clean install, this install & build all dependencies. Maven is inbuilt in intellij.
5. Run the application, database will be created automatically with tables.
6. Once application is run, you can browse below swagger url to check list of api's I've created
7. Check this swagger url: http://localhost:8080/swagger-ui/index.html#
   
Note: 
1. attendees is a comma separated string which has userids of users we add for event
2. date param for fetch events by date or conflict api can be given in this format ex:2023-11-11
3. userIds param for fetch events by date is a comma separated string which has userids of users
4. interval days are in days & totaloccurences are no of times
5. duration in time slots api is in minutes

If I could've spend additional time I would be trying to consider
1. Currently this is a Work Calendar. I could introduce multiple calendars like birthday, holidays etc like how it is in Google Calendar.
2. I would introduce Notification system into the project, it could be email triggers, inapp triggers.
3. Usually many do give validations on frontend side, I prefer giving validations on backend as well. I could've added validations properly on backend code as well
