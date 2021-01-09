![alt tag](https://psv4.userapi.com/c856420/u543027815/docs/d1/eb89c90bbca0/aaa1.png?extra=oJqQZNKnpklrwgBHtkPO9bvcxeBmI4eERHLKW7VzCci07KtJmn1TFHe9QHqEiA8owdiHwz1v_xWbyxBLWerhaNJ3O4hnxgYsfzshL96HmbJIAy9dJLtVftrjB7i_xaIF-93lK2ofgP9xMaGk-qir_kiJ "Screenshot")
Mindustry Java Mod By Braindustry Team 

![](https://psv4.userapi.com/c856536/u543027815/docs/d16/68103b9d8180/vyfyvfyv.png?extra=JyuGfOYCY-kvsuW4f8495blCjHRUei2LdIembdofNVT8lMUWK3u5gag3nfxegcLET_sjlKOp4M6Rb7n4yVRnFAEJQ2A_NwQb5h3rdI5J5o2HB5EYHd_QqTFSUxmE8pqNirVyE3NaudFz64oyeIhAMFL0)

[Trello Board](https://trello.com/b/ZapdrgJm/braindustry)

# Build 
## Building for Desktop Testing

1. Install JDK 14. If you don't know how, look it up. If you already have any version of the JDK >= 8, that works as well. 
2. Run `gradlew jar` [1].
3. Your mod jar will be in the `build/libs` directory. **Only use this version for testing on desktop. It will not work with Android.**
To build an Android-compatible version, you need the Android SDK. You can either let Github Actions handle this, or set it up yourself. See steps below.

## Building through Github Actions

This repository is set up with Github Actions CI to automatically build the mod for you every commit. This requires a Github repository, for obvious reasons.
To get a jar file that works for every platform, do the following:
1. Make a Github repository with your mod name, and upload the contents of this repo to it. Perform any modifications necessary, then commit and push. 
2. Check the "Actions" tab on your repository page. Select the most recent commit in the list. If it completed successfully, there should be a download link under the "Artifacts" section. 
3. Click the download link (should be the name of your repo). This will download a **zipped jar** - **not** the jar file itself [2]! Unzip this file and import the jar contained within in Mindustry. This version should work both on Android and Desktop.

## Building Locally

Building locally takes more time to set up, but shouldn't be a problem if you've done Android development before.
1. Download the Android SDK, unzip it and set the `ANDROID_HOME` environment variable to its location.
2. Make sure you have API level 30 installed, as well as any recent version of build tools (e.g. 30.0.1)
3. Add a build-tools folder to your PATH. For example, if you have `30.0.1` installed, that would be `$ANDROID_HOME/build-tools/30.0.1`.
4. Run `gradlew deploy`. If you did everything correctlly, this will create a jar file in the `build/libs` directory that can be run on both Android and desktop. 

--- 

*[1]* *On Linux/Mac it's `./gradlew`, but if you're using Linux I assume you know how to run executables properly anyway.*  
*[2]: Yes, I know this is stupid. It's a Github UI limitation - while the jar itself is uploaded unzipped, there is currently no way to download it as a sing
