# Smart Pantry Manager

A native Java Android Application designed to reduce household food waste by tracking available ingredients and dynamically suggesting recipes that can be prepared using strictly existing inventory.

## 🚀 Features
- **Pantry Management (CRUD):** Add, view, edit, and delete ingredients with quantities, units, and expiry tracking.
- **Strict-Matching Engine:** Advanced matching logic filtering recipes based on current stock levels, handling plural variants without using external location or GPS SDKs.
- **Dynamic lists:** Utilizes optimized RecyclerViews with Custom Adapters for lightning-fast rendering.
- **Data Persistence:** Implemented via localized SQLite storage surviving application restarts.

## 🛠️ Architecture & Tech Stack
- **Language:** Java (JDK 11)
- **IDE:** Android Studio
- **Storage:** SQLite (via SQLiteOpenHelper)
- **UI Components:** ConstraintLayout, RecyclerView, CardView, BottomNavigationView

## 💻 Setup Instructions
1. Clone the repository: `git clone https://github.com/LwaneleMayimele/-Smart-Pantry-Manager.git`
2. Open the project folder in Android Studio.
3. Sync the Gradle project files.
4. Run the application on an Android Virtual Device (AVD) or a physical device running API 26 or higher.
