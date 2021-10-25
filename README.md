# Le_GIFaro

The Android application utilize Giphy Api to load and show Gifs: https://developers.giphy.com/docs/api/endpoint

Tech stack used:
  - MVVM with coroutines and Live Data
  - Retrofit + gson
  - Room DB to save favourites
  - Fresco for Gif processing (with image caching for offline use)
  - Espresso for UI tests
  
The app have an ability to load trending GIFs as well as search by specific query.

Each GIF can be added to favourite and then found under the Favourites tab. 

The app use ConnectivityManager to improve user experience with unstable internet connection.

Both dark and light themes supported.

UI tests covers the main application workflows.
