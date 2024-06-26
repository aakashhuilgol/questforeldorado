# Running the app

To run the app. In the root directory:

```bash
./gradlew run
```

To run with 1-4 players (default 2), use parameter <em>"np"</em>:

```bash
./gradlew run --args="np4"
```

To play the game with the caves, use parameter <em>"caves"</em>:

```bash
./gradlew run --args="caves"
```

To play with the original map (before exchange), run woth <em>"ormap"</em> parameter:

```bash
./gradlew run --args="ormap"
```

To generate jacoco report, run the following command. The resulting artefacts will be generated at <em>/build/report/jacoco</em>.

```bash
./gradlew jacocoTestReport
```
