package com.company;

import java.util.Scanner;

public class Main
{
    public static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    public static int[][] numBoard = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Will the PLAYER or the COMPUTER go first?");
        String response = "";
        while(!(response.equalsIgnoreCase("player") || response.equalsIgnoreCase("computer")))
        {
            response = input.nextLine();
        }
        boolean playerTurn = response.equalsIgnoreCase("player");
        String winner = null;
        displayBoard();
        while(winner == null)
        {
            if(playerTurn)
            {
                while(true)
                {
                    try
                    {
                        System.out.println("Where would you like to play? Input in the form \"rowNumber columnNumber\"");
                        String play = input.nextLine();
                        Scanner readThing = new Scanner(play);
                        readThing.useDelimiter(" ");
                        int row = readThing.nextInt();
                        int column = readThing.nextInt();
                        readThing.close();
                        if(numBoard[row - 1][column - 1] != 0)
                            throw new Exception();
                        else
                            numBoard[row - 1][column - 1] = 1;
                        break;
                    }
                    catch(Exception e)
                    {

                    }
                }
            }
            else
            {
                
            }
        }
    }

    public static void displayBoard()
    {
        System.out.println(" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        System.out.println("___|___|___");
        System.out.println(" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("___|___|___");
        System.out.println(" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
        System.out.println("   |   |   ");
    }

    public static String findWinner()
    {
        if(numBoard[0][0] == numBoard[0][1] && numBoard[0][1] == numBoard[0][2] && numBoard[0][0] != 0)
            return board[0][0];
        else if(numBoard[1][0] == numBoard[1][1] && numBoard[1][1] == numBoard[1][2] && numBoard[1][0] != 0)
            return board[1][0];
        else if(numBoard[2][0] == numBoard[2][1] && numBoard[2][1] == numBoard[2][2] && numBoard[2][0] != 0)
            return board[2][0];
        else if(numBoard[0][0] == numBoard[1][0] && numBoard[1][0] == numBoard[2][0] && numBoard[0][0] != 0)
            return board[0][0];
        else if(numBoard[0][1] == numBoard[1][1] && numBoard[1][1] == numBoard[2][1] && numBoard[0][1] != 0)
            return board[0][1];
        else if(numBoard[0][2] == numBoard[1][2] && numBoard[1][2] == numBoard[2][2] && numBoard[0][2] != 0)
            return board[0][2];
        else if(numBoard[0][0] == numBoard[1][1] && numBoard[1][1] == numBoard[2][2] && numBoard[0][0] != 0)
            return board[0][0];
        else if(numBoard[0][2] == numBoard[1][1] && numBoard[1][1] == numBoard[2][0] && numBoard[0][2] != 0)
            return board[0][2];
        else
            return null;
    }
}
