Mini CRM Android App
This repository contains the Android application for a Mini Customer Relationship Management (CRM) system. The app allows users to manage customers and their orders, with seamless data synchronization between a local offline database and a cloud-based backend.

🌟 Features
Implemented Features:
Firebase Authentication: Secure user sign-up and sign-in functionality.

Customer Management (CRUD): Add, view, update, and delete customer information.

Order Management (CRUD): Create, view, update, and delete orders associated with specific customers.

Local Database (Room): All data is stored locally in a Room database, allowing for full offline functionality. Any changes made while offline are saved to the device.

Cloud Sync (Firebase Firestore): Data is synchronized in real-time between the local Room database and Firebase Firestore. Changes made offline are automatically pushed to the cloud once the device reconnects.

REST API Integration: Fetch random company data from a public API (dummyjson.com) to quickly populate the "Add Customer" form.

🛠️ Technologies & Libraries Used
Language: Kotlin

Architecture: MVVM (ViewModel, LiveData, Repository)

UI: XML with Material Design Components

Database: Room Persistence Library

Backend & Sync: Firebase Firestore, Firebase Authentication

Networking: Retrofit 2, Gson

Asynchronous Programming: Coroutines

🚀 Setup and Installation
To get the Android CRM app running on your local machine, follow these steps.

Prerequisites
Android Studio (latest version recommended)

A physical Android device or emulator running API level 21 or higher

Steps
1. Clone the Repository

git clone <your-repository-url>
cd android-crm

2. Set up Firebase

Go to the Firebase Console and create a new project.

Add a new Android app to your Firebase project with the package name com.example.androidcrm.

Follow the setup instructions to download the google-services.json file.

Place the downloaded google-services.json file into the android-crm/app/ directory.

In the Firebase Console, enable the following services:

Authentication: Enable the "Email/Password" sign-in method.

Firestore Database: Create a new Firestore database and start in "test mode" for initial setup.

3. Build and Run the App

Open the android-crm project in Android Studio.

Let Android Studio sync the Gradle files.

Build and run the application on your emulator or physical device.

📱 How to Use the App
Sign Up / Sign In: Launch the app and create a new account or sign in with an existing one.

Customer List: After logging in, you will see a list of all customers. You can add a new customer by tapping the floating action button.

Add/Edit Customer:

To add a new customer, fill in the details manually.

Alternatively, use the "Fetch Random Company Data" button to populate the form with data from a live API.

Tap the save icon in the toolbar.

View Orders: Tap on any customer in the list to view their details and a list of their associated orders.

Offline Mode: The app is fully functional without an internet connection. Any changes you make will be saved locally and will sync to the cloud automatically when you go online.

