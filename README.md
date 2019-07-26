# obss-jobs
A job advert and application system, it was my project for the Summer Internship program of OBSS on July 2019.

## Project Description
The application consists of 2 different profiles: Regular User (Applicant) and HR Expert. Users are logged in and authenticated via LinkedIn authentication. After login they are able to apply for jobs, see their applications' statuses and add skills.

HR Experts are authenticated with LDAP authentication, they are able to see and evaluate applications, change application status, set activation and expiration dates or the content for job adverts or add new job ads. They are also able to add a user to blacklist if they wish to. System sends an email to users in case of an application status change. System is also able to sort the candidates with respect to their skills' compatibility with job requirements.

System also contains a free text search among the User profiles which is achieved with SOLR. 

## Technologies used

### Frontend

* Vue.js
* Bootstrap
* Sass
* NPM

### Backend

* Spring Boot
* Hibernate
* Lombok
* LDAP
* Solr

### Database

* MySQL
