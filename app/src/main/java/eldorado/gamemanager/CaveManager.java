package eldorado.gamemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eldorado.models.Token;
import eldorado.utils.TokenTypes;

public class CaveManager {
    private static CaveManager instance;
    private List<Token> caveTokens = new ArrayList<>();

    public static CaveManager getInstance() {
        if (instance == null) {
            instance = new CaveManager();
        }
        return instance;
    }

    public void createCave() {
        caveTokens.clear();
        addTokenToCave(2, new Token(TokenTypes.JUNGLE1, null));
        addTokenToCave(3, new Token(TokenTypes.JUNGLE2, null));
        addTokenToCave(2, new Token(TokenTypes.JUNGLE3, null));
        addTokenToCave(2, new Token(TokenTypes.VILLAGE1, null));
        addTokenToCave(3, new Token(TokenTypes.VILLAGE2, null));
        addTokenToCave(2, new Token(TokenTypes.SEA1, null));
        addTokenToCave(3, new Token(TokenTypes.SEA2, null));
        addTokenToCave(4, new Token(TokenTypes.DRAWCARD, null));
        addTokenToCave(2, new Token(TokenTypes.NATIVETOKEN, null));
        addTokenToCave(4, new Token(TokenTypes.REMOVECARD, null));
        addTokenToCave(3, new Token(TokenTypes.REPLACECARDS, null));
        addTokenToCave(2, new Token(TokenTypes.SAVEITEM, null));
        addTokenToCave(2, new Token(TokenTypes.CHANGECARDTYPE, null));
        addTokenToCave(2, new Token(TokenTypes.IGNOREOCCUPIED, null));
    }

    public List<Token> getTokens() {
        return new ArrayList<>(caveTokens);
    }

    public void removeTokens(List<Token> tokens) {
        for (Token token : tokens) {
            caveTokens.remove(token);
        }
    }

    public List<Token> getFourRandomTokens() {
        List<Token> fourRandomTokens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            fourRandomTokens.add(getRandomToken(getTokens()));
        }
        return fourRandomTokens;
    }

    public Token getRandomToken(List<Token> tokens) {
        if (tokens.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(tokens.size());
        Token randomToken = tokens.get(randomIndex);
        tokens.remove(randomToken);
        return randomToken;
    }

    private void addTokenToCave(int number, Token token) {
        for (int i = 0; i < number; i++) {
            caveTokens.add(token);
        }
    }
}
