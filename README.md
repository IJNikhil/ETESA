# Project: ETESA

## Purpose
ETESA is a real-time attendance taking Android application developed in Java using Firebase. It aims to securely store daily attendance records of students with a user-friendly interface.

## Project Overview

### Technology Stack
- **Language**: Java
- **Database & Authentication**: Firebase

### Features
- **Firebase Authentication**:
  - Allows faculty members to log in using email and password.
  - Admin console to manage faculty accounts.

- **Firebase Firestore**:
  - Stores daily attendance records for each subject or lecture dynamically.
  - Facilitates real-time updates and secure storage of student attendance data.

## Installation Steps

### 1. Clone the Repository
```bash
git clone https://github.com/IJNikhil/ETESA.git
cd ETESA/
```

### 2. Set Up Firebase Project
- Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/).
- Add an Android app to your Firebase project using package name `com.etesa.app`.
- Download the `google-services.json` file and place it in the `app/` directory of your Android project.

### 3. Configure Firebase Dependencies
- Open `build.gradle` (Project Level) and add the Google Services classpath:
  ```groovy
  classpath 'com.google.gms:google-services:4.3.10'
  ```
- Open `build.gradle` (App Level) and apply the Google Services plugin at the bottom:
  ```groovy
  apply plugin: 'com.google.gms.google-services'
  ```

### 4. Run the Application
- Connect your Android device or use an emulator with minimum SDK version 28.
- Build and run the application from Android Studio.

## Required Files & Setup

### Project Structure
- **Files**: Ensure the following files are correctly set up and configured:
  - `google-services.json`: Firebase configuration file for Android.
  - `build.gradle` (Project Level): Ensure `google-services` classpath is included.
  - `build.gradle` (App Level): Apply `google-services` plugin and configure dependencies.

## Dependencies

### Android Dependencies
- **AndroidX Libraries**:
  ```groovy
  implementation 'androidx.appcompat:appcompat:1.6.1'
  implementation 'com.google.android.material:material:1.10.0'
  implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
  ```

- **Firebase SDKs**:
  ```groovy
  implementation 'com.google.firebase:firebase-auth:20.0.1'
  implementation 'com.google.firebase:firebase-firestore:24.9.1'
  implementation platform('com.google.firebase:firebase-bom:32.6.0')
  implementation 'com.google.firebase:firebase-analytics:21.5.0'
  ```

## Conclusion
ETESA simplifies attendance management for educational institutions by leveraging Firebase for real-time data handling and authentication. Follow the steps above to set up and deploy the application on your development environment.
```

This Markdown file provides a structured guide for installing, setting up dependencies, and understanding the purpose and features of the "ETESA" project. It's designed to be clear and understandable for anyone looking to work with or deploy the application. Adjustments can be made based on specific details of your project setup and requirements.