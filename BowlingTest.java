package stev.bowling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de tests JUnit pour valider la librairie bowling-score.
 * Vérifie le comportement des classes NormalFrame, LastFrame et Game
 * selon les règles du jeu de quilles et les spécifications de l'API.
 */
public class BowlingTest {

    // =====================================================================
    // Tests pour NormalFrame — carreaux ouverts
    // =====================================================================

    /**
     * Vérifie que countPinsDown() retourne la somme correcte des quilles
     * abattues dans un carreau ouvert (3 + 6 = 9).
     */
    @Test
    public void normalFrameOpenFrameCountPinsDown() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 6);
        assertEquals(9, f.countPinsDown());
    }

    /**
     * Vérifie que countRolls() retourne 2 pour un carreau ouvert complet.
     */
    @Test
    public void normalFrameOpenFrameCountRolls() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 6);
        assertEquals(2, f.countRolls());
    }

    /**
     * Vérifie que toString() retourne "36" pour un carreau avec 3 puis 6 quilles abattues.
     */
    @Test
    public void normalFrameOpenFrameToString() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 6);
        assertEquals("36", f.toString());
    }

    /**
     * Vérifie que le symbole '-' est utilisé pour représenter un dalot (0 quille abattue).
     */
    @Test
    public void normalFrameGutterUsesSymbol() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 0);
        f.setPinsDown(2, 0);
        assertEquals("--", f.toString());
    }

    // =====================================================================
    // Tests pour NormalFrame — abat (strike)
    // =====================================================================

    /**
     * Vérifie qu'un abat est représenté par "X " (X suivi d'un espace).
     */
    @Test
    public void normalFrameStrikeToString() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 10);
        assertEquals("X ", f.toString());
    }
    

    /**
     * Vérifie qu'un abat ne compte qu'un seul lancer dans le carreau.
     */
    @Test
    public void normalFrameStrikeCountsOneRoll() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 10);
        assertEquals(1, f.countRolls());
    }

    /**
     * Vérifie qu'un abat comptabilise bien 10 quilles abattues.
     */
    @Test
    public void normalFrameStrikeCountPinsDown() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 10);
        assertEquals(10, f.countPinsDown());
    }

    // =====================================================================
    // Tests pour NormalFrame — réserve (spare)
    // =====================================================================

    /**
     * Vérifie que le symbole '/' est utilisé pour représenter une réserve.
     */
    @Test
    public void normalFrameSpareToString() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 5);
        f.setPinsDown(2, 5);
        assertEquals("5/", f.toString());
    }

    /**
     * Vérifie qu'une réserve commençant par un dalot est représentée par "-/".
     */
    @Test
    public void normalFrameSpareWithZeroFirstRollToString() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 0);
        f.setPinsDown(2, 10);
        assertEquals("-/", f.toString());
    }

    /**
     * Vérifie qu'une réserve compte 2 lancers.
     */
    @Test
    public void normalFrameSpareCountRolls() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 5);
        f.setPinsDown(2, 5);
        assertEquals(2, f.countRolls());
    }

    // =====================================================================
    // Tests pour NormalFrame — exceptions
    // =====================================================================

    /**
     * Vérifie qu'une exception est levée lorsque le premier lancer abat plus de 10 quilles.
     */
    @Test
    public void normalFrameFirstRollOver10ThrowsException() {
        NormalFrame f = new NormalFrame(1);
        assertThrows(BowlingException.class, () -> f.setPinsDown(1, 11));
    }

    /**
     * Vérifie qu'une exception est levée lorsque la somme des deux lancers dépasse 10.
     */
    @Test
    public void normalFrameTotalPinsOver10ThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 6);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 5));
    }

    /**
     * Vérifie qu'une exception est levée pour un nombre de quilles négatif.
     */
    @Test
    public void normalFrameNegativePinsThrowsException() {
        NormalFrame f = new NormalFrame(1);
        assertThrows(BowlingException.class, () -> f.setPinsDown(1, -1));
    }

    /**
     * Vérifie qu'une exception est levée si on enregistre le lancer 2 avant le lancer 1.
     */
    @Test
    public void normalFrameRollOutOfOrderThrowsException() {
        NormalFrame f = new NormalFrame(1);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 5));
    }

    /**
     * Vérifie qu'une exception est levée si on tente un troisième lancer dans un carreau normal.
     */
    @Test
    public void normalFrameThirdRollThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 4);
        assertThrows(BowlingException.class, () -> f.setPinsDown(3, 2));
    }

    /**
     * Vérifie qu'une exception est levée si on tente un deuxième lancer après un abat
     * dans un carreau normal.
     */
    @Test
    public void normalFrameSecondRollAfterStrikeThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 10);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 5));
    }

    /**
     * Vérifie que l'état du carreau reste inchangé après le lancement d'une exception.
     */
    @Test
    public void normalFrameStateUnchangedAfterException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 5);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 6));
        assertEquals(1, f.countRolls());
        assertEquals(5, f.countPinsDown());
    }

    // =====================================================================
    // Tests pour NormalFrame — reset et countPinsDown(x)
    // =====================================================================

    /**
     * Vérifie que reset() efface tous les lancers et remet le carreau à zéro.
     */
    @Test
    public void normalFrameResetClearsAllRolls() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 6);
        f.reset();
        assertEquals(0, f.countRolls());
        assertEquals(0, f.countPinsDown());
    }

    /**
     * Vérifie que reset() permet de re-saisir les lancers depuis le début.
     */
    @Test
    public void normalFrameResetAllowsReEntry() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        f.reset();
        f.setPinsDown(1, 7);
        f.setPinsDown(2, 2);
        assertEquals(9, f.countPinsDown());
    }

    /**
     * Vérifie que toString() retourne deux espaces si aucun lancer n'a été effectué.
     */
    @Test
    public void normalFrameToStringWithNoRolls() {
        NormalFrame f = new NormalFrame(1);
        assertEquals("  ", f.toString());
    }

    /**
     * Vérifie que toString() retourne le lancer suivi d'un espace si seulement
     * le premier lancer a eu lieu.
     */
    @Test
    public void normalFrameToStringAfterFirstRollOnly() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        assertEquals("3 ", f.toString());
    }

    // =====================================================================
    // Tests pour LastFrame
    // =====================================================================

    /**
     * Vérifie qu'une réserve au dixième carreau autorise un troisième lancer.
     */
    @Test
    public void lastFrameSpareAllowsThirdRoll() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 5);
        f.setPinsDown(2, 5);
        f.setPinsDown(3, 3);
        assertEquals(3, f.countRolls());
    }

    /**
     * Vérifie qu'un abat au dixième carreau autorise deux lancers supplémentaires.
     */
    @Test
    public void lastFrameStrikeAllowsThreeRolls() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 10);
        f.setPinsDown(2, 5);
        f.setPinsDown(3, 3);
        assertEquals(3, f.countRolls());
    }

    /**
     * Vérifie que trois abats consécutifs au dixième carreau sont valides (30 quilles).
     */
    @Test
    public void lastFrameThreeStrikesValid() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 10);
        f.setPinsDown(2, 10);
        f.setPinsDown(3, 10);
        assertEquals(30, f.countPinsDown());
    }

    /**
     * Vérifie qu'un carreau ouvert au dixième carreau se limite à 2 lancers.
     */
    @Test
    public void lastFrameOpenFrameExactlyTwoRolls() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 4);
        assertEquals(2, f.countRolls());
    }

    /**
     * Vérifie qu'une exception est levée lors d'un troisième lancer dans un carreau ouvert
     * au dixième carreau.
     */
    @Test
    public void lastFrameOpenFrameThirdRollThrowsException() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 3);
        f.setPinsDown(2, 4);
        assertThrows(BowlingException.class, () -> f.setPinsDown(3, 2));
    }

    /**
     * Vérifie que toString() retourne exactement 3 caractères pour le dixième carreau.
     */
    @Test
    public void lastFrameToStringReturnsThreeChars() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 1);
        f.setPinsDown(2, 9);
        f.setPinsDown(3, 3);
        assertEquals(3, f.toString().length());
    }

    /**
     * Vérifie la représentation textuelle d'une réserve au dixième carreau ("1/3").
     */
    @Test
    public void lastFrameSpareToString() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 1);
        f.setPinsDown(2, 9);
        f.setPinsDown(3, 3);
        assertEquals("1/3", f.toString());
    }

    /**
     * Vérifie la représentation textuelle de trois abats au dixième carreau ()
     */
    @Test
    public void lastFrameThreeStrikesToString() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 10);
        f.setPinsDown(2, 10);
        f.setPinsDown(3, 10);
        assertEquals("XXX", f.toString());
    }

    /**
     * Vérifie que toString() retourne trois espaces si aucun lancer n'a été effectué
     * au dixième carreau.
     */
    @Test
    public void lastFrameToStringWithNoRolls() {
        LastFrame f = new LastFrame(10);
        assertEquals("   ", f.toString());
    }

    // =====================================================================
    // Tests pour Game
    // =====================================================================

    // NOTE : La méthode getScore(int x) spécifiée dans l'API est absente de l'implémentation.
    // Les tests de score cumulatif ne peuvent pas être compilés — c'est un bug détecté.

    // NOTE : La méthode getFrame(int x) spécifiée dans l'API est absente de l'implémentation.
    // C'est un bug détecté supplémentaire.

    // =====================================================================
    // Tests paramétrés
    // =====================================================================

    /**
     * Vérifie que countPinsDown() retourne la somme correcte pour différentes
     * combinaisons de lancers valides dans un carreau normal ouvert.
     */
    @ParameterizedTest
    @CsvSource({
        "0, 0, 0",
        "1, 2, 3",
        "5, 4, 9",
        "0, 9, 9",
        "7, 2, 9",
        "4, 5, 9"
    })
    public void normalFrameCountPinsDownParameterized(int roll1, int roll2, int expected)
            throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, roll1);
        f.setPinsDown(2, roll2);
        assertEquals(expected, f.countPinsDown());
    }

    /**
     * Vérifie que toString() de Game contient bien les scores cumulatifs
     * pour différentes combinaisons de lancers ouverts.
     */
    @ParameterizedTest
    @CsvSource({
        "1, 2, 30",
        "2, 3, 50",
        "0, 0,  0",
        "3, 4, 70"
    })
    public void gameAllOpenFramesToStringContainsScore(int roll1, int roll2, int expectedScore)
            throws BowlingException {
        Game g = new Game();
        for (int i = 1; i <= 9; i++) {
            g.addFrame(new NormalFrame(i).setPinsDown(1, roll1).setPinsDown(2, roll2));
        }
        g.addFrame(new LastFrame(10).setPinsDown(1, roll1).setPinsDown(2, roll2));
        assertTrue(g.toString().contains(String.valueOf(expectedScore)));
    }

    // =====================================================================
    // Tests supplémentaires NormalFrame — cas non couverts
    // =====================================================================

    /**
     * Vérifie qu'une exception est levée si on enregistre le lancer 1 deux fois
     * dans le même carreau.
     */
    @Test
    public void normalFrameFirstRollTwiceThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        assertThrows(BowlingException.class, () -> f.setPinsDown(1, 4));
    }

    /**
     * Vérifie que countRolls() retourne 0 avant tout lancer.
     */
    @Test
    public void normalFrameCountRollsBeforeAnyRoll() {
        NormalFrame f = new NormalFrame(1);
        assertEquals(0, f.countRolls());
    }

    /**
     * Vérifie que countPinsDown() retourne 0 avant tout lancer.
     */
    @Test
    public void normalFrameCountPinsDownBeforeAnyRoll() {
        NormalFrame f = new NormalFrame(1);
        assertEquals(0, f.countPinsDown());
    }

    /**
     * Vérifie que toString() retourne "- " quand seul le premier lancer est un dalot.
     */
    @Test
    public void normalFrameToStringFirstRollGutter() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 0);
        assertEquals("- ", f.toString());
    }

    /**
     * Vérifie qu'une exception est levée pour un nombre négatif au deuxième lancer.
     */
    @Test
    public void normalFrameNegativeSecondRollThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 3);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, -1));
    }

    /**
     * Vérifie qu'une exception est levée si le deuxième lancer dépasse 10 quilles.
     */
    @Test
    public void normalFrameSecondRollOver10ThrowsException() throws BowlingException {
        NormalFrame f = new NormalFrame(1);
        f.setPinsDown(1, 0);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 11));
    }

    /**
     * Vérifie qu'une exception est levée si le numéro de lancer est invalide (0 ou négatif).
     */
    @Test
    public void normalFrameInvalidRollNumberThrowsException() {
        NormalFrame f = new NormalFrame(1);
        assertThrows(BowlingException.class, () -> f.setPinsDown(0, 5));
    }

    // =====================================================================
    // Tests supplémentaires LastFrame — cas non couverts
    // =====================================================================

    /**
     * Vérifie que toString() retourne " " (espace) suivi du bon symbole
     * après un seul lancer au dixième carreau.
     */
    @Test
    public void lastFrameToStringAfterOneRoll() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 5);
        assertEquals("5  ", f.toString());
    }

    /**
     * Vérifie que toString() retourne "XX-" pour abat + abat + dalot.
     */
    @Test
    public void lastFrameTwoStrikesAndGutterToString() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 10);
        f.setPinsDown(2, 10);
        f.setPinsDown(3, 0);
        assertEquals("XX-", f.toString());
    }

    /**
     * Vérifie que toString() retourne "X-/" pour abat + dalot + réserve. abdoulkande2@gmail.com
     */
    @Test
    public void lastFrameStrikeGutterSpareToString() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 10);
        f.setPinsDown(2, 0);
        f.setPinsDown(3, 10);
        assertEquals("X-/", f.toString());
    }

    /**
     * Vérifie qu'une exception est levée si le deuxième lancer dépasse les quilles
     * restantes au dixième carreau (sans abat au premier lancer).
     */
    @Test
    public void lastFrameSecondRollExceedsRemainingPinsThrowsException() throws BowlingException {
        LastFrame f = new LastFrame(10);
        f.setPinsDown(1, 4);
        assertThrows(BowlingException.class, () -> f.setPinsDown(2, 7));
    }

    /**
     * Vérifie que countRolls() retourne 0 avant tout lancer au dixième carreau.
     */
    @Test
    public void lastFrameCountRollsBeforeAnyRoll() {
        LastFrame f = new LastFrame(10);
        assertEquals(0, f.countRolls());
    }

    // =====================================================================
    // Tests supplémentaires Game — cas non couverts
    // =====================================================================

    /**
     * Vérifie que toString() de Game contient bien le symbole X pour un abat.
     */
    @Test
    public void gameToStringContainsStrikeSymbol() throws BowlingException {
        Game g = new Game();
        g.addFrame(new NormalFrame(1).setPinsDown(1, 10));
        for (int i = 2; i <= 9; i++) {
            g.addFrame(new NormalFrame(i).setPinsDown(1, 0).setPinsDown(2, 0));
        }
        g.addFrame(new LastFrame(10).setPinsDown(1, 0).setPinsDown(2, 0));
        assertTrue(g.toString().contains("X"));
    }

    /**
     * Vérifie que toString() de Game de la partie exemple correspond exactement
     * à la grille attendue dans l'énoncé.
     */
    @Test
    public void gameExampleToStringFormat() throws BowlingException {
        Game g = new Game();
        g.addFrame(new NormalFrame(1).setPinsDown(1, 3).setPinsDown(2, 6));
        g.addFrame(new NormalFrame(2).setPinsDown(1, 10));
        g.addFrame(new NormalFrame(3).setPinsDown(1, 5).setPinsDown(2, 0));
        g.addFrame(new NormalFrame(4).setPinsDown(1, 1).setPinsDown(2, 9));
        g.addFrame(new NormalFrame(5).setPinsDown(1, 10));
        g.addFrame(new NormalFrame(6).setPinsDown(1, 0).setPinsDown(2, 0));
        g.addFrame(new NormalFrame(7).setPinsDown(1, 0).setPinsDown(2, 6));
        g.addFrame(new NormalFrame(8).setPinsDown(1, 10));
        g.addFrame(new NormalFrame(9).setPinsDown(1, 2).setPinsDown(2, 8));
        g.addFrame(new LastFrame(10).setPinsDown(1, 1).setPinsDown(2, 9).setPinsDown(3, 3));
        String result = g.toString();
        assertTrue(result.contains("36"));
        assertTrue(result.contains("X"));
        assertTrue(result.contains("5-"));
        assertTrue(result.contains("1/"));
        assertTrue(result.contains("--"));
        assertTrue(result.contains("-6"));
        assertTrue(result.contains("2/"));
        assertTrue(result.contains("1/3"));
        assertTrue(result.contains("109"));
    }
}
