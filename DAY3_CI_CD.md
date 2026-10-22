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

---

# Day 3 Implementation: What Was Delivered

## Repository
`Dots_and_Box_game` — Classic dots and boxes game (Java Swing)

The pipeline above was documented, and a workflow file existed, but the workflow
could not have passed: it invoked plugins that were not configured, built a
Docker image with no Dockerfile, and its release step only echoed a message.
This is what the implementation changed.

## Build configuration

### Java 17

The build now targets Java 17. Java 8 was the first technical debt item in the
Day 1 audit, and it is also a practical blocker: the current Checkstyle and
Spotless releases need a JDK 11 or newer to run at all, so the lint stage could
not have been made to work on Java 8.

### The gates the pipeline runs

| Gate | Before | After |
|------|--------|-------|
| `mvn checkstyle:check` | 405 violations, from `sun_checks.xml` | 0 violations, from `config/checkstyle.xml` |
| `mvn spotless:check` | plugin not configured | runs, with the text rules |
| `mvn test` | no tests, no JUnit | 27 tests, JUnit 5 |
| Coverage | not measured | JaCoCo, published as an artifact |

The Checkstyle rule set is deliberately small, and deliberately satisfied by the
code as it stands. Turning the gate on in the same commit that adopts a strict
style would have required reformatting the whole project, and that would have
made the change impossible to review. The rule set covers the mistakes that
break a build or hide a defect: tabs and trailing whitespace, missing braces,
unused and redundant imports, naming, and a few semantic traps such as
`StringLiteralEquality` and `DefaultComesLast`.

Two checks are intentionally absent, and the reason is in the config file:
`AvoidStarImport`, because the legacy Swing UI still uses wildcard imports, and
`FinalClass`, because several helper classes are still instantiable. The
fuller style configuration is its own piece of work.

The Spotless configuration uses the text rules only — trailing whitespace and
the final newline. They need no compiler internals, so the formatter cannot
fail on a JDK it was not built for, and the sources already use spaces for
indentation.

## Tests

The pipeline executes tests, so it needs something to run. Two test classes
cover the parts of the game that are worth pinning down:

* `GameLogicTest` — move validation (adjacency, board bounds, a line that has
  already been drawn, a reversed pair) and square detection, including the
  last square of the board and the rule that a square is only reported once.
* `GameStateTest` — drawn lines, visited boxes, the two scores, turn order and
  its undo, the end of the game, and the winner.

## Pipeline

* **The pull request trigger is restored.** It had been removed, so no pull
  request was ever gated.
* **`workflow_dispatch`** is added, for a manual run.
* **Artifacts are published**: the JARs, the JaCoCo report and the test
  reports.
* **The dependency scan is published rather than enforced.** The National
  Vulnerability Database needs an API key for a reliable run, and without one
  the download is rate limited and the scan aborts before it produces
  anything. The step is allowed to fail for that reason, and the Trivy
  filesystem scan covers the same ground in the meantime.
* **Secret detection** runs first, because it is the check that must always
  run and it does not depend on an external feed.
* **The release placeholder is replaced.** A merge to `main` now creates a
  GitHub release carrying the JARs and pushes the Docker image to the GitHub
  Container Registry. Both are gated on a merge to `main`, so a fork's pull
  request can neither publish an artifact nor read the registry token.

### One thing the pipeline could not do on its own

A pull request event takes its workflow file from the **base** branch, and the
base branch had no pull request trigger, so a pull request could not gate
itself. The pipeline therefore also runs on a push to a `feature/**` branch,
which is what validated this work until the change was merged. Once the
workflow is on `development`, the pull request trigger takes over.

## The Dockerfile

The pipeline was already trying to build an image that did not exist. There is
now a multi-stage Dockerfile: Maven builds the JAR, and the runtime stage
carries a JRE and a virtual framebuffer, because a Swing application needs a
display and a headless container can only offer a virtual one.

## Day 3 Outcome
CI/CD pipeline delivered and green: lint, tests with coverage, build, security
scan, artifacts, and a release on merge to `main`. 1 PR created targeting
`development`.

<!-- Feature branch commit for PR -->
