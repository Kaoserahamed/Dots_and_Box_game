# Dots and Boxes desktop client image.
#
# The game is a Swing application, so the image carries a JRE and the
# assembled JAR. Running it needs a display, which is why the runtime stage
# also carries a virtual framebuffer: `xvfb-run java -jar /app/app.jar`
# starts the game against a virtual screen, which is what a headless
# container can offer.
#
# Only the client module is built into the image. The server is a separate
# service with its own image, which is a later piece of work.
FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /build

# The aggregator POM and the two modules the client needs are copied first, so
# a change to the sources does not re-resolve the dependencies.
COPY pom.xml .
COPY shared ./shared
COPY client ./client
RUN mvn -B -ntp -pl client -am clean package -DskipTests


FROM eclipse-temurin:17-jre

ENV PYTHONUNBUFFERED=1 \
    DISPLAY=:99

RUN apt-get update \
    && apt-get install --no-install-recommends -y xvfb xauth \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /build/client/target/make-square-game-1.0.0-jar-with-dependencies.jar /app/app.jar

ENTRYPOINT ["xvfb-run", "--auto-servernum", "java", "-jar", "/app/app.jar"]
