
/**
 * GoFish
 */
import java.util.ArrayList;
import java.util.Scanner;
public class GoFish {
    ArrayList<String> myHand = new ArrayList<String>();
    ArrayList<String> computerHand = new ArrayList<String>();
    ArrayList<String> deckOfCards = new ArrayList<String>();


     //Constructor that initializes the deck of cards via nested for-loop
     public GoFish() {
        String[] numcard = {"2","3","4","5","6","7","8","9","10","J","Q","K","A"};
        String[] suitcard = {" Clubs"," Diamond"," Hearts"," Spades"};
        for (int i = 0; i<numcard.length;i++){
            for (int j = 0; j<suitcard.length; j++){
                deckOfCards.add(numcard[i]+suitcard[j]);
            }
        }
    }

    //Given a hand of cards, will randomly draw 5 cards into your hand
    public ArrayList<String> randomlyDraw(ArrayList<String> hand){
        int deckSize = deckOfCards.size();
        for (int i = 0; i<5; i++){
            double randomIndexDouble = Math.random()*(deckSize);
            int randomIndex = (int)randomIndexDouble;
            hand.add(deckOfCards.get(randomIndex));
            deckOfCards.remove(randomIndex);
            deckSize = deckOfCards.size();
        }
        return hand;
        
    }
   
    //Method that handles changes to the player's hand when asking the computer for a card
    public ArrayList<String> iAskComputerMyHand(String card1, String myCard, ArrayList<String> otherHand, ArrayList<String> myHand){
        boolean hasCard = false;
        int index = 0;
        for (int i = 0; i<otherHand.size(); i++){
            if (otherHand.get(i).substring(0,1).equalsIgnoreCase(card1) || otherHand.get(i).substring(0,2).equalsIgnoreCase(card1)){
                hasCard = true;
                index = i;
                break;
            }
        }
        if (hasCard){
            System.out.println("Match! I have this card: " + otherHand.get(index));
            myHand.remove(myCard);
        } else {
            System.out.println("Sorry! Go fish!");
            double randomIndexDouble = Math.random()*(deckOfCards.size());
            int randomIndex = (int)randomIndexDouble;
            myHand.add(deckOfCards.get(randomIndex));
            deckOfCards.remove(randomIndex);

        }
        return myHand;
    }

    //Method that handles changes to the computer's hand when asking the computer for a card
    public ArrayList<String> iAskComputerTheirHand(String card2, ArrayList<String> compHand, ArrayList<String> playerHand){
        boolean hasCard = false;
        int index = 0;
        for (int i = 0; i<compHand.size(); i++){
            if (compHand.get(i).substring(0,1).equalsIgnoreCase(card2) || compHand.get(i).substring(0,2).equalsIgnoreCase(card2)){
                hasCard = true;
                index = i;
                break;
            }
        }
        if (hasCard){
            compHand.remove(index);
        } 
        return compHand;
    }

    //Method that handles changes to the computer's hand when the computer asks the player for a card
    public ArrayList<String> computerAsksMe(ArrayList<String> compHand, String haveCard, int randomIndex){
            if (haveCard.equalsIgnoreCase("y")){
                compHand.remove(randomIndex);
            } else {
                System.out.println("Go fish, computer!");
                double randomIndDouble = Math.random()*(deckOfCards.size());
                int randomInd = (int)randomIndDouble;
                compHand.add(deckOfCards.get(randomInd));
                deckOfCards.remove(randomInd);    
            } 
        return compHand;
    }

    //Method that handles changes to the player's hand when the computer asks the player for a card
    public ArrayList<String> computerAsksMeMyHand(ArrayList<String> playerHand, String haveCard, int randomIndex, String cardReference){
        int cardIndex = -1;
        if (haveCard.equalsIgnoreCase("Y")){
            for (int i = 0; i<playerHand.size(); i++){
                if (cardReference.substring(0,1).equals(playerHand.get(i).substring(0,1)) || cardReference.substring(0,2).equals(playerHand.get(i).substring(0,2))){
                    cardIndex = i;
                    break;
                }
            }

            /*Try-catch block to handle if the element at the given index does not exist, but this should work well because of 
            the checks that catch if the user is lying
            */ 
            try {
                System.out.println("Match! You have this card: " + playerHand.get(cardIndex));
                playerHand.remove(cardIndex);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("You lied...please be honest!");
                computerAsksMe(computerHand, "n", 0);
            }

        }
        return playerHand;
    }

