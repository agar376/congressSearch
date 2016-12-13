Created the Congress Search App in Android using Android Studio for development and Android Studio Emulator, Genymotion and BlueStack as emulators.
All images were fetched from Google Images and copied under drawable.

The project follows an MVC framework, with all the model classes and service classes written in Java, and the UI is written in XML. The project is based entirely out of Java and MXL and no third party libraries were used. 

The project is compatible upto API level 15 until API level 25. The testing has been performed on API level 25 (Nougat) and API level 21 (Marshmallow).

Different Functionalities have been implemented as follows:
- Main Activity that controls all the Fragments of Legislators, Bills, Committees and Favourites
- About Me is a separate activity
- Legislators, Bills, Committees and Favourites have been implemented as Fragments, with Tab host to view different tabs
- The underlying data is fetched using Async calls to the AWS Congress APIs built as part of HW8
- All sorting, enrichment and modification of data is done in Java Service layer before the data is passed to XML for display
- Android CSS has been used for the UI similarity
- Favourites have been implemented using the Shared Preferences where favourite marked id is stored under different database (legislator, bill, committee) type and under each key, an array of map with key as the id and value as the corresponding JSON string is stored.

The following libraries were used to implement the functionalities:
- Android View
- Android Widget
- Android OS AsyncTask
- Android Tab Host
- Android Widget ProgressBar
- Android Widget Toast
- Android Tab Widget
- Android AppCompatActivity v7
- Java net URL
- Java Util
- Java DateUtil
- Java SimpleDataFormat