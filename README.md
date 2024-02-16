[![](https://jitpack.io/v/deskpro/messenger-sdk-android.svg)](https://jitpack.io/#deskpro/messenger-sdk-android)

<img align="right" alt="Deskpro" src="https://raw.githubusercontent.com/DeskproApps/bitrix24/master/docs/assets/deskpro-logo.svg" />

# DeskPro Android Messenger
![Messenger SDK Android CI](https://github.com/deskpro/messenger-sdk-android/workflows/Messenger%20SDK%20Android%20CI/badge.svg)

DeskPro Android Messenger is a Chat/AI/Messaging product. You can embed a “widget” directly into native app, so that enables end-users to use the product. Similar implementation for [iOS](https://github.com/deskpro/messenger-sdk-ios).

## Requirements

- minSDK 23
- View binding enabled

## Dependency installation

- For detailed instructions about getting Git project into your build, check [here](https://jitpack.io/#deskpro/messenger-sdk-android).
- Open the build.gradle file for the app module and add the following lines:

```
android {
    ...
    buildFeatures {
        viewBinding = true
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

dependencies {
    ...
    implementation("com.github.deskpro:messenger-sdk-android:Tag")
}
```

## Manual installation

[Download](https://jitpack.io/com/github/deskpro/messenger-sdk-android/0.0.3/messenger-sdk-android-0.0.3.aar) latest .aar file

- Open the build.gradle file for the app module and add the following lines:

```
android {
    ...
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    ...
    implementation(files("libs/messenger-sdk-android-Tag.aar")
}
```

## Initialization

```
val messengerConfig =
    MessengerConfig(appUrl: "YOUR_APP_URL", appId: "YOUR_APP_ID")
```
Replace `YOUR_APP_URL` and `YOUR_APP_ID` with your app's URL and ID.
```
val messenger = DeskPro(applicationContext, messengerConfig)
```
To open a Messenger, paste this line example in the desired place:
```
messenger.present().show()
```
Note: You can create multiple Messenger instances.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/deskpro/messenger-sdk-android/tags).

