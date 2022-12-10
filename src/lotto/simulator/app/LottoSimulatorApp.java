/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lotto.simulator.app;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Thato Keith Kujwane
 */
public class LottoSimulatorApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here: Build Lotto Betting Application

        //Initialize lotto numbers array
        int[] lottoNumbers = new int[6];
        //Instantiate Scanner
        Scanner sc = new Scanner(System.in);
        //Declare variable
        boolean userWantsToPlay, userNumsCountValid;

        do {
            //Generate unique lotto numbers by calling the method responsible for the task
            generateLottoNumbers(lottoNumbers);
            //Declare variable
            int totNumbers;

            do {
                //Prompt user for the total numbers to play
                System.out.print("How many numbers do you want to play? ");
                totNumbers = sc.nextInt();
                
                userNumsCountValid = validateUserNumbersCount(lottoNumbers.length, totNumbers);
                
                //Evaluate user input
                if (!userNumsCountValid) {
                    //Display error message
                    System.out.println("Cannot play " + totNumbers + " of " + lottoNumbers.length + " numbers.");
                }
                //Continue prompting while the user wants to play 0 or more than 6 numbers
            } while (! userNumsCountValid);

            //Get user numbers by calling a method that is responsible for the task
            //This method will prompt the user for numbers and return those numbers in an integer array
            int[] userBetNumbers = getUserBetNumbers(totNumbers);

            bubbleSort(lottoNumbers);
            bubbleSort(userBetNumbers);

            //Prompt user for bet amount
            System.out.print("Please enter bet amount: R");
            double betAmount = sc.nextDouble();

            //Call method to get the correctly guessed numbers, this method extracts the numbers that appear in both arrays
            int[] winningNumbers = getWinningNumbers(lottoNumbers, userBetNumbers);

            //Determine the winning amount for the correctly guessed numbers
            double winningAmount = determineWinningAmount(winningNumbers);

            //Calculate total winning amount
            double totalWinningAmount = winningAmount * (0.5 * betAmount);

            //Call a method that will display the bet information
            displayBetInformation(lottoNumbers, userBetNumbers, winningNumbers, betAmount, totalWinningAmount);

            char userResponse;
            //Determine if user still wants to play
            do {
                System.out.print("Do you want to continue playing? Enter Y or N: ");
                userResponse = sc.next().toUpperCase().charAt(0);

                if (userResponse != 'Y' && userResponse != 'N') {
                    System.out.println("Response " + userResponse + " is not valid. Try again.");
                }
            } while (userResponse != 'Y' && userResponse != 'N');

            userWantsToPlay = userResponse == 'Y';
            //Continue running the application while the user wants to play
        } while (userWantsToPlay);
    }
    
    private static boolean validateUserNumbersCount(int totLottoNumbersCount, int userNumsCount){
        return userNumsCount >= 1 && userNumsCount <= totLottoNumbersCount;
    }

    //This method checks if a given number exists in a given array
    public static boolean examineIfUnique(int numToExamine, int[] numbers) {
        boolean isUnique = true;

        for (int i = 0; i < numbers.length; i++) {
            //Get number at an index i
            int numAtIndex = numbers[i];

            //Compare the numberAtIndex with the numbers we're looking for
            if (numAtIndex == numToExamine) {
                //Once we find a matching number in the array, it means that our 'searchKey' numToExamine is not unique
                // since there exists a similar elemenyt in the array
                isUnique = false;
                break;
            }
        }
        return isUnique;
    }

    //This method populates an array of lotto numbers with unique random numbers
    public static void generateLottoNumbers(int[] lottoNumbers) {
        for (int i = 0; i < lottoNumbers.length; i++) {
            //For each array index

            //We generate a random number
            int randomNumber = 1 + (int) Math.floor(Math.random() * (49));

            //For a number to be unique, it must not exist in a given list / array of numbers
            //We call a method to check if the generated number is unique or not
            boolean isUnique = examineIfUnique(randomNumber, lottoNumbers);

            //While the number is not unique, generate a new number and check if it's unique or not
            //This loop will keep on generatin g random numbers until it finds a unique number
            while (!isUnique) {
                randomNumber = 1 + (int) Math.floor(Math.random() * (49));
                isUnique = examineIfUnique(randomNumber, lottoNumbers);
            }

            //Once we find a unique number, we then store it in the array of lotto numbers
            lottoNumbers[i] = randomNumber;
        }
    }

    //This method prompt the user for a 'totalNumbers' of unique numbers
    public static int[] getUserBetNumbers(int totalNumbers) {
        //Initialize array that will hold the userNumbers
        int[] userNumbers = new int[totalNumbers];
        //Instantiate Scanner to read input
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < totalNumbers; i++) {
            //For each array index

            int userNumber;
            boolean isUnique;

            do {
                //Prompt user for unique bet number
                System.out.print("Enter a bet number between 1 and 49: ");
                userNumber = sc.nextInt();

                //Check if the number is really unique
                isUnique = examineIfUnique(userNumber, userNumbers);

                if (!isUnique && (userNumber != 0)) {
                    //If the betNumber is not unique, display error message
                    System.out.println("The number " + userNumber + " has already been captured.");
                } else if ((userNumber < 1) || (userNumber > 49)) {
                    //Otherwise if the number exceeds the range, display an error message
                    System.out.println("Bet number cannot be " + userNumber + ", it must be between 1 and 49.");
                }
                //Run this section while the user number is not unique, or does not fall within range
            } while (!isUnique || (userNumber < 1) || (userNumber > 49));

            //Once the user enters an acceptible number, store it in the userNumbers array
            userNumbers[i] = userNumber;
        }

        //Return array of user numbers
        return userNumbers;
    }

    //This method simply just counts how many numbers a user guessed correctly
    public static int countWinningNumbers(int[] lottoNumbers, int[] userBetNumbers) {
        //Initialize counter
        int counter = 0;

        //For each array index
        for (int i = 0; i < userBetNumbers.length; i++) {
            //Get user bet number from the array of userBetNumbers
            int betNumber = userBetNumbers[i];

            //Search for the betNumber in the array of lottoNumbers
            for (int j = 0; j < lottoNumbers.length; j++) {
                //For each array index, get a lotto number
                int lottoNum = lottoNumbers[j];

                //If we find a lottoNumber that matches the betNumber
                if (lottoNum == betNumber) {
                    //It means the user has guessed this correctly, we count it.
                    counter++;
                }
            }
        }
        //Return how many numbers were guessed correctly
        return counter;
    }

    //This method gets the winning numbers from the lottoNumbers array
    //It basically compares the lottoNumbers array with the userBetNumbers array then extract matching numbers
    public static int[] getWinningNumbers(int[] lottoNumbers, int[] userBetNumbers) {
        //Count how many numbers match by calling the responsible method
        int numsWon = countWinningNumbers(lottoNumbers, userBetNumbers);

        //This index will be used to go through the winningNumbers array
        int index = 0;

        //Initialize array that will hold the correctly guessed numbers
        int[] wonNumbers = new int[numsWon];

        //Find matching numbers
        for (int i = 0; i < userBetNumbers.length; i++) {
            //Get number from userBetNumbers array
            int betNum = userBetNumbers[i];

            for (int j = 0; j < lottoNumbers.length; j++) {
                //Get number from lottoNumbers array`
                int lottoNum = lottoNumbers[j];

                //Once it matches, it means that the number has won
                if (lottoNum == betNum) {
                    //Add the number to the array of winningNumbers
                    wonNumbers[index++] = lottoNum;
                }
            }
        }
        return wonNumbers;
    }

    //This method determines the winning amount when given an array of winning numbers
    public static double determineWinningAmount(int[] winningNumbers) {
        double amount = 0;

        //For each wonNumber in the array of winning numbers
        for (int wonNum : winningNumbers) {
            //Increment the amount by a value double the wonNumber
            amount += wonNum * 2;
        }

        return amount;
    }

    //This method performs the bubble sorting technique on a given array of integers
    public static void bubbleSort(int[] numbers) {
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length - 1; j++) {
                int numAtJ = numbers[j];
                int nextNum = numbers[j + 1];

                if (numAtJ > nextNum) {
                    numbers[j] = nextNum;
                    numbers[j + 1] = numAtJ;
                }

            }
        }
    }

    //This method displays the bet summary
    public static void displayBetInformation(int[] lottoNumbers, int[] userBetNumbers, int[] winningNumbers, double betAmount, double totalWinningAmount) {
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("The generated lotto numbers are: "
                + Arrays.toString(lottoNumbers) + "\n"
                + "The user numbers are: " + Arrays.toString(userBetNumbers) + "\n"
                + "The user has correctly guessed " + winningNumbers.length + " numbers.\n"
                + "The correctly gussed numbers are: " + Arrays.toString(winningNumbers) + "\n"
                + "The user has made a bet of R" + df.format(betAmount)
                + " and has won a total of R" + df.format(totalWinningAmount) + "\n");
    }
}
