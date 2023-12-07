This is to show how to run the code

You will download the zip file,

Export the zip file

Open the directory where you have AndroidStudio projects

Copy, or drag, the folder to the directory

Open it in android studios

You may experience some issues which is revolving 
The project is using an incompatible version (AGP 8.2.0) of the Android Gradle plugin. Latest supported version is AGP 8.1.4

To fix this you will need to go to 
build.gradle.kts (The one containing buildscript & plugins)

go to 
id("com.android.application") version “8.2.0” apply false

and replace it with 
id("com.android.application") version "8.1.4" apply false

Now save the project and sync it just in case

To run, just click the green run button or Control R
