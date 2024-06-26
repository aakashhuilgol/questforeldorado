package eldorado.models;

import java.util.ArrayList;
import java.util.List;

import eldorado.gamemanager.CaveManager;
import eldorado.gamemanager.ElDoradoManager;
import eldorado.utils.TerrainTypes;

public class GameTile extends HexTile {
    public final TerrainTypes type;
    public final int value;
    private List<Token> token = new ArrayList<>();
    private CaveManager caveManager = CaveManager.getInstance();

    public GameTile(
            Integer[] _xPoints,
            Integer[] _yPoints,
            int sideLength,
            TerrainTypes _type,
            int _value) {
        super(_xPoints, _yPoints, sideLength);
        type = _type;
        value = _value;
    }
    public Token getToken() {
        Token newToken = new Token(caveManager.getRandomToken(token).getTokenType(), ElDoradoManager.getInstance().getCurrentPlayer());
        return newToken;
    }

    public List<Token> getCave() {
        return token;
    }

    public GameTile setToken(List<Token> _token) {
        if (type == TerrainTypes.CAVE) {
            this.token = _token;
            caveManager.removeTokens(_token);
        }
        return this;
    }
}
