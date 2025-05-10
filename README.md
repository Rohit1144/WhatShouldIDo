# What Should I Do – AI-Powered Productivity App

### Group & Members
- Group 4 from FIT5046 – Monash University
- Project for **Assessment 3 & 4: Android App Proposal & Development**
- Hyung Yeon Ji (34650431)
- Rohit Chowdary Kanagala (34669167)
- Yiheng Cai (34531181)
- Kyriaki Darioti (34339736)

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

### Tech Stack

| Area               | Technology                           |
|--------------------|--------------------------------------|
| Language & UI      | Kotlin + Jetpack Compose             |
| Backend Services   | Google Firebase, Room DB (Auth & DB) |
| AI Integration     | TensorFlow Lite + Gemini Nano        |
| Visualization      | Compose Charts / TBD                 |

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

### License

This project is developed for academic purposes under Monash University guidelines. Do not distribute or reproduce without permission.

---