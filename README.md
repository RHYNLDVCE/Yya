# Yya

Yya is an interactive, locally hosted quiz application built using Kotlin Multiplatform. The system is designed to facilitate live, classroom-style quizzes where an administrator hosts a session and participants join in real-time.

## Architecture and Platforms

The project consists of two primary applications:

1. **Desktop Application (Admin)**
   - Built using Compose for Desktop.
   - Allows administrators to create quizzes, manage question banks, and monitor student participation.
   - Hosts a local Ktor WebSocket server to broadcast questions and receive incoming answers.
   - Generates a QR Code encoding the necessary connection details for seamless participant onboarding.
   - Displays a live, real-time leaderboard as participants submit their answers.

2. **Android Application (Student)**
   - Built using Jetpack Compose.
   - Participants scan the generated QR Code using the integrated ZXing barcode scanner to connect to the local server.
   - Receives active questions pushed by the administrator in real-time.
   - Records response times and submits answers back to the server for evaluation.

## Core Functionalities

- **Real-Time Communication:** Instantaneous, low-latency synchronization between the Admin server and Student clients utilizing Ktor WebSockets.
- **Automated Client Provisioning:** Users can join active sessions seamlessly without manual IP configuration, powered by embedded QR code scanning.
- **Local Persistence:** Powered by SQLDelight to store quizzes, question banks, participant data, and historical leaderboards safely and locally without relying on external cloud databases.

## Technology Stack

- **Kotlin Multiplatform:** For sharing domain logic and networking code between the Android and JVM Desktop environments.
- **Compose Multiplatform:** Modern declarative UI framework.
- **Ktor:** Framework for asynchronous server and client WebSocket communication.
- **SQLDelight:** Type-safe SQL database driver for local persistence.
- **Koin:** Dependency injection framework.
- **ZXing:** Embedded barcode generation and scanning.

## Getting Started

### Prerequisites
- JDK 17 or higher
- Android Studio or IntelliJ IDEA
- An Android device or emulator for the Student application

### Running the Desktop Admin Application
To start the administrative server and manage quizzes, run the following Gradle task from the project root:

```bash
./gradlew :desktopApp:run
```

### Running the Android Student Application
To install and run the student application on an attached Android device or running emulator, execute:

```bash
./gradlew :androidApp:installDebug
```

## Usage Instructions

1. Launch the Desktop application on the host machine.
2. Ensure the host machine and the participating Android devices are connected to the same Local Area Network (LAN).
3. In the Desktop application, create a new quiz or select an existing one, then initiate the session.
4. Open the Android application on a participating device and tap the scan button.
5. Scan the QR code displayed on the Desktop application screen.
6. Once connected, wait for the administrator to broadcast the next question.