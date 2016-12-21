package com.company;

import java.util.Arrays;

import java.util.Scanner;

public class Main
{
    public static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    public static int[][] numBoard = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose your game mode:");
        System.out.println("1: Player vs. Player\n2: Player vs. Computer\n3: Computer vs. Computer");
        int mode = 0;
        Scanner modePicker = new Scanner(System.in);
        while(!(mode > 0 && mode < 4))
        {
            try
            {
                mode = modePicker.nextInt();
                if(mode <= 0 || mode >= 4)
                    throw new Exception();
            }
            catch(Exception e)
            {
                System.out.println("Invalid input.");
            }
        }
        boolean p1Human = (mode > 0 && mode < 3);
        boolean p2Human = (mode > 0 && mode < 2);
        System.out.println("Will PLAYER1 or PLAYER2 go first?");
        String response = "";
        while(!(response.equalsIgnoreCase("player1") || response.equalsIgnoreCase("player2")))
        {
            response = input.nextLine();
            if(!(response.equalsIgnoreCase("player1") || response.equalsIgnoreCase("player2")))
                System.out.println("Invalid input.");
        }
        boolean p1Turn = response.equalsIgnoreCase("player1");
        String p1Letter = "X";
        String p2Letter = "O";
        String winner = null;
        int turns = 0;
        displayBoard();
        while(winner == null)
        {
            if(p1Turn && p1Human || !p1Turn && p2Human)
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
                        if(numBoard[row - 1][column - 1] != -1)
                            throw new Exception();
                        else
                        {
                            numBoard[row - 1][column - 1] = p1Turn ? 0 : 1;
                            board[row - 1][column - 1] = p1Turn ? p1Letter : p2Letter;
                            turns++;
                        }
                        break;
                    }
                    catch(Exception e)
                    {

                    }
                }
                p1Turn = !p1Turn;
            }
            else
            {
                if(turns == 0)
                {
                    int row = (int)(Math.random() * 2) * 2;
                    int column = (int)(Math.random() * 2) * 2;
                    numBoard[row][column] = p1Turn ? 0 : 1;
                    board[row][column] = p1Turn ? p1Letter : p2Letter;
                }
                int[] play = p1Turn ? forcedPlay(0) : forcedPlay(1);
                if(play != null)
                {
                    numBoard[play[0]][play[1]] = p1Turn ? 0 : 1;
                    board[play[0]][play[1]] = p1Turn ? p1Letter : p2Letter;
                }
                else if(true)
                {
                    int[] bestPlay = p1Turn ? calculateFitness(0) : calculateFitness(1);
                }
                System.out.println("Computer plays! Your turn!\n");
                p1Turn = !p1Turn;
            }
            winner = findWinner();
            displayBoard();
        }
        if(winner.equals(p1Letter))
            System.out.println("Player 1 wins!");
        if(winner.equals(p2Letter))
            System.out.println("Player 2 wins!");
    }

    public static int[] forcedPlay(int turnNum)
    {
        for(int i = 0; i <= 1; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(numBoard[j][0] == numBoard[j][1] && numBoard[j][0] == (turnNum + i) % 2 && numBoard[j][2] == -1)
                    return new int[] {j, 2};
                if(numBoard[j][0] == numBoard[j][2] && numBoard[j][0] == (turnNum + i) % 2 && numBoard[j][1] == -1)
                    return new int[] {j, 1};
                if(numBoard[j][2] == numBoard[j][1] && numBoard[j][2] == (turnNum + i) % 2 && numBoard[j][0] == -1)
                    return new int[] {j, 0};
            }
            for(int j = 0; j < 3; j++)
            {
                if(numBoard[0][j] == numBoard[1][j] && numBoard[0][j] == (turnNum + i) % 2 && numBoard[2][j] == -1)
                    return new int[] {2, j};
                if(numBoard[0][j] == numBoard[2][j] && numBoard[0][j] == (turnNum + i) % 2 && numBoard[1][j] == -1)
                    return new int[] {1, j};
                if(numBoard[2][j] == numBoard[1][j] && numBoard[2][j] == (turnNum + i) % 2 && numBoard[0][j] == -1)
                    return new int[] {0, j};
            }
            if(numBoard[0][0] == numBoard[1][1] && numBoard[0][0] == (turnNum + i) % 2 && numBoard[2][2] == -1)
                return new int[] {2, 2};
            if(numBoard[0][0] == numBoard[2][2] && numBoard[0][0] == (turnNum + i) % 2 && numBoard[1][1] == -1)
                return new int[] {1, 1};
            if(numBoard[2][2] == numBoard[1][1] && numBoard[2][2] == (turnNum + i) % 2 && numBoard[0][0] == -1)
                return new int[] {0, 0};
            if(numBoard[0][2] == numBoard[1][1] && numBoard[0][2] == (turnNum + i) % 2 && numBoard[2][0] == -1)
                return new int[] {2, 0};
            if(numBoard[0][2] == numBoard[2][0] && numBoard[0][2] == (turnNum + i) % 2 && numBoard[1][1] == -1)
                return new int[] {1, 1};
            if(numBoard[2][0] == numBoard[1][1] && numBoard[2][0] == (turnNum + i) % 2 && numBoard[0][2] == -1)
                return new int[] {0, 2};
        }
        return null;
    }

    public static int[] calculateFitness(int turn)
    {
        int[][] fitness = new int[3][3];
        int[][] temp = Arrays.copyOf(numBoard, numBoard.length);
        for(int i = 0; i < temp.length; i++)
        {
            for(int j = 0; j < temp[i].length; j++)
            {
                if(temp[i][j] != -1)
                {
                    fitness[i][j] = -1;
                    continue;
                }
                temp[i][j] = turn;
                int numThreats = 0;
                if(i == 0)
                {
                    if((temp[i][j] == temp[1][j] || temp[i][j] == temp[2][j]) && (temp[1][j] != 1 - turn || temp[2][j] != 1 - turn))
                        numThreats++;
                }
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
        if(numBoard[0][0] == numBoard[0][1] && numBoard[0][1] == numBoard[0][2] && numBoard[0][0] != -1)
            return board[0][0];
        else if(numBoard[1][0] == numBoard[1][1] && numBoard[1][1] == numBoard[1][2] && numBoard[1][0] != -1)
            return board[1][0];
        else if(numBoard[2][0] == numBoard[2][1] && numBoard[2][1] == numBoard[2][2] && numBoard[2][0] != -1)
            return board[2][0];
        else if(numBoard[0][0] == numBoard[1][0] && numBoard[1][0] == numBoard[2][0] && numBoard[0][0] != -1)
            return board[0][0];
        else if(numBoard[0][1] == numBoard[1][1] && numBoard[1][1] == numBoard[2][1] && numBoard[0][1] != -1)
            return board[0][1];
        else if(numBoard[0][2] == numBoard[1][2] && numBoard[1][2] == numBoard[2][2] && numBoard[0][2] != -1)
            return board[0][2];
        else if(numBoard[0][0] == numBoard[1][1] && numBoard[1][1] == numBoard[2][2] && numBoard[0][0] != -1)
            return board[0][0];
        else if(numBoard[0][2] == numBoard[1][1] && numBoard[1][1] == numBoard[2][0] && numBoard[0][2] != -1)
            return board[0][2];
        return null;
    }
}
