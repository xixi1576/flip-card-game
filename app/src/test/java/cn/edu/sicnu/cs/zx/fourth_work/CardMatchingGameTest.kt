package cn.edu.sicnu.cs.zx.fourth_work

import cn.edu.sicnu.cs.zx.fourth_work.model.Card
import cn.edu.sicnu.cs.zx.fourth_work.model.CardMatchingGame
import cn.edu.sicnu.cs.zx.fourth_work.model.Deck
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 翻牌游戏 MVC Model 层的单元测试（JVM 上运行）。
 */
class CardMatchingGameTest {

    // ---- Card 匹配规则 ----

    @Test
    fun card_match_sameRank_returns4() {
        assertEquals(4, Card("♥", "A").match(arrayOf(Card("♠", "A"))))
    }

    @Test
    fun card_match_sameSuitOnly_returns1() {
        assertEquals(1, Card("♥", "A").match(arrayOf(Card("♥", "K"))))
    }

    @Test
    fun card_match_noRelation_returns0() {
        assertEquals(0, Card("♥", "A").match(arrayOf(Card("♠", "K"))))
    }

    @Test
    fun card_match_moreThanOneCard_returns0() {
        assertEquals(0, Card("♥", "A").match(arrayOf(Card("♠", "A"), Card("♦", "A"))))
    }

    // ---- Deck 发牌 ----

    @Test
    fun deck_containsAll52CardsOnce() {
        val deck = Deck()
        val drawn = mutableSetOf<String>()
        var card = deck.drawRandomCard()
        while (card != null) {
            drawn.add(card.toString())
            card = deck.drawRandomCard()
        }
        assertEquals(Card.validSuits.size * Card.rankStrings.size, drawn.size)
    }

    @Test
    fun deck_afterAllDrawn_returnsNull() {
        val deck = Deck()
        repeat(Card.validSuits.size * Card.rankStrings.size) {
            deck.drawRandomCard()
        }
        assertEquals(null, deck.drawRandomCard())
    }

    // ---- CardMatchingGame 游戏规则 ----

    @Test
    fun game_initialState_scoreZeroAnd16UniqueCards() {
        val game = CardMatchingGame(16)
        assertEquals(0, game.score)
        val texts = (0 until 16).map { game.cardAtIndex(it).toString() }.toSet()
        assertEquals(16, texts.size)
    }

    @Test
    fun game_chooseOnce_costsOnePointAndCardFacesUp() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        assertEquals(-game.COST_TO_CHOOSE, game.score)
        assertTrue(game.cardAtIndex(0).isChosen)
        assertFalse(game.cardAtIndex(0).isMatched)
    }

    @Test
    fun game_chooseSameCardTwice_deselectsWithoutExtraCost() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(0)
        assertEquals(-game.COST_TO_CHOOSE, game.score)
        assertFalse(game.cardAtIndex(0).isChosen)
    }

    @Test
    fun game_chooseTwoCards_eitherMatchedOrPreviousFlippedBack() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)
        val first = game.cardAtIndex(0)
        val second = game.cardAtIndex(1)
        if (first.isMatched && second.isMatched) {
            // 同点数：-1(选第一张) -1(选第二张) + 4*4 = 14；仅同花色：-2 + 1*4 = 2
            assertTrue(game.score == 14 || game.score == 2)
        } else {
            // 未匹配：第一张被翻回，第二张保持正面
            assertFalse(first.isChosen)
            assertTrue(second.isChosen)
        }
    }

    @Test
    fun game_toggleChosenCard_isStableWithoutExtraCost() {
        val game = CardMatchingGame(16)
        game.chooseCardAtIndex(0)
        game.chooseCardAtIndex(1)
        val firstMatched = game.cardAtIndex(0).isMatched && game.cardAtIndex(1).isMatched
        val scoreAfterTwoChoices = game.score

        // 再次点击第二张牌
        game.chooseCardAtIndex(1)

        if (firstMatched) {
            // 已匹配的牌不能再被选择，分数与状态不变
            assertEquals(scoreAfterTwoChoices, game.score)
            assertTrue(game.cardAtIndex(1).isMatched)
        } else {
            // 未匹配时第二张处于选中态，再次点击将其翻回，且不再扣分
            assertEquals(scoreAfterTwoChoices, game.score)
            assertFalse(game.cardAtIndex(1).isChosen)
        }
    }
}
