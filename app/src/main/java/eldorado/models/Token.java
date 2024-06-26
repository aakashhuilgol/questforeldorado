package eldorado.models;

import eldorado.gamemanager.PlayerManager;
import eldorado.utils.TerrainTypes;
import eldorado.utils.TokenTypes;
import java.util.HashMap;

public class Token {
  private int movementValue;
  private TerrainTypes terrainType;
  private TokenTypes tokenType;
  private PlayerManager player;
  private HashMap<TokenTypes, Runnable> tokenActions;

  public Token(TokenTypes tokenType, PlayerManager player) {
    this.tokenType = tokenType;
    this.movementValue = tokenType.movementValue;
    this.terrainType = tokenType.terrainType;
    this.player = player;
    this.tokenActions = new HashMap<>();

    // Initialize with empty actions
    for (TokenTypes _tokenType : TokenTypes.values()) {
      this.tokenActions.put(_tokenType, () -> {
      });
    }

    this.tokenActions.put(TokenTypes.DRAWCARD, () -> {
      // Draw a card from the deck
      this.player.drawCards(1);
    });

    this.tokenActions.put(TokenTypes.REMOVECARD, () -> {
      // Remove a card from the hand
      this.player.movement.put(
        TerrainTypes.BASECAMP,
        player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) - 1);    
      });

    // REPLACECARDS, select any number of cards from your hand and draw the same
    // number of cards from your draw pile
    this.tokenActions.put(TokenTypes.REPLACECARDS, () -> {
      this.player.replaceCards = true;
    });

    // SAVEITEM, when playing an item it is no longer removed from the game
    this.tokenActions.put(TokenTypes.SAVEITEM,
        () -> {
          this.player.saveItem = true;
        });

    // IGNOREOCCUPIED, when moving you may ignore occupied spaces
    this.tokenActions.put(TokenTypes.IGNOREOCCUPIED,
        () -> {
          this.player.ignoreOccupied = true;
        });

    // NATIVETOKEN, when moving you may ignore occupied spaces
    this.tokenActions.put(TokenTypes.NATIVETOKEN,
        () -> {
          this.player.changeToNative = true;
        });

    // CHANGECARDTYPE, when playing a card you may change its type
    this.tokenActions.put(TokenTypes.CHANGECARDTYPE,
        () -> {
          // Not implemented
          this.player.changeType = true;
        });
  }

  public int getMovementValue() {
    return this.movementValue;
  }

  public TerrainTypes getTerrainType() {
    return this.terrainType;
  }

  public TokenTypes getTokenType() {
    return this.tokenType;
  }

  public void play() {
    this.tokenActions.get(this.tokenType).run();
  }
}