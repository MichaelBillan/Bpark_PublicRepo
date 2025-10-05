# BPark – Automated Parking Management System

## Overview
BPark is an academic semester project developed as part of the Software Systems Engineering course at Braude College of Engineering.  
The system provides an automated parking management solution designed to streamline vehicle entry, parking, and exit processes for college users, while maintaining a robust backend for subscription and data management.

## Project Purpose
BPark was created to address the parking shortage problem on campus by introducing an automated, computer-based system.  
The system’s main objectives are:
- Efficient vehicle deposit and retrieval.
- Management of subscriber accounts and their parking activity.
- Reservation of parking spots in advance.
- Real-time availability monitoring and time extensions for parking.
- Data management and reporting for system administrators.

## System Features

### User Management
- Initial registration for new subscribers.
- Unique subscription code generation for each user.
- Management of personal data (name, email, phone number).
- History of parking sessions.

### Vehicle Deposit & Retrieval
- Subscribers can log in through a terminal or tag reader at the parking entrance.
- If a parking spot is available, the system assigns a unique parking code.
- Vehicles are placed on a designated conveyor system for storage.
- When retrieving a car, the subscriber re-enters their parking code to have the vehicle delivered automatically.
- “Lost code” feature sends the code to the user’s email/SMS.
- Notifications are sent for late returns or extended parking time.

### Parking Reservation
- Users can book a parking space up to 7 days in advance, and no later than 24 hours before the desired time.
- Reservation is allowed only if at least 40% of spaces are available.
- Confirmation code is issued for successful bookings.
- Reservation expires 15 minutes after the scheduled time if the user does not arrive.

### Data Management and Reports
- Users can view their activity history and update limited personal details.
- The parking attendant can view all active and past parking sessions.
- The parking manager can view analytical reports and visual statistics, such as:
  - Average parking durations and delays.
  - Subscription statistics and parking trends.

## System Architecture

BPark follows a Full Stack Architecture, divided into:
- Frontend (Client Side): User interaction interface (terminals and desktop UI).
- Backend (Server Side): Includes the database, business logic, and server handling multiple user connections.

### Communication
- Built on a distributed operational model.
- Supports multiple workstations connected via LAN using TCP/IP.
- The tag reader functionality is simulated for testing purposes.
- Future versions will support web access (via browser) and mobile applications.

## Technologies Used
| Layer | Technologies |
|-------|---------------|
| Language | Java |
| IDE | Eclipse |
| Database | MySQL (Relational) |
| Architecture | Client-Server (OCSF Framework) |
| UI | JavaFX |

## Development Process
The project was developed according to the Software Engineering Life Cycle (SELC) principles:
1. Requirements Analysis – Understanding user roles and system goals.
2. System Design – UML modeling and database schema creation.
3. Implementation – Developing backend and frontend components.
4. Integration & Testing – Ensuring full client-server communication.
5. Presentation & Documentation – Delivering the final prototype and system demo.

## User Roles
| Role | Description |
|------|--------------|
| Subscriber | Registers, reserves parking, and retrieves cars. |
| Attendant (Usher) | Manages active parking, verifies users, and updates system status. |
| Manager | Monitors reports and oversees system operations. |

## Future Enhancements
- Web-based access via browser (Phase 2).
- Mobile application integration.
- Real-time hardware connections for RFID/tag reader devices.
- Enhanced analytics and automated billing.

## Project Information
- Course: 61756 – Software Systems Engineering Methods
- Institution: Braude College of Engineering
- Semester: Fall 2023
- Project Type: Semester Group Project
- Version: Phase 1 – Local Network Implementation


## Contributors

### Michael Billan | Team Lead and Main Backend Developer
- Designed the system architecture.
- Implemented backend functionality.
- Created and managed the database schema.


## License
This project was developed for academic and educational purposes under Braude College guidelines.  
All rights reserved © Braude College, Software Engineering Department.

