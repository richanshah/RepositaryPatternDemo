# RepositaryPatternDemo
The application is written entirely in Kotlin.

Android Jetpack is used as an Architecture glue including but not limited to ViewModel, 
LiveData, Lifecycles, Navigation, Room and Data Binding.

The application does network HTTP requests via Retrofit, OkHttp and GSON. Loaded data is saved to SQL based database Room, which serves as single source of truth and support offline mode. Paging library is used for data pagination online and offline.

Kotlin Coroutines manage background threads with simplified code and reducing needs for callbacks. Combination of Coroutines and Kotlin build in functions (transformation, collections) are preferred over RxJava 2.

Work manager does synchronisation job being compatible with Doze Mode and using battery efficiently. Navigation component manages in-app navigation.

Dagger 2 is used for dependency injection.

Glide is used for image loading and Timber for logging.

Stetho is used to empower debugging skills (like Network calls log, Database content overview, UI Hierarchy view, etc).

A sample app consist of 3 screens: List of LEGO® themes, list of sets and set details.
