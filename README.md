<img src="watchlist.png" alt="Watchlist Image" width="6400" />

<a href="https://appetize.io/app/rz2scysw7s6sytetrycafienu4?device=pixel7&osVersion=13.0&scale=75" target="_blank" rel="nofollow noopener noreferrer"
aria-label="Live Demo"> <u>Live Demo 🚀</u> </a>


# Watchlist

---

## Project Description

Watchlist is a movie and tv series database for getting the latest information about trending or specific movies. 
Add a movie or tv series to your favourite to reference it later

---

## Screenshots
### OnBoarding
<img src="onboarding1.png" alt="First Onboarding Screen" width="90" /> <img src="onboarding2.png" alt="Second Onboarding Screen" width="90" /> <img src="onboarding3.png" alt="Third Onboarding Image" width="90" />

### Log In & Sign Up
<img src="login.png" alt="Log In Screen" width="90" /> <img src="sign-up.png" alt="Sign Up Screen" width="90" /> <img src="reset-password.png" alt="Reset Password Screen" width="90" />

### Trending, Search, and Favourites
<img src="trending.png" alt="Trending Screen" width="90" /> <img src="movie-search.png" alt="Movie Search Screen" width="90" /> <img src="series-search.png" alt="Series Search Screen" width="90" /> <img src="favourites.png" alt="Favourites Screen" width="90" />

### Movie and Series Details
<img src="series-detail.jpg" alt="Series Details Screen" width="90" /> <img src="movie-detail.jpg" alt="Movie Details Screen" width="90" />

---

## Technologies Used

* Jetpack Compose
* TMDB API
* Retrofit
* Firebase
* Dagger-Hilt
* Material 3 Design

---

## Challenges Overcome

Firebase is Excellent. But it has a few limitations.
1. You cannot store a document value as a data object. So to get around that I stored the document values to Json.
2. You cannot just add an entry to a document. You first have to get the document as a Hashmap, update the hashmap, and then push the updated hashmap as the document (that's 2 network calls instead of 1). So I had to do those when adding to favourites, and when removing from favourites 

My first idea was to over-engineer the whole thing 😅 I made a <a href="https://x.com/FemiOkedeyi/status/1753339199724351837?s=09" target="_blank" rel="nofollow noopener noreferrer" aria-label="Thread in X"> <u>Thread in X</u> </a> about it. You can check it out but don't forget to like and follow ☺️

---

## Future improvements

Future improvements will probably just be to use a custom database so that getting and updating favourites will just be a single network call.

I should also probably call more APIs to display more details about movies and tv, like credits, video trailer, and add ratings

---

## Before You Run

### Setting The Api Key

* Get an API key from <a href="https://developer.themoviedb.org/docs/getting-started" target="_blank" rel="nofollow noopener noreferrer" aria-label="TMDB's website"> <u>TMDB's Website.</u> </a>
* In the project's root directory, Create a file named apikey.properties
* Then in the apikey.properties file, set the api key as below

```
TMDB_API_KEY="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
```

### Handling The Keystore Properties

Now you have 2 options here:

1. Option 1 - If you want to be able to get release build variant

   * In Android Studio, create a new keystore file under Build > Genetare Signes Bundle/APK
   * In the project's root directory, Create a file named keystore.properties
   * Then in the keystore.properties file, set the key alias, key password, store password, and store file path as below

    ```
    KEY_ALIAS=xxxx
    KEY_PASSWORD=xxxxxxxx
    STORE_PASSWORD=xxxxxxxx
    STORE_FILE=C\:\\xxxx\\xxxx\\xxxx\\xxxx.jks
    ```

2. Option 2 - If you're okay with debug variant

    If you don't want to go through all that stress,
    In the Module's build.gradle file, just comment out the following lines of code
    
    ```
    val keystorePropertiesFile: File = rootProject.file("keystore.properties")
    val keystoreProperties = Properties()
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
    ```
    
    Also
    
    ```
    signingConfigs {
            create("release") {
                keyAlias = keystoreProperties.getProperty("KEY_ALIAS")
                keyPassword = keystoreProperties.getProperty("KEY_PASSWORD")
                storePassword = keystoreProperties.getProperty("STORE_PASSWORD")
                storeFile = file(keystoreProperties.getProperty("STORE_FILE"))
            }
        }
    ```
    
    And Finally
    
    ```
    buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
    
                signingConfig = signingConfigs.getByName("release")
            }
        }
    ```

    Just make sure the build variant in android studio is set to debug and you're done.


---

## License

[Apache License](LICENSE)