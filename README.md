# What Should I Do – AI-Powered Productivity App

---

## Authors

- Group 4 from FIT5046 – Monash University
- Project for **Assessment 3 & 4: Android App Proposal & Development**
- Hyung Yeon Ji (34650431)
- Rohit Chowdary Kanagala (34669167)
- Yiheng Cai (34531181)
- Kyriaki Darioti (34339736)

---

**"What Should I Do"** is a mobile-based productivity application designed to help users efficiently manage daily tasks through AI-powered suggestions and intuitive task organization. Built using Kotlin and Jetpack Compose, this Android app integrates task recommendations, and visual progress tracking to reduce decision fatigue and improve day-to-day productivity.

---

## Project Overview

This app uses **Google's Gemini Nano AI model** , **Room DB** and **Firebase Cloud Services** to deliver personalized, proactive, and reactive task recommendations both online and offline. Users can interact with a minimalistic interface to manage tasks, and track their progress through insightful reports.

---
## Project Design

Refer to https://drive.google.com/drive/folders/1SXxe_7o91yQ9zX8VB3Y5OlVajYjKYbM-?usp=drive_link

---

## Key Features

- **Secure Authentication**
    - Google sign-in via Firebase Authentication

- **Task Management**
    - Add, edit, delete, complete, or cancel tasks (CRUD functionality)
    - Tasks stored securely using Firestore

- **AI-Powered Recommendations**
    - **Reactive**: Users can ask, *"What should I do now?"*, and receive intelligent suggestions
    - **Proactive**: Notifications based on habits, time of day, or location (upcoming)

- **Progress Reporting**
    - Visualize completed, overdue, and cancelled tasks using charts and graphs

- **User Onboarding**
    - Questionnaire during first-time setup for personalized task suggestions

---

## Tech Stack

| Area               | Technology                           |
|--------------------|--------------------------------------|
| Language & UI      | Kotlin + Jetpack Compose             |
| Backend Services   | Google Firebase, Room DB (Auth & DB) |
| AI Integration     | TensorFlow Lite + Gemini Nano        |
| Visualization      | Compose Charts / TBD                 |

---

## License

This project is developed for academic purposes under Monash University guidelines. Do not distribute or reproduce without permission.

