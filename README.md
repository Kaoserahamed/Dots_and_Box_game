# Make Square Game

A classic two-player dots and boxes game built with Java Swing. Players take turns connecting dots to form squares and earn points.

## Game Rules

- Two players take turns connecting adjacent dots with lines
- When a player completes a square, they earn a point and get another turn
- The game ends when all 24 squares are completed
- The player with the most squares wins

## Requirements

- Java Development Kit (JDK) 17 or higher
- Maven 3.6+ or Gradle 8.0+ (for building from source)

## Download

### Pre-built JAR
Download the latest release from the [Releases](https://github.com/Kaoserahamed/Dots_and_Box_game/releases) page.

### Build from Source

The build is split into three modules, and the two build tools mirror each other exactly.

**Using Maven:**
```bash
git clone https://github.com/Kaoserahamed/Dots_and_Box_game.git
cd Dots_and_Box_game
mvn clean package
```

**Using Gradle:**
```bash
git clone https://github.com/Kaoserahamed/Dots_and_Box_game.git
cd Dots_and_Box_game
gradle build
```

## Running the Game

### From JAR file:
```bash
java -jar client/target/make-square-game-1.0.0-jar-with-dependencies.jar
```

### From source with Maven:
```bash
mvn -pl client exec:java -Dexec.mainClass="com.makesquare.Main"
```

### From source with Gradle:
```bash
gradle :client:run
```

### Running the server

The server is a separate Spring Boot service. It needs a signing secret for the session tokens,
and it must be at least 32 bytes long.

```bash
export AUTH_JWT_SECRET="a-secret-of-at-least-32-bytes-for-local-use"
mvn -pl server spring-boot:run
```

## Project Structure

```
Dots_and_Box_game/
├── shared/                                          # The protocol the client and server speak
│   └── src/main/java/com/makesquare/shared/protocol/
│       ├── MessageType.java                        # The message vocabulary
│       └── MoveMessage.java                        # One move
├── client/                                          # The desktop game
│   ├── src/main/java/com/makesquare/
│   │   ├── Main.java                               # Entry point
│   │   ├── constants/GameConstants.java            # Game configuration
│   │   ├── model/GameState.java                    # Game state management
│   │   ├── logic/GameLogic.java                    # Core game logic
│   │   ├── ui/                                     # StartFrame, GameFrame
│   │   └── util/                                   # AudioPlayer, ResourceLoader
│   └── src/main/resources/                         # Game assets (images, sounds)
├── server/                                          # The Spring Boot service
│   ├── src/main/java/com/makesquare/server/
│   │   ├── ServerApplication.java                  # Entry point
│   │   ├── StatusController.java                   # Service status
│   │   └── auth/                                   # Registration, sign in, JWT sessions
│   └── src/main/resources/application.yml          # Server configuration
├── config/checkstyle.xml                           # The style rules the pipeline enforces
├── pom.xml                                          # Maven build, an aggregator over the modules
├── settings.gradle                                  # Gradle build, mirroring the Maven modules
└── build.gradle
```

## Features

- Clean, intuitive graphical interface
- Visual feedback for player turns
- Score tracking
- Sound effects
- Game restart functionality
- Professional code architecture with separation of concerns

## Controls

- Click on dots to select them
- Click on two adjacent dots to draw a line
- Use the RESTART button to start a new game
- Use the EXIT button to close the game

## Technical Details

- **Language:** Java 17
- **GUI Framework:** Swing (JavaFX is the target for the client module)
- **Server:** Spring Boot 3, with JSON Web Token sessions and bcrypt password hashing
- **Build Tools:** Maven / Gradle, over the same three modules
- **Architecture:** three modules - a shared protocol, a client and a server
- **Quality:** Checkstyle and Spotless in the pipeline, JUnit 5 with JaCoCo coverage

## Version History

### Version 1.0.0
- Initial professional release
- Refactored codebase with clean architecture
- Improved game logic and bug fixes
- Added proper resource management
- Enhanced UI/UX

## License

This project is open source and available for educational purposes.

## Contributors

- Original Game Design
- Refactored and Enhanced by Professional Development Team

## Support

For issues, questions, or contributions, please visit the [GitHub repository](https://github.com/Kaoserahamed/Kaoserahamed).
