# Dots and Boxes desktop client image.
#
# The game is a Swing application, so the image carries a JRE and the
# assembled JAR. Running it needs a display, which is why the runtime stage
# also carries a virtual framebuffer: `xvfb-run java -jar /app/app.jar`
# starts the game against a virtual screen, which is what a headless
# container can offer.
FROM eclipse-temurin:17-jdk AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp clean package -DskipTests


FROM eclipse-temurin:17-jre

ENV PYTHONUNBUFFERED=1 \
    DISPLAY=:99

RUN apt-get update \
    && apt-get install --no-install-recommends -y xvfb xauth \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /build/target/make-square-game-1.0.0-jar-with-dependencies.jar /app/app.jar

ENTRYPOINT ["xvfb-run", "--auto-servernum", "java", "-jar", "/app/app.jar"]
