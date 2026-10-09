# Learning Dashboard Mobile App

## 1) Architecture
I used a simple MVVM setup:
UI (`Fragment`) → `ViewModel` → `Repository` → mock API/local cache.

This keeps screen logic out of the UI, makes state easy to test, and keeps the data flow simple for a small app.

## 2) Offline Support
Course data is seeded from `app/src/main/assets/courses.json` on first load and then saved to an app-private cache file in internal storage.
After that, the app reads from the local cache, so previously loaded courses still appear without internet.

## 3) Security
In production, authentication tokens should be stored in the Android Keystore-backed `EncryptedSharedPreferences` or another secure key management solution.
Never store tokens in plain SharedPreferences or hardcoded in the app.

## 4) Scale
If the app had 1 million users and hundreds of courses, I would improve:
- Backend paging and pagination in the UI
- Remote caching strategy and stale-while-revalidate sync
- Better database modeling and indexing
- Background refresh / retry policies
- Analytics and crash monitoring

## 5) Second Platform
If I were building the same app on iOS/macOS, I would use SwiftUI + MVVM, `ObservableObject` view models, `NavigationStack`, a mock JSON/API service, and local persistence with Core Data or files for offline access.

## Run
Open the project in Android Studio and run the `app` module on an emulator or device.

