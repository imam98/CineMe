# CineMe
Movie information app designed to be your cinema companion. This project utilize [themoviedb.org](https://www.themoviedb.org/) api to receive all the necessary data. Therefore you need to have an api key to build this project.

## Configuring API Key
1. Login/sign up to [themoviedb.org](https://www.themoviedb.org/)
2. Go to the account settings page
3. Select the API menu
4. Request an API Key if you don't have one
5. Copy API (v3 auth)
6. Navigate to `.gradle` folder in your home directory  
   **Windows:** `C:\Users\<Your Username>\.gradle`  
   **Mac:** `/Users/<Your Username>/.gradle`  
   **Linux:** `/home/<Your Username>/.gradle`
7. Open `gradle.properties` (Create if it doesn't exist)
8. Put `CineMe_APIKey="<YOUR_API_KEY>"` inside the file
9. Save and close the file

**NOTE**  
This project is still in an early development, so don't expect anything to be running perfectly
