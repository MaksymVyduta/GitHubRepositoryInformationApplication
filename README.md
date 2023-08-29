GitHub Repository Information Application
This application retrieves non-forked repositories, their branches, and the last commit details for a given GitHub user.


How to Run


1) Clone the repository to your local machine:
   git clone https://github.com/MaksymVyduta/GitRepoSearch.git
2) Navigate to the project directory
3) Build and run the application using Gradle:
   ./gradlew bootRun

The application will be accessible at http://localhost:8080.


How to Use the Application


To use this application, you need to send a POST request with a JSON payload containing the GitHub username to the following endpoint: http://localhost:8080/.

Example JSON Payload:
{
"username": "your-github-username"
}
Make sure to replace your-github-username with the GitHub username for which you want to retrieve repository information.

Please note that you need to provide a valid GitHub token in the request header for authentication.


How to Obtain GitHub Token

To use the GitHub API, you need a personal access token. Here's how you can obtain and set it:

-Go to your GitHub account settings.
-Navigate to "Developer settings" > "Personal access tokens".
-Click on "Generate new token".
-Give your token a name, select the desired scopes (e.g., repo), and click "Generate token".
-Copy the generated token.
-Setting the GitHub Token
-Open the src/main/resources/application.properties file.
- Replace your-github-token with the token you obtained:
   github.api.token=your-github-token

How to Run Tests

Navigate to the project directory if you're not already there:
Run tests using Gradle:
./gradlew test

or

Run tests using the IntelliJ using JUnit configuration. In that case you will see the test results in the "Run" terminal at the bottom of the screen.


