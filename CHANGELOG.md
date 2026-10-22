# Changelog

## Unreleased - GridBattle Foundation

### Added
1. **CI/CD pipeline**: lint and format, unit and integration tests with coverage, the Maven and
   Gradle builds, the Docker image, a security scan, artifacts, and a release with the JARs on a
   merge to `main`.
2. **Three-module build**: `shared` holds the protocol the client and the server speak, `client`
   holds the desktop game, and `server` holds the Spring Boot service.
3. **Authentication**: registration, sign in, JSON Web Token sessions and bcrypt password
   hashing, with a deny-by-default token check.
4. **Tests**: 65 tests across the game logic, the board state, the shared protocol, the
   credential primitives, the tokens, the authentication rules and the endpoints.

### Changed
1. **Java 17**: the build now targets Java 17, which is what the current Checkstyle and
   Spotless releases need to run at all.
2. **Resource loading**: the resources load from the classpath, so the game finds them from an
   IDE, from a JAR and from a container alike.

## Version 1.0.0 - Professional Refactor

### Issues Fixed
1. **Restart Logic Bug**: Fixed score display not updating correctly after restart
2. **Memory Leak**: Audio clips now properly close after playing
3. **Invalid Move Handling**: Improved validation for dot selection and line drawing
4. **Resource Path Issues**: Implemented proper resource loading with fallback support
5. **Magic Numbers**: Replaced hardcoded values with named constants

### Optimizations
1. **Code Organization**: Restructured into proper MVC architecture
   - `model/` - Game state management
   - `logic/` - Core game algorithms
   - `ui/` - User interface components
   - `util/` - Helper utilities
   - `constants/` - Configuration values

2. **Performance Improvements**:
   - Reduced redundant UI updates
   - Optimized square detection algorithm
   - Better memory management with proper resource cleanup

3. **Code Quality**:
   - Eliminated code duplication
   - Improved naming conventions
   - Added comprehensive documentation
   - Separated concerns for better maintainability

### New Features
1. **Professional Project Structure**: Maven and Gradle support
2. **Resource Management**: Centralized resource loading
3. **Better Error Handling**: Graceful handling of missing resources
4. **Enhanced UI**: Improved visual feedback and cursor changes
5. **Build Automation**: Easy compilation and packaging

### Architecture Changes
- **GameState**: Manages all game state (board, scores, turns)
- **GameLogic**: Handles move validation and square detection
- **StartFrame**: Welcome screen with clean separation
- **GameFrame**: Main game UI with improved organization
- **AudioPlayer**: Thread-safe audio playback
- **ResourceLoader**: Centralized resource management
- **GameConstants**: All configuration in one place

### Technical Improvements
- Java 8+ compatibility
- Thread-safe audio playback
- Proper exception handling
- Clean separation of concerns
- Comprehensive documentation
- Professional build configuration