    //Operation that checks for duplicates, three-of-a-kinds, and four-of-a-kind given a hand of cards
    public ArrayList<String> checkForDoubles(ArrayList<String> inputer){
        int endOfLoop = inputer.size();
        for (int i = 0; i<endOfLoop; i++){
            for (int j = i; j<endOfLoop; j++){
                if (inputer.get(i).substring(0,2).equals(inputer.get(j).substring(0,2)) && j != i){
                    inputer.remove(i);
                    inputer.remove(j-1);
                    j--;
            }
            endOfLoop = inputer.size();
            }
            
        }
        return inputer;
    }


    public static void main(String[] args) {
        boolean playAgain = true;
        Scanner input = new Scanner(System.in);
        while (playAgain == true){

            //Initializes game state, randomly draws hands for the player and computer, and notifies the user of the gameplay
            GoFish newGame = new GoFish();      
            newGame.myHand = newGame.randomlyDraw(newGame.myHand);
            newGame.computerHand = newGame.randomlyDraw(newGame.computerHand);
            System.out.println("Welcome to Go Fish! You're playing against me - the computer!\nI drew 5 random cards for myself and 5 random cards for you. Your starting hand is: " + newGame.myHand);
            newGame.myHand = newGame.checkForDoubles(newGame.myHand);
            newGame.computerHand = newGame.checkForDoubles(newGame.computerHand);
            System.out.println("After removing pairs/triples/four of a kinds, your hand is now: "+ newGame.myHand);
            System.out.println("For reference, I have " + newGame.computerHand.size() + " cards left.");

            //Main game loop that occurs as long as each player has cards in their hand and the deck is not empty
            while (newGame.myHand.size()>0 && newGame.computerHand.size()>0 && newGame.deckOfCards.size()>0){
                boolean works = false;
                int indexOfCard = 0;
                String cardCheck = "";

                //Checks if the input is valid and if the card exists in the users hand
                while (works == false){
                    System.out.print("Ask me for one of your cards. Enter the first character of the card to check for (ex. numbers, J,Q,K,A): ");
                    cardCheck = input.next();
                    for (int i = 0; i<newGame.myHand.size(); i++){
                        if (newGame.myHand.get(i).substring(0,1).equalsIgnoreCase(cardCheck)|| newGame.myHand.get(i).substring(0,2).equalsIgnoreCase(cardCheck) ){
                            works = true;
                            indexOfCard = i;
                            break;
                        }
                    }
                    if (works == false){
                    System.out.println("Invalid card. Please choose a card in your hand and try again.");
                    }     
            }
            //Processing changes to each hand after player asks computer
            newGame.myHand = newGame.iAskComputerMyHand(cardCheck, newGame.myHand.get(indexOfCard), newGame.computerHand, newGame.myHand);
            newGame.computerHand = newGame.iAskComputerTheirHand(cardCheck, newGame.computerHand, newGame.myHand);
            System.out.println("Your new hand is: " + newGame.myHand+ "\nFor reference, I have " + newGame.computerHand.size() + " cards left.");
            
            //Checking for duplicates if the hand is non-zero
            if (newGame.myHand.size() !=0){
                newGame.myHand = newGame.checkForDoubles(newGame.myHand);
            }
            if (newGame.computerHand.size() !=0){
                newGame.computerHand = newGame.checkForDoubles(newGame.computerHand);
            }


            System.out.println("After removing pairs/triples/four of a kinds, your hand is now: "+ newGame.myHand);
            System.out.println("For reference, I have " + newGame.computerHand.size() + " cards left.\n");
            
            //Checking to see if the game is over yet
            if (newGame.myHand.size() == 0 || newGame.computerHand.size() == 0){
                break;
            } 

            //Initializing variables for when the computer asks the player for a card
            double randomIndexDoubleFinal = Math.random()*(newGame.computerHand.size());
            int randomIndexFinal = (int)randomIndexDoubleFinal;
            boolean validTings = false;
            String haveCard = "";
            boolean breakFlag = false;
            String cardRefer = newGame.computerHand.get(randomIndexFinal);

            //Checks if the input is valid and if the user is lying or not. If they are, they are re-prompted
            while (!validTings){
                System.out.print("Do you have any " + cardRefer.substring(0,2) + "'s?\nYou must be honest. Say 'Y' for yes and 'N' for no: ");
                haveCard = input.next();
                if (haveCard.equalsIgnoreCase("Y") ){
                    for (int i = 0; i<newGame.myHand.size(); i++){
                        if (cardRefer.substring(0,1).equals(newGame.myHand.get(i).substring(0,1)) || cardRefer.substring(0,2).equals(newGame.myHand.get(i).substring(0,2))){
                            validTings = true;
                            break;
                        }
                        
                    }
                    if (!validTings) {
                        System.out.println("You lied - you don't have it...please try again.");
                    }
                    
                } else if (haveCard.equalsIgnoreCase("N")){
                    for (int i = 0; i<newGame.myHand.size(); i++){
                        if (cardRefer.substring(0,1).equals(newGame.myHand.get(i).substring(0,1)) || cardRefer.substring(0,2).equals(newGame.myHand.get(i).substring(0,2))){
                            breakFlag = true;
                            break;
                        }
                        
                    }
                    validTings = !breakFlag;
                    if (breakFlag) {
                        System.out.println("You lied - you do have it...please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please say either 'Y' or 'N.'");
                }
            }
            //Processing changes to each hand after computer asks player
            newGame.computerHand = newGame.computerAsksMe(newGame.computerHand, haveCard, randomIndexFinal);
            newGame.myHand = newGame.computerAsksMeMyHand(newGame.myHand, haveCard, randomIndexFinal, cardRefer);
            System.out.println("Your new hand is: " + newGame.myHand+ "\nFor reference, I have " + newGame.computerHand.size() + " cards left.");
            
            //Checking for duplicates if the hand is non-zero
            if (newGame.myHand.size() !=0)  {
                newGame.myHand = newGame.checkForDoubles(newGame.myHand);
            }
            if (newGame.computerHand.size() !=0) {
                newGame.computerHand = newGame.checkForDoubles(newGame.computerHand);
            }

            System.out.println("After removing pairs/triples/four of a kinds, your hand is now: "+ newGame.myHand);
            System.out.println("For reference, I have " + newGame.computerHand.size() + " cards left.\n");

            //Checking to see if the game is over yet
            if (newGame.myHand.size() == 0 || newGame.computerHand.size() == 0) break;

    }

    //After game loop is broken, examine which end condition has been met
    if (newGame.myHand.size() == 0){
        System.out.println("CONGRATS! You won the game!!");
    } else if (newGame.computerHand.size() == 0){
        System.out.println("You lost this one...I won!!");
    } else{
        System.out.println("The deck of cards ran out! It was a tie!");
    }
    
    //Asking the user to play again
    System.out.print("Would you like to quit? If not, a new game will start automatically. Say 'Y' for yes and 'N' for no: ");
    String quitInput = input.next();
    boolean finalValid = false;

    //Ensuring the input is valid and changing game state based on input
    while (finalValid == false){
        if (quitInput.equalsIgnoreCase("Y") ){
            finalValid = true;
            System.out.println("Thanks for playing! See ya later!");
            playAgain = false;
        } else if(quitInput.equalsIgnoreCase("N")){
            finalValid = true;
            System.out.println("Whoo! New game starting...\n\n\n\n\n\n");
        } else {
            System.out.println("Invalid input. Please say either 'Y' or 'N.'");
        }
            }
                
            }
    input.close();

}
}
//Go Fish!