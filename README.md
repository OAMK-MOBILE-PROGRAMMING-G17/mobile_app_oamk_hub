# 📱 OAMK Hub - Mobile App

*Note: The frontend and backend are deployed using a CI/CD pipeline. Some features might take a few seconds to load if the server is waking up from sleep mode.*

A Kotlin-based mobile application designed to serve the student community at Oulu University of Applied Sciences (OAMK).  
This document covers the app features, technologies used, and instructions for running and contributing to the project.

---

## Table of Contents

- [Overview](#overview)
- [Team Members](#team-members)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation Process](#installation-process)
- [Running the Application](#running-the-application)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

OAMK Hub is a mobile platform developed to empower OAMK students, staff, and the local community.  
It offers a centralized space for news, events, lost & found services, a marketplace, and real-time communication between users.

---

## Team Members

Our team of three dedicated developers has worked tirelessly to bring this project:

-**Anil Shah**: Full Stack Developer
-**Bibek Tandon**: Full Stack Developer
-**Pabitra Kunwar**: Frontend Developer

---

## Features

- 📰 **OAMK News and Events** — Stay updated with the latest campus happenings.
- 📢 **Community Posts** — Share updates and engage with fellow students.
- 🧥 **Lost and Found** — Report or search for lost and found items.
- 🛒 **Marketplace** — Buy and sell used items within the community.
- 💬 **Real-Time Chat** — Instant messaging between users for quicker transactions.

---

## Technologies Used

**Frontend:**
- Kotlin
- Jetpack Compose
- Retrofit (API calls)

**Backend:**
- Node.js
- Express.js
- MongoDB
- Socket.IO (Real-time chat)

**Security:**
- JWT Token Authentication
- Password encryption with bcrypt
- OTP service for password recovery

**Other Tools:**
- Git & GitHub
- CI/CD pipeline for backend deployment

---

## Installation Process

1. Clone the repository:
   ```bash
   git clone https://github.com/OAMK-MOBILE-PROGRAMMING-G17/mobile_app_oamk_hub.git

    ```
2. Open the project in Android Studio.
3. Install the required dependencies:
   ```bash
   ./gradlew build
   ```
4. Set up the backend server (if not already running):
5. Navigate to the backend directory:
   ```bash
   cd backend
   ```
6. Install the required dependencies:
   ```bash
    npm install
    ```
7. Start the backend server:
8. ```bash
   npm start devStart
   ```
9. Ensure the backend server is running on `http://localhost:5000` or update the API base URL in the app's configuration.
10. Run the app on an emulator or physical device:
    - Select the desired device in Android Studio and click on the "Run" button.

---
## Running the Application
1. Open Android Studio.
2. Import the project.
3. Ensure the Android SDK is installed and configured.
4. Connect a physical device or start an emulator.
5. Click on the "Run" button in Android Studio.
6. Wait for the app to build and install on the device/emulator.
7. Once installed, open the app and start exploring its features.


## User Interface Plan

The OAMK Hub mobile application is structured into key sections:

- **Home Screen**: Displays recent posts, news, and campus events.
- **Authentication**: Secure Sign-Up and Log-In screens for user access.
- **Lost & Found**: Post or search for lost and found items across campus.
- **Marketplace**: Buy and sell used items among students with detailed listings.
- **Events Calendar**: Browse and stay updated on campus events and activities.
- **Announcements**: View official notifications and important updates from OAMK staff.
- **Forum/Discussion Board**: Engage in topic-based discussions and community chats.
- **Profile Management**: Manage user information, preferences, and activity history.
- **Emergency Contacts**: Quick access to important OAMK emergency and support contacts.

For more details, view the following resources:
- **Figma UI Design**: [Figma UI Design](#) *https://www.figma.com/design/Qw2xd2QoBAXQ7nHgqU0rCk/OAMK-Hub?node-id=52-79&p=f&t=ER8hZdAVFN4NXmS8-0*
- **Backend URL**: [Backend URL](#) *https://oamkhub-brdphqdjbbbyafag.northeurope-01.azurewebsites.net*
- **ERD Diagram**: [ERD Diagram](#) *https://unioulu-my.sharepoint.com/:w:/g/personal/t3kupa01_students_oamk_fi/ERyubn3tbl1Ii-D4zHq0OFABikLRK4t3JPQNjIt88Bl54A?e=UbUSrF*
- **

---

## License

This project is licensed under the [MIT License](LICENSE).

---
