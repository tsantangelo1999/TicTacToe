package com.company;

import java.util.Arrays;
import java.util.ArrayList;

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
        while(winner == null)
        {
            displayBoard();
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
                else
                {
                    int[] play = p1Turn ? forcedPlay(0) : forcedPlay(1);
                    if(play != null)
                    {
                        numBoard[play[0]][play[1]] = p1Turn ? 0 : 1;
                        board[play[0]][play[1]] = p1Turn ? p1Letter : p2Letter;
                    } else if(true)
                    {
                        int[] bestPlay = p1Turn ? calculateFitness(0) : calculateFitness(1);
                        /*while(true)
                        {
                            int x = (int) (Math.random() * 3);
                            int y = (int) (Math.random() * 3);
                            if(numBoard[x][y] == -1)
                            {
                                numBoard[x][y] = p1Turn ? 0 : 1;
                                board[x][y] = p1Turn ? p1Letter : p2Letter;
                                break;
                            }
                        }*/
                        numBoard[bestPlay[0]][bestPlay[1]] = p1Turn ? 0 : 1;
                        board[bestPlay[0]][bestPlay[1]] = p1Turn ? p1Letter : p2Letter;
                    }
                }
                System.out.println("Computer plays! Your turn!\n");
                p1Turn = !p1Turn;
            }
            turns++;
            winner = findWinner();
        }
        displayBoard();
        if(winner.equals(p1Letter))
            System.out.println("Player 1 wins!");
        else if(winner.equals(p2Letter))
            System.out.println("Player 2 wins!");
        else
            System.out.println("It's a draw!");
    }

    public static int[] forcedPlay(int turnNum)
    {
        for(int i = 0; i <= 1; i++)
        {
            /*for(int j = 0; j < 3; j++)
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
                return new int[] {0, 2};*/
            for (int j = 0; j < numBoard.length; j++)
            {
                for (int k = 0; k < numBoard[j].length; k++)
                {
                    if(numBoard[j][k] == numBoard[j][(k + 1) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[j][(k + 2) % 3] == -1)
                        return new int[] {j, (k + 2) % 3};
                    if(numBoard[j][k] == numBoard[(j + 1) % 3][k] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][k] == -1)
                        return new int[] {(j + 2) % 3, k};
                    if((j + k) % 2 == 0)
                    {
                        if(j == k)
                            if(numBoard[j][k] == numBoard[(j + 1) % 3][(k + 1) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][(k + 2) % 3] == -1)
                                return new int[] {(j + 2) % 3, (k + 2) % 3};
                        if(j != k || j == 1)
                            if(numBoard[j][k] == numBoard[(j + 1) % 3][(k + 2) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][(k + 1) % 3] == -1)
                                return new int[] {(j + 2) % 3, (k + 1) % 3};
                    }
                }
            }
        }
        return null;
    }

    public static int[] calculateFitness(int turn)
    {
        int[][] fitness = new int[3][3];
        int[][] temp = new int[3][3];
        for (int i = 0; i < temp.length; i++)
        {
            for (int j = 0; j < temp[i].length; j++)
            {
                temp[i][j] = numBoard[i][j];
            }
        }
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
                int numOpens = 0;
                if((temp[i][j] == temp[i][(j + 1) % 3] || temp[i][j] == temp[i][(j + 2) % 3]) && !(temp[i][(j + 1) % 3] == turn - 1 || temp[i][(j + 2) % 3] == turn - 1))
                    numThreats++;
                if(!(temp[i][(j + 1) % 3] == turn - 1 || temp[i][(j + 2) % 3] == turn - 1))
                    numOpens++;
                if((temp[i][j] == temp[(i + 1) % 3][j] || temp[i][j] == temp[(i + 2) % 3][j]) && !(temp[(i + 1) % 3][j] == turn - 1 || temp[(i + 2) % 3][j] == turn - 1))
                    numThreats++;
                if(!(temp[(i + 1) % 3][j] == turn - 1 || temp[(i + 2) % 3][j] == turn - 1))
                    numOpens++;
                if((i + j) % 2 == 0)
                {
                    if(i == j)
                    {
                        if((temp[i][j] == temp[(i + 1) % 3][(j + 1) % 3] || temp[i][j] == temp[(i + 2) % 3][(j + 2) % 3]) && !(temp[(i + 1) % 3][(j + 1) % 3] == turn - 1 || temp[(i + 2) % 3][(j + 2) % 3] == turn - 1))
                            numThreats++;
                        if(!(temp[(i + 1) % 3][(j + 1) % 3] == turn - 1 || temp[(i + 2) % 3][(j + 2) % 3] == turn - 1))
                            numOpens++;
                    }
                    if(i != j || i == 1)
                    {
                        if((temp[i][j] == temp[(i + 1) % 3][(j + 2) % 3] || temp[i][j] == temp[(i + 2) % 3][(j + 1) % 3]) && !(temp[(i + 1) % 3][(j + 2) % 3] == turn - 1 || temp[(i + 2) % 3][(j + 1) % 3] == turn - 1))
                            numThreats++;
                        if(!(temp[(i + 1) % 3][(j + 2) % 3] == turn - 1 || temp[(i + 2) % 3][(j + 1) % 3] == turn - 1))
                            numOpens++;
                    }
                }
                fitness[i][j] = numThreats + numOpens;
                if(numThreats > 1)
                    return new int[] {i, j};
                if(numThreats == 1)
                {

                }
                temp[i][j] = -1;
            }
        }
        ArrayList<int[]> results = new ArrayList<>();
        int maxFitness = 0;
        for(int i = 0; i < fitness.length; i++)
        {
            for(int j = 0; j < fitness[i].length; j++)
            {
                if(fitness[i][j] > maxFitness)
                    maxFitness = fitness[i][j];
            }
        }
        for(int i = 0; i < fitness.length; i++)
        {
            for(int j = 0; j < fitness[i].length; j++)
            {
                if(fitness[i][j] == maxFitness)
                    results.add(new int[] {i, j});
            }
        }
        return results.get((int)(Math.random() * results.size()));
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
        else
            topLoop: for(int i = 0; i < numBoard.length; i++)
                for(int j = 0; j < numBoard[i].length; j++)
                {
                    if(numBoard[i][j] == -1)
                        break topLoop;
                    if(i == 2 && j == 2)
                        return "draw";
                }
        return null;
    }
}
