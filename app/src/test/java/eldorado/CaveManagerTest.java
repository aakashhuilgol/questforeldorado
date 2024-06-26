package eldorado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eldorado.gamemanager.CaveManager;
import eldorado.models.Token;

public class CaveManagerTest {
    private CaveManager caveManager;

    @Before
    public void setUp() {
        caveManager = CaveManager.getInstance();
        caveManager.createCave();
    }

    @Test
    public void testSingletonInstance() {
        CaveManager anotherInstance = CaveManager.getInstance();
        assertSame("CaveManager should return the same instance for subsequent calls.", caveManager, anotherInstance);
    }

    @Test
    public void testCreateCave() {
        List<Token> tokens = caveManager.getTokens();
        assertEquals("Cave should contain 36 tokens", 36, tokens.size());
    }

    @Test
    public void testGetTokens() {
        List<Token> tokens = caveManager.getTokens();
        assertEquals("Cave should contain 36 tokens", 36, tokens.size());
    }

    @Test
    public void testGetFourTokens() {
        List<Token> tokens = caveManager.getFourRandomTokens();
        assertEquals("Result should contain 4 tokens", 4, tokens.size());
    }

    @Test
    public void testGetRandomToken() {
        Token randomToken = caveManager.getRandomToken(caveManager.getTokens());
        assertNotNull("Random token should not be null", randomToken);
        assertNull("Return null if empty", caveManager.getRandomToken(new ArrayList<>()));
    }

    @Test
    public void testRemoveTokens() {
        List<Token> tokens = caveManager.getFourRandomTokens();
        caveManager.removeTokens(tokens);
        assertEquals("Cave should contain 32 tokens", 32, caveManager.getTokens().size());
    }
}
