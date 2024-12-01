class Day07 {

    companion object {

        enum class Card {
            JOKER, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE;

            companion object {
                fun from(card: Char, jokers: Boolean) = when(card) {
                    'J' -> if (jokers) JOKER else JACK
                    '1' -> ONE
                    '2' -> TWO
                    '3' -> THREE
                    '4' -> FOUR
                    '5' -> FIVE
                    '6' -> SIX
                    '7' -> SEVEN
                    '8' -> EIGHT
                    '9' -> NINE
                    'T' -> TEN
                    'Q' -> QUEEN
                    'K' -> KING
                    'A' -> ACE
                    else -> throw IllegalArgumentException("Invalid card: '$card'")
                }
            }
        }

        enum class HandType {
            HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND;

            companion object {
                fun from(cards: List<Card>): HandType {
                    // find all non-joker cards
                    val nonJokerCards = cards.filter { it != Card.JOKER }

                    // if there are no non-joker cards, it's a five of a kind
                    if (nonJokerCards.isEmpty()) return FIVE_OF_A_KIND

                    // find non-joker card type with the most matches
                    val mostMatchesCardType = nonJokerCards
                        .groupingBy { it } // group matching cards
                        .eachCount() // count each group size
                        .maxBy { it.value } // find the group with the most matches
                        .key // get the card type from the group

                    // replace jokers with the most matching card type
                    val substitutedCards = cards.map { if (it == Card.JOKER) mostMatchesCardType else it }

                    val matchingCardCount = substitutedCards
                        .groupingBy { it } // group matching cards
                        .eachCount() // count each group size
                        .values.sorted() // sort so we can match on set of counts

                    return when(matchingCardCount) {
                        listOf(1,1,1,1,1) -> HIGH_CARD // 5 unique cards
                        listOf(1,1,1,2) -> ONE_PAIR // 3 unique cards, 2 matching cards
                        listOf(1,2,2) -> TWO_PAIR // 1 unique card, 2 matching cards, 2 matching cards
                        listOf(1,1,3) -> THREE_OF_A_KIND // 2 unique cards, 3 matching cards
                        listOf(2,3) -> FULL_HOUSE // 2 matching cards, 3 matching cards
                        listOf(1,4) -> FOUR_OF_A_KIND // 1 unique card, 4 matching cards
                        listOf(5) -> FIVE_OF_A_KIND // 5 matching cards
                        else -> throw IllegalArgumentException("Invalid hand: $cards $substitutedCards $matchingCardCount")
                    }
                }
            }

        }

        data class Hand(
            val cards: List<Card>,
            val bid: Int,
            val type: HandType,
        ) : Comparable<Hand> {
            override fun compareTo(other: Hand): Int {
                // compare hand types first
                val typeComparison = type.compareTo(other.type)

                // if hand types are different, use that comparison
                if (typeComparison != 0) return typeComparison

                // if hand types are the same, compare cards
                val cardsComparison = cards.zip(other.cards).map { (a, b) -> a.compareTo(b) }

                // return first non-zero comparison, or 0 if all cards are equal
                return cardsComparison.firstOrNull { it != 0 } ?: 0
            }
        }

        fun parseHand(line: String, jokers: Boolean): Hand {
            val parts = line.split(" ")
            val cards = parts.first().toList().map { Card.from(it, jokers) }
            return Hand(
                cards = cards,
                bid = parts.last().toInt(),
                type = HandType.from(cards),
            )
        }

        fun calculateWinnings(hands: List<Hand>): Int {
            return hands
                .sorted()
                .mapIndexed { index, hand -> (index + 1) * hand.bid }
                .sum()
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val input = readInput("2023/Day07")

            // Part 1
            calculateWinnings(input.map { parseHand(it, false) }).println()

            // Part 2
            calculateWinnings(input.map { parseHand(it, true) }).println()
        }
    }
}