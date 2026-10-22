# Day 4 Project Restructuring: Separate Client and Server Modules

## Repository
`Dots_and_Box_game` — Classic dots and boxes game (Java Swing)

## What changed

The repository was a single Maven project with a flat source tree. The service boundaries in
`DAY2_ARCHITECTURE.md` — a game engine, a client and a server — existed only as a naming
convention in the imports, and the earlier marker commit had left four empty placeholder
directories where they were meant to be.

The build is now three modules:

| Module | Owns |
|--------|------|
| `shared` | The protocol the client and the server both speak |
| `client` | The desktop game: the board, the rendering, the player input |
| `server` | The Spring Boot service: the rooms, the games, the players |

The root `pom.xml` became an aggregator over the three modules, and the Gradle build mirrors it
exactly through `settings.gradle`. The two builds therefore cannot drift apart and produce
artifacts that disagree — a mistake the pipeline caught in this very change, when the
authentication dependencies were added to the Maven build and not to the Gradle one.

## `shared` — the protocol both sides speak

The message vocabulary (`MessageType`) and a `MoveMessage` record. Both sides compile against the
same enum, so a message type cannot be renamed in one place only. The module holds the protocol
and nothing else, so neither side can drift into owning the other half of a conversation.

The engine itself is deliberately **not** in this module. Extracting the game engine from the
Swing UI into a module that JavaFX and the web client can both use is its own piece of work
(Day 19), and moving it here would take that work out of its own hands.

## `client` — the desktop game

The existing Swing application, moved into its own module. The JavaFX migration is a change
*inside* this module and does not affect the others.

Two things had to change because the sources moved:

* **The resources load from the classpath.** The old loader looked for `src/main/resources/`
  relative to the working directory, which stopped resolving once the sources were in a module.
  It now reads the classpath first and keeps the on-disk lookup as a fallback.
* **The sound is handed over as a stream.** A resource inside a JAR has no path on disk for the
  audio system to open, so `AudioPlayer` now takes a stream as well as a path.

## `server` — the Spring Boot service

A `@SpringBootApplication` entry point, a status endpoint that proves the service starts and
answers, and a configuration file. The authentication module lands here in Day 5.

## The pipeline follows the layout

The JARs, the coverage report and the test reports are collected from every module, and the
image builds the client module together with the module it depends on. Every module the
aggregator lists has to be present in the build context, even the one the image does not build,
because the reactor is read as a whole.

## Verification

The four-module reactor builds and passes every gate on GitHub Actions: Checkstyle, Spotless, the
unit and integration tests with coverage, the Maven build, the Gradle build, the Docker image and
the security scan.

## Day 4 Outcome
The build is split into shared, client and server modules, and the service boundaries in
`DAY2_ARCHITECTURE.md` are now modules rather than conventions. 1 PR created targeting
`development`.
