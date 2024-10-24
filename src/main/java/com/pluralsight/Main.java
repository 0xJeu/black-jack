package com.pluralsight;

import java.util.*;

public class Main {

    //Keyboard to use throughout main class
    static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) {
        Deck deck = new Deck();
        HashMap<String, Hand> players = playerCreation();
        Hand dealerHand = new Hand();

        //Print statement checks deck size.
        System.out.println("Deck size is " + deck.getSize());

        //Shuffle the deck before dealing
        deck.shuffle();

        //Loop handles dealing cards to the dealer
        for (int i = 0; i < 2; i++) {
            Card card = deck.deal();
            dealerHand.deal(card);
        }

        // Loop handles dealing the cards to available players in HashMap
        for (Map.Entry<String, Hand> entry : players.entrySet()) {
            // deal 2 cards
            for (int i = 0; i < 2; i++) {
                // get a card from the deck
                Card card = deck.deal();
                // deal that card to the hand
                entry.getValue().deal(card);

            }
            //Print player's initial hand value
            System.out.printf("%s's hand is worth %d.\n", entry.getKey(), entry.getValue().getValue());
        }

        System.out.printf("%s's hand is worth %d.\n", "Dealer", dealerHand.getValue());


        //Method to decide whether each player should "hit" (draw a card) or "stay" (keep current hand) based on their current hand
        hitOrStay(players, deck);

        //Loop to display final hand values
        for (Map.Entry<String, Hand> entry : players.entrySet()) {
            int handValue = entry.getValue().getValue();
            String playerName = entry.getKey();

            System.out.printf("%s's hand is worth %d.\n", playerName, handValue);
        }

        //Method to find winner
        String winner = determineWinner(players, dealerHand);

        // Conditional to determine what print statement to display at the end
        if (winner.equalsIgnoreCase("Tie")) {
            System.out.println("The game is tied!");
        } else if (winner.equalsIgnoreCase("Bust")) {
            System.out.println("No winner (all players bust)");
        } else {
            System.out.println("The winner is: " + winner);
        }
    }

    public static HashMap<String, Hand> playerCreation() {
        //HashMap to store created player(s)
        HashMap<String, Hand> players = new HashMap<>();

        //Loop to create players
        while (true) {
            System.out.print("Please enter your name: ");
            String name = keyboard.nextLine();
            Hand hand = new Hand();
            players.put(name, hand);
            System.out.println("Player and hand created successfully!");
            System.out.print("would you like to add another player? Y or N: ");
            String nextPlayer = keyboard.nextLine();
            if (nextPlayer.equalsIgnoreCase("y")) {
                continue;
            } else {
                break;
            }
        }

        return players;
    }

    public static String determineWinner(HashMap<String, Hand> players, Hand dealerHand) {
        //Variables needed to determine winner
        int highScore = 0;
        List<String> winner = new ArrayList<>();
        boolean isTied = false;

        // Include dealer's hand in the comparison
        int dealerValue = dealerHand.getValue();
        if (dealerValue <= 21) {
            highScore = dealerValue;
            winner.add("Dealer");
        }

        //This first loops finds the highest score
        for (Map.Entry<String, Hand> entry : players.entrySet()) {
            int handValue = entry.getValue().getValue();

            if (handValue <= 21) {
                if (handValue > highScore) {
                    highScore = handValue;
                    winner.clear();  // Clear previous winners as we have a new high score
                    winner.add(entry.getKey());
                } else if (handValue == highScore) {
                    winner.add(entry.getKey());
                }
            }
        }

        //Conditional used to determine final outcome
        if (winner.isEmpty()) {
            return "All Players Bust..";
        } else if (winner.size() == 1) {
            return winner.get(0);
        } else {
            return "Game is tied.";

        }
    }

    public static HashMap<String, Hand> hitOrStay(HashMap<String, Hand> players, Deck deck) {

        for (Map.Entry<String, Hand> entry : players.entrySet()) {
            Hand playerHand = entry.getValue();
            String playerName = entry.getKey();

            while (playerHand.getValue() < 21) {
                System.out.printf("%s, Hit or Stay?: ", entry.getKey());
                String userResponse = keyboard.nextLine().trim();

                if (userResponse.equalsIgnoreCase("hit")) {
                    // get a card from the deck
                    Card card = deck.deal();

                    // deal that card to the hand
                    entry.getValue().deal(card);

                    //Display new handValue
                    System.out.printf("%s's hand is worth %d.\n", playerName, playerHand.getValue());

                } else if (userResponse.equalsIgnoreCase("stay")) {
                    break;
                } else {
                    System.out.println("Invalid input. enter hit or stay.");
                }

                if (playerHand.getValue() > 21) {
                    System.out.println("Bust. You lose..");
                }
            }
        }
        return players;
    }
}
