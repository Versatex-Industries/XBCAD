SchoolSync 

SUBMISSION TEXT:
•	ALL Documentation & Source Code will be submitted to summative project link by one group member 
•	Self-Evaluation and Peer Evaluation will be submitted to Self-Evaluation report link by every group member.

What the Project Does?

SchoolSync is an Android application that facilitates seamless communication and scheduling for schools. The ViewTimetableActivity feature of the app allows users to view their class timetables based on their roleâ€”teachers and students. Teachers can manage and view timetables for the classes they are responsible for, while students can view the timetables of the classes they are enrolled in. The app ensures role-specific functionality, offering a personalized and efficient scheduling experience.

Why the Project Is Useful?

Timetable Accessibility: Students and teachers can easily view and manage their schedules from a single platform.
Role-based Experience: The app differentiates between the needs of teachers and students, displaying appropriate timetables based on their roles.
Real-time Timetable Updates: Teachers can manage class schedules and students can access the latest information in real-time, ensuring that no one misses critical updates.
Streamlined Workflow: Teachers can select their classes and students automatically receive the correct timetable, making scheduling easy and efficient for all users.
Features
Dynamic Timetable Display: Displays timetables for both students and teachers.
Role-based Timetable Access: The app automatically adjusts the displayed timetable based on whether the user is a teacher or a student.
Class Selection for Teachers: Teachers can select the class they want to view, with the first available class selected by default.
Error Handling: If there is an issue fetching data (e.g., user profile, timetable), the app will show appropriate error messages via Toast notifications.

Requirements:
Android 5.0 (Lollipop) or higher
Internet connection (for API calls)
Retrofit for API handling
Kotlin programming language
Coroutines for asynchronous tasks


Installation
Clone the repository:

bash
Copy code
git clone https://github.com/yourusername/schoolsync.git
Open the project in Android Studio.

Build and run the app on an Android device or emulator.

How Users Can Get Started with the Project
To get started with SchoolSync and begin viewing timetables:

Download the App: set it up locally in Android Studio.

Sign In: Upon first launch, sign in with your credentials. If you're a teacher, your class management options will be available. If you're a student, your assigned class timetable will be automatically displayed.

View Timetable: After signing in, the app will automatically fetch your timetable. Teachers can choose from the classes they manage, while students will see their assigned class timetable.

Stay Updated: The app will automatically update your timetable as needed. Any changes will be reflected in real-time.

File Structure
ViewTimetableActivity.kt: The activity where the timetable is fetched and displayed.
ApiService: Interface for the Retrofit API calls.
RetrofitClient: Retrofit client setup to handle API communication.
TimetableResponse: Data model representing a timetable entry.
UserProfileResponse: Data model representing the user's profile (used to determine role and class).
API Endpoints Used
GET /api/user/profile: Fetches the user's profile, which includes their role (Teacher/Student) and classId.
GET /api/classes: Fetches the list of classes managed by the teacher (if the user is a teacher).
GET /api/timetable/{classId}: Fetches the timetable for a specific class using the classId.
Code Explanation
ViewTimetableActivity
This activity is responsible for managing the display of the user's timetable based on their role.

Fetching the User Profile: The user's profile is fetched using the getProfile() API call, which returns the role and classId. If the user is a teacher, the first class they manage is selected by default; otherwise, the student's assigned classId is used.

Loading the Timetable: Once the classId is determined, the app makes a network call to fetch the timetable for that class using the getTimetableForClass() API call. The timetable data is then dynamically added to the UI.

Displaying the Timetable: Each timetable entry is displayed using a custom layout (item_timetable), where the day, period, subject, and teacher's name are shown.

Important Methods
fetchUserRoleAndLoadTimetable(): Fetches the user's profile and determines whether they are a teacher or a student, then loads the appropriate timetable.

fetchFirstClassIdForTeacher(): Fetches the list of classes for a teacher and returns the first classId. This is used when the user is a teacher.

loadTimetable(classId: String): Fetches the timetable data for the given classId and updates the UI with the results.

addTimetableEntryToView(entry: TimetableResponse): Dynamically adds a timetable entry to the UI using the item_timetable.xml layout.

showError(message: String): Displays a Toast with the error message if an API call fails or if there's an issue fetching the data.

Dependencies:
Retrofit: For network requests and API calls.
Coroutines: For background operations (asynchronous tasks).
Gson: For JSON parsing in Retrofit.

Contributing:
Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes.
Commit your changes (git commit -am 'Add feature').
Push to the branch (git push origin feature-branch).
Create a new Pull Request.

https://github.com/Versatex-Industries/XBCAD.git

https://github.com/Versatex-Industries/xbcad-backend-rewrite.git


Application - https://youtu.be/dqzw3HJ_PKo


