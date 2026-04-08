# Make Square Game

A classic two-player dots and boxes game built with Java Swing. Players take turns connecting dots to form squares and earn points.

## Game Rules

- Two players take turns connecting adjacent dots with lines
- When a player completes a square, they earn a point and get another turn
- The game ends when all 24 squares are completed
- The player with the most squares wins

## Requirements

- Java Development Kit (JDK) 8 or higher
- Maven 3.6+ or Gradle 6.0+ (for building from source)

## Download

### Pre-built JAR
Download the latest release from the [Releases](https://github.com/Kaoserahamed/Kaoserahamed/releases) page.

### Build from Source

**Using Maven:**
```bash
git clone https://github.com/Kaoserahamed/Kaoserahamed.git
cd Kaoserahamed
mvn clean package
```

**Using Gradle:**
```bash
git clone https://github.com/Kaoserahamed/Kaoserahamed.git
cd Kaoserahamed
gradle build
```

## Running the Game

### From JAR file:
```bash
java -jar make-square-game-1.0.0.jar
```

### From source with Maven:
```bash
mvn exec:java -Dexec.mainClass="com.makesquare.Main"
```

### From source with Gradle:
```bash
gradle run
```

### Direct compilation:
```bash
# Compile
javac -d bin -sourcepath src/main/java src/main/java/com/makesquare/Main.java

# Run
java -cp bin com.makesquare.Main
```

## Project Structure

```
make-square-game/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── makesquare/
│       │           ├── Main.java                    # Entry point
│       │           ├── constants/
│       │           │   └── GameConstants.java       # Game configuration
│       │           ├── model/
│       │           │   └── GameState.java           # Game state management
│       │           ├── logic/
│       │           │   └── GameLogic.java           # Core game logic
│       │           ├── ui/
│       │           │   ├── StartFrame.java          # Welcome screen
│       │           │   └── GameFrame.java           # Main game window
│       │           └── util/
│       │               ├── AudioPlayer.java         # Sound effects
│       │               └── ResourceLoader.java      # Resource management
│       └── resources/                               # Game assets (images, sounds)
├── pom.xml                                          # Maven configuration
├── build.gradle                                     # Gradle configuration
└── README.md                                        # This file
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

- **Language:** Java 8+
- **GUI Framework:** Swing
- **Build Tools:** Maven / Gradle
- **Architecture:** MVC pattern with separated concerns

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
