# Day 3 CI/CD: Build / Test / Deploy Pipeline

## Repository
`Dots_and_Box_game` — Classic dots and boxes game (Java Swing)

## Pipeline Overview
GitHub Actions CI/CD for Java 8+ desktop application (Maven + Gradle).

### Trigger
- Push/Pull Request to `main`
- Manual workflow dispatch

### Stages

#### 1. Lint & Format
- Checkstyle (Java code style)
- Spotless (formatting)

#### 2. Test
- Unit tests (JUnit 5) — game logic, move validation, square detection
- Integration tests — game flow end-to-end
- Coverage: JaCoCo

#### 3. Build
- Maven: `mvn clean package` (produces `make-square-game-1.0.0.jar`)
- Gradle: `gradle build`
- Assembly: `jar-with-dependencies`
- Docker image: `docker build -t dots-and-box-game .`

#### 4. Security Scan
- Dependency check (`mvn dependency:check`, Gradle dependency analysis)
- Secret detection (no API keys, no credentials)

#### 5. Deploy (on merge to `main`)
- Create GitHub Release with JAR artifact
- Build and push Docker image
- Docker Hub release

### Quality Gates
- All unit tests pass
- Build succeeds (Maven + Gradle)
- Code style compliance (Checkstyle)
- No secrets committed

### Artifacts
- JAR file (`make-square-game-1.0.0.jar`)
- JAR with dependencies (`jar-with-dependencies`)
- Docker image
- Test reports (JaCoCo XML)
- GitHub Release

## Day 3 Outcome
CI/CD pipeline documented. No code changes. 0 PRs. Ready for Day 4 (project restructuring).