# Privacy Dashboard

[![coverage](https://sifis-home.github.io/privacydashboard/reports/jacoco.svg)](https://sifis-home.github.io/privacydashboard/reports/index.html)

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

You can also import the project to your IDE of choice as you would with any
Maven project. Read more on [how to import Vaadin projects to different 
IDEs](https://vaadin.com/docs/latest/flow/guide/step-by-step/importing) (Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Log in the app

To enter the application as a Data Subject: user=subject<number_between_0-49> password=subject<same_number> (e.g. user=subject25 password=subject25)

To enter the application as a Data Controller: user=controller<number_between_0-49> password=controller<same_number> (e.g. user=controller19 password=controller19)

To enter the application as a DPO: user=DPO<number_between_0-49> password=DPO<same_number> (e.g. user=DPO48 password=DPO48)

## Application views

Once logged in, the pages the user can access depend on their role. This is the home page:

![Home screen of the web app](readmeImgs\home.png)

### Profile

By clicking the button in the bottom left corner you access the profile modal, where you can change your credentials and log out.

For data subjects, there is the possibility to ask for the removal of their data from all of thier applications.

### Contacts

In this page all the contacts of the user are listed by name, role and email address. For each contact there is a link that takes the user to the conversation page and the list of the applications they have in common (by clicking on the name you are taken to the app's page). 

For DPO/Data Controller, a contact is whoever has a common application with them.

For a data subject, a contact is a non-data subject who has a common application with them.

### Messages

In this pages all the conversations of the user are listed. The user can also start a new conversation with another user.

### Apps

In this page the user can check all the applications they have. For each application there is a description, the vote of the GDPR Questionnaire, a link for the privacy notice of the app and the contact list of those who also possess the app.

Data subjects also view the list of consenses they gave to the applications, with the possibility to cancel them.

### Questionnaire

This page is reserved for DPO/Data Controllers, where they can verify the adeherence to the GDPR of their applications. Once they selected an application, they are brought to the actual questionnaire page. It is divided into three sections, followed by a summary:
- Personale data
- Security
- Tests and Certifications

Votes are one of three colors:
- Green: the answer satisfies the GDPR completely
- Orange: The answer partially staisfies the GDPR
- Red: The answer is contrary to the GDPR

### Rights

This page is vastly different depending on the role of the user.

For data subjects, this page allows them to request something of their right: access data, withdraw consent, ask information, compile a complaint and erase data. They can also view a list of thier handled requests and pending requests.

For DPO/Data Controllers, the page lists all the right requests of the data subjects, with the possibility to filter between handled and pending. By selecting a request they can answer it and change its status.

### Privacy Notice

In this page the privacy notices for all of the user applications are listed.

For DPO/Data Controllers it si possible to create a privacy notice. They can either:
- Create a privacy notice from scratch
- Create a privacy notice from a template
- Upload a file containing the privacy notice

## Running the test suite

To run the test suite, run the command `mvn clean test`. You can run all the tests in a class by adding the flag `-Dtest=<class>`. You can also run a single test by using the flag like this `-Dtest=<class>#methodName`.

To create a coverage report, run the command `mvn jacoco:report`

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

Once the JAR file is built, you can run it using
`java -jar target/privacydashboard-1.0-SNAPSHOT.jar`

To build the project without being interrupted by eventual test failures, add the flag `-DskipTests`

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Dependancies amd libraries used
- Maven Spring Framework is used to create the Model-View-Controller implementation and provides the Web Services.
- Vaadin is used to create the GUI of the web application.
- JUnit is used to create and execute the tests of the application.
- Jacoco is used to obtain the instruction coverage of the tests.

## Useful links

- Read the documentation at [vaadin.com/docs](https://vaadin.com/docs).
- Follow the tutorials at [vaadin.com/tutorials](https://vaadin.com/tutorials).
- Watch training videos and get certified at [vaadin.com/learn/training](https://vaadin.com/learn/training).
- Create new projects at [start.vaadin.com](https://start.vaadin.com/).
- Search UI components and their usage examples at [vaadin.com/components](https://vaadin.com/components).
- View use case applications that demonstrate Vaadin capabilities at [vaadin.com/examples-and-demos](https://vaadin.com/examples-and-demos).
- Discover Vaadin's set of CSS utility classes that enable building any UI without custom CSS in the [docs](https://vaadin.com/docs/latest/ds/foundation/utility-classes). 
- Find a collection of solutions to common use cases in [Vaadin Cookbook](https://cookbook.vaadin.com/).
- Find Add-ons at [vaadin.com/directory](https://vaadin.com/directory).
- Ask questions on [Stack Overflow](https://stackoverflow.com/questions/tagged/vaadin) or join our [Discord channel](https://discord.gg/MYFq5RTbBn).
- Report issues, create pull requests in [GitHub](https://github.com/vaadin/platform).


## Deploying using Docker

To build the Dockerized version of the project, run

```
docker build . -t privacydashboard:latest
```

Once the Docker image is correctly built, you can test it locally using

```
docker run -p 8080:8080 privacydashboard:latest
```
