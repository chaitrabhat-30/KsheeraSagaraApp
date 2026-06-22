# Ksheera-Sagara (Dairy Profit Tracker App)
### Daily Dairy Profit Tracker  for Karnataka Farmers

## About
Ksheera-Sagara is an Android mobile application designed for dairy farmers to track their daily milk income and expenses, and calculate their actual monthly profit or loss. 
Most dairy farmers receive a daily milk slip but never calculate their real earnings after subtracting feed, medicine, and labor costs — leading them to believe they are profitable when they may actually be at a loss.

## Problem Statement
Dairy farmers in rural Karnataka receive a daily Milk Slip showing liters sold, fat percentage, and payment received. However they never subtract their daily expenses like cattle feed, medicines, electricity and labor from this income. Because of this many farmers believe they are earning well but are actually running at a loss without realizing it.

## Solution
Ksheera-Sagara solves this by giving farmers a clear simple view of their real financial health after all costs are considered.

## Features
- 📊 Profit/Loss Dashboard with Green/Red indicator
- 🥛 Daily Milk Entry (Liters, Fat%, Amount)
- 💸 Expense Tracking (Fodder, Medical, Labor, Electricity)
- 🐄 Cow-wise Profit Analysis
- 📈 Expense Pie Chart
- 🤖 Gemini AI Cost Reduction Suggestions
- 📄 PDF Export of Monthly Report
- 🔐 Login with Profile Management
- 📱 Works fully offline using Room Database

## Tech Stack
- Language: Kotlin
- Database: Room Database (SQLite)
- AI: Gemini API
- PDF: iTextPDF
- UI: Material Design with Navigation Drawer
- Min SDK: Android 7.0 (API 24)

## How to Run the App

### Option 1 — Install APK (Easiest!)
1. Download the `app-release.apk` file
2. Transfer to your Android phone via USB or WhatsApp
3. On your phone go to **Settings → Security**
4. Enable **Install from Unknown Sources**
5. Open the APK file on your phone
6. Tap **Install**
7. Open **Ksheera-Sagara** app
8. Enter any farmer name and password: `admin123`
9. Start tracking your dairy profit!

### Option 2 — Run from Source Code
1. Install **Android Studio** on your laptop
2. Clone the repository:
