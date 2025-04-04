# Spring Boot Kitchensink Application

A starter Spring Boot web application demonstrating various features including MongoDB integration, form validation, and REST services.

## Prerequisites

- Java 21 or later
- Maven 3.8 or later
- MongoDB 7.0 or later

### Installing MongoDB on macOS

1. Install MongoDB using Homebrew:
```bash
brew tap mongodb/brew
brew install mongodb-community@7.0
```

2. Start MongoDB service:
```bash
# Using Homebrew services (recommended)
brew services start mongodb-community@7.0

# Or using launchctl directly
launchctl load ~/Library/LaunchAgents/homebrew.mxcl.mongodb-community@7.0.plist
```

3. Verify MongoDB is running:
```bash
brew services list
# You should see mongodb-community@7.0 with status "started"
```

4. To stop MongoDB when needed:
```bash
brew services stop mongodb-community@7.0
```

### Installing MongoDB on other platforms

- **Windows**: Download and install from [MongoDB Download Center](https://www.mongodb.com/try/download/community)
- **Linux**: Follow the [official installation guide](https://www.mongodb.com/docs/manual/administration/install-on-linux/)

## Running the Application

1. Start MongoDB if not already running:
```bash
brew services start mongodb-community@7.0  # macOS
# or
sudo systemctl start mongod           # Linux
# or start MongoDB service on Windows
```

2. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```

3. Access the application at: http://localhost:8080/kitchensink

## Features

- MongoDB database integration
- RESTful web services
- HTML5 based UI using Thymeleaf templates
- Bean Validation
- MongoDB repositories and services

## API Endpoints

- GET /rest/members - List all members
- GET /rest/members/{id} - Get a specific member
- POST /rest/members - Create a new member
- PUT /rest/members/{id} - Update a member
- DELETE /rest/members/{id} - Delete a member

## Troubleshooting

### MongoDB Connection Issues

If you see connection refused errors:

1. Verify MongoDB is running:
```bash
# macOS/Linux
ps aux | grep mongo

# Windows
tasklist | findstr mongo
```

2. Check MongoDB logs:
```bash
# macOS
tail -f /opt/homebrew/var/log/mongodb/mongo.log

# Linux
tail -f /var/log/mongodb/mongod.log

# Windows
type "C:\Program Files\MongoDB\Server\7.0\log\mongod.log"
```

3. Ensure MongoDB is listening on default port (27017):
```bash
# macOS/Linux
lsof -i :27017

# Windows
netstat -an | findstr "27017"
```

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details. 