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

# Playing the game
To navigate through the map, you can drag and drop in the MapGUI.
To play the game, select a card that you want to play. Once selected, you can right click on the card, to get the options to play, discard or buy. You can click on play to play the particular card, and then click on the hex tile on the map to move to that position (if possible). Click end turn when you are done with your turn.

# Rules of the game

Rules of the game are taken from :
https://www.ultraboardgames.com/the-quest-for-el-dorado/game-rules.php
https://www.ultraboardgames.com/the-quest-for-el-dorado/cards.php
https://www.ultraboardgames.com/the-quest-for-el-dorado/caves-variant.php
