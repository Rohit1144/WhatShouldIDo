# What Should I Do – AI-Powered Productivity App

### Group & Members
- Group 4 from FIT5046 – Monash University
- Project for **Assessment 3 & 4: Android App Proposal & Development**
- Hyung Yeon Ji (34650431) - yjh.yen90@gmail.com / yhyu0001@student.monash.edu
- Rohit Chowdary Kanagala (34669167) - rohitchowdary1144@gmail.com / rkan0046@student.monash.edu
- Yiheng Cai (34531181) - kyrda@yahoo.gr / kdar0010@student.monash.edu
- Kyriaki Darioti (34339736) - aussieyeehern@gmail.com / ycai0085@student.moansh.edu

---

**"What Should I Do"** is a mobile-based productivity application designed to help users efficiently manage daily tasks through AI-powered suggestions and intuitive task organization. Built using Kotlin and Jetpack Compose, this Android app integrates task recommendations, and visual progress tracking to reduce decision fatigue and improve day-to-day productivity.

---

### Project Overview

This app uses **Google's Gemini Nano AI model** , **Room DB** and **Firebase Cloud Services** to deliver personalized, proactive, and reactive task recommendations both online and offline. Users can interact with a minimalistic interface to manage tasks, and track their progress through insightful reports.

---
### Project Design

Refer to https://drive.google.com/drive/folders/1SXxe_7o91yQ9zX8VB3Y5OlVajYjKYbM-?usp=drive_link

---

### Key Features

- **Secure Authentication**
    - Google sign-in via Firebase Authentication
    - Email/Password sign-in via Firebase Authentication
    - Secure update password via Firebase Authentication

- **Task Management**
    - Add, edit, delete (archive), complete, or cancel tasks (CRUD functionality)
    - Tasks stored securely using Firestore
    - Real time due date

- **AI-Powered Recommendations**
    - **Reactive**: Users can ask, *"What should I do now?"*, and receive intelligent recommendation of task to finish

- **Progress Reporting**
    - Visualize overall completed, pending, and cancelled tasks using donut charts
    - Monthly report of stack bar graph visualizing completed, pending, and cancelled tasks

- **User Onboarding**
    - Questionnaire during first-time setup for personalized task suggestions

- **Session Persistent**
    - Allows user to log back in without having to log in again each session

- **Random Inspiration Quotes**
    - Inspirational Quotes from public API (Zen Quotes) via Retrofit
    - Save favorite quotes via Room DB

- **Data Management**
    - Factory Reset to permanently delete tasks and quotes data
    - User delete to permanently delete account data

---

### Tech Stack

| Area             | Technology                           |
|------------------|--------------------------------------|
| Language & UI    | Kotlin + Jetpack Compose             |
| Backend Services | Google Firebase, Room DB (Auth & DB) |
| AI Integration   | Locally stored Gemma 2B 4int         |
| Report Analysis  | MPAndroidChart - Donut / Bar         |

---

### Git Command Quick Guide

Initial Setup (Clone & Branching)
`git clone <https://username:token@repo-url>`

Create and switch to your personal branch
`git checkout -b <personal branch>`

Push your new branch to GitHub
`git push -u origin <personal branch>`

Check current status
`git status`

Pull latest updates from main branch (to stay sync to latest)
`git checkout main`
`git pull origin main`

Merge master into your personal branch (to resolve conflicts)
`git checkout <personal branch>`
`git merge main`

Stage and commit your work 
`git add .`
`git commit -m "write comments inside the quotation"`

Push your changes to your personal branch 
`git push origin <personal branch>`

Merging to main (After your work is finished)
`git checkout main` 
`git pull origin main`
`git merge <personal branch>`
`git push origin main`

Undo local uncommitted changes (soft reset)
`git restore .`

Undo the last commit (keep changes in working directory)
`git reset --soft Head~1`

Hard reset to a previous commit (discards any changes made)
`git reset --hard <commit-hash>`

Revert a specific commit (RECOMMENDED: safest way)
`git revert <commit-hash>`

Check commit history
`git log --oneline`

List of branches
`git branch -a`

Switch between branches
`git checkout <branch name>`

Delete a local branch
`git branch -d <branch name>`

---

### Manual Pull Request & Main Branch Protection Policy

Due to branch protection rules are not enforced on private organization repositories under GitHub Free, our team follows this manual process to maintain code quality and project safety:

1. All development must happen on personal branches (e.g., `yeonji`, `kyriaki`, `rohit`, `yiheng`)
2. No one should push directly to the `main` branch
3. To merge code into `main`, team members must:
    - Open a **Pull Request**
    - Let **CI/CD (Android CI)** complete and pass
    - Get at least **1 team member to approve** the Pull Request
4. After all checks pass, the Pull Request can be merged using **GitHub's UI**.

This approach simulates GitHub's team-protected workflow without requiring a paid plan.

---

### Git Commit Message Guidelines

- "start: [section name] - [additional comments]" (when starting the section)
- "wip: [section name] - [additional comments]" (optional work-in-progress commit)
- "fix: [section name] - [additional comments]" (any fixes or intermediate commits)
- "end: [section name] - [additional comments]" (when finished with the section)

---

### Configuration and Setup Guide to Test Locally
0. Pixel 7 Pro (internal storage 6GB) recommended

1. Google Auth Sign in
- Download google-services.json : https://drive.google.com/file/d/1yoVVEhhTGY4LZIVSSrTgqN-UILhCRmWi/view?usp=sharing
- Inside Emulator Settings > Passwords, passkeys and account > Add Google Account
- Store provided google-services.json below app/ directory

2. Gemma AI Model 
- Download Gemma 2B int4 : https://drive.google.com/file/d/1T5N4wVRK6S_DEtirn2CdnTxSUy35ZoKN/view?usp=sharing
- Mac: use brew to install ADB
  - `brew install android-platform-tools`
- Windows: 
  - Search "Environment Variables" in Windows search bar
  - Select "Edit system environment variables"
  - Click "Environment Variables" button
  - In "System Variables" section, locate "Path" Variable > "Edit"
  - Click "New" > add the path to the directory for the Platform Tools
- Mac (Terminal) & Windows (PowerShell): Use the following command to push the provided Gemma 2B int4 model
  - `adb push "<local path where AI model is stored>\gemma-2b-it-cpu-int4.bin" /sdcard/Android/data/com.example.fit5046_g4_whatshouldido/files/gemma-2b-it-cpu-int4.bin`
  - Note: Please make sure it is pushed to the following path `/sdcard/Android/data/com.example.fit5046_g4_whatshouldido/files/gemma-2b-it-cpu-int4.bin`
  - To check if it is pushed use the following command:
    - `adb shell ls /sdcard/Android/data/com.example.fit5046_g4_whatshouldido/files/`
- Please note to check log cat (filter out with 'Gemma') to make sure the model is initialised before testing the AI feature.
  - `Gemma model is initialized`
  - if it shows `model init failed` message on log cat, please use the above command provided on Terminal or Powershell to see if the model exist in the path (`/sdcard/Android/data/com.example.fit5046_g4_whatshouldido/files/gemma-2b-it-cpu-int4.bin`) properly.

### License

This project is developed for academic purposes under Monash University guidelines. Do not distribute or reproduce without permission.

---