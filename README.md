Open the project in IntelliJ
In application.yaml file, replace /*use your mysql root password*/ beside password with you mysql root password
Add new Configuration, choose Springboot and give Calendar Application as Main class. Make sure jdk17 has been selected as I developed on jdk17
Do mvn clean install, this install & build all dependencies. Maven is inbuilt in intellij.
Run the application, database will be created automatically with tables.
Once application is run, you can browse below swagger url to check list of api's I've created
Check this swagger url: http://localhost:8080/swagger-ui/index.html#
Note: 
attendees is a comma separated string which has userids of users we add for event
date param for fetch events by date or conflict api can be given in this format ex:2023-11-11
userIds param for fetch events by date is a comma separated string which has userids of users
interval days are in days & totaloccurences are no of times
duration in time slots api is in minutes
