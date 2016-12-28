package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main
{
    private static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    private static int[][] numBoard = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose your game mode:");
        System.out.println("1: Player vs. Player\n2: Player vs. Computer\n3: Computer vs. Computer");
        int mode = 0;
        while(!(mode > 0 && mode < 4))
        {
            try
            {
                String modeStr = input.nextLine();
                mode = Integer.parseInt(modeStr);
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
                        System.out.println("Invalid input.");
                    }
                }
                p1Turn = !p1Turn;
            }
            else
            {
                if(turns == 0)
                {
                    int row = 2;//(int)(Math.random() * 2) * 2;
                    int column = 0;//(int)(Math.random() * 2) * 2;
                    numBoard[row][column] = p1Turn ? 0 : 1;
                    board[row][column] = p1Turn ? p1Letter : p2Letter;
                }
                else
                {
                    ArrayList<int[]>[] play = p1Turn ? forcedPlay(0) : forcedPlay(1);
                    if(play[0].size() != 0)
                    {
                        int rand = (int)(Math.random() * play[0].size());
                        numBoard[play[0].get(rand)[0]][play[0].get(rand)[1]] = p1Turn ? 0 : 1;
                        board[play[0].get(rand)[0]][play[0].get(rand)[1]] = p1Turn ? p1Letter : p2Letter;
                    } 
                    else if(play[1].size() != 0)
                    {
                        int rand = (int)(Math.random() * play[1].size());
                        numBoard[play[1].get(rand)[0]][play[1].get(rand)[1]] = p1Turn ? 0 : 1;
                        board[play[1].get(rand)[0]][play[1].get(rand)[1]] = p1Turn ? p1Letter : p2Letter;
                    }
                    else
                    {
                        int[] bestPlay = p1Turn ? calculateFitness(0) : calculateFitness(1);
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
        if(winner.equals(p1Letter) && mode == 1)
            System.out.println("Player 1 wins!");
        else if(winner.equals(p1Letter) && mode == 2)
            System.out.println("Computer wins!");
        else if(winner.equals(p2Letter) && mode == 2)
            System.out.println("Computer wins!");
        else if(winner.equals(p2Letter) && mode == 1)
            System.out.println("Player 2 wins!");
        else //no need for if player wins in player vs computer
            System.out.println("It's a draw!");
        System.out.println("Would you like to play again (YES/NO)");
        response = "";
        while(!(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")))
        {
            response = input.nextLine();
            if(!(response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")))
                System.out.println("Invalid input.");
        }
        if(response.equalsIgnoreCase("yes"))
        {
            board = new String[][] {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
            numBoard = new int[][] {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
            main(null);
        }
    }

    private static ArrayList<int[]>[] forcedPlay(int turnNum)
    {
        ArrayList<int[]>[] result = new ArrayList[2];
        result[0] = new ArrayList<>();
        result[1] = new ArrayList<>();
        for(int i = 0; i <= 1; i++)
        {
            for (int j = 0; j < numBoard.length; j++)
            {
                for (int k = 0; k < numBoard[j].length; k++)
                {
                    if(numBoard[j][k] == numBoard[j][(k + 1) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[j][(k + 2) % 3] == -1)
                        result[i].add(new int[] {j, (k + 2) % 3});
                    if(numBoard[j][k] == numBoard[(j + 1) % 3][k] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][k] == -1)
                        result[i].add(new int[] {(j + 2) % 3, k});
                    if((j + k) % 2 == 0)
                    {
                        if(j == k)
                            if(numBoard[j][k] == numBoard[(j + 1) % 3][(k + 1) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][(k + 2) % 3] == -1)
                                result[i].add(new int[] {(j + 2) % 3, (k + 2) % 3});
                        if(j != k || j == 1)
                            if(numBoard[j][k] == numBoard[(j + 1) % 3][(k + 2) % 3] && numBoard[j][k] == (turnNum + i) % 2 && numBoard[(j + 2) % 3][(k + 1) % 3] == -1)
                                result[i].add(new int[] {(j + 2) % 3, (k + 1) % 3});
                    }
                }
            }
        }
        return result;
    }

    private static int[] calculateFitness(int turn)
    {
        int[][] fitness = new int[3][3];
        for(int i = 0; i < numBoard.length; i++)
        {
            for(int j = 0; j < numBoard[i].length; j++)
            {
                if(numBoard[i][j] != -1)
                {
                    fitness[i][j] = -1;
                    continue;
                }
                numBoard[i][j] = turn;
                int numThreats = 0;
                int numOpens = 0;
                if((numBoard[i][j] == numBoard[i][(j + 1) % 3] || numBoard[i][j] == numBoard[i][(j + 2) % 3]) && !(numBoard[i][(j + 1) % 3] == 1 - turn || numBoard[i][(j + 2) % 3] == 1 - turn))
                    numThreats++;
                if(!(numBoard[i][(j + 1) % 3] == 1 - turn || numBoard[i][(j + 2) % 3] == 1 - turn))
                    numOpens++;
                if((numBoard[i][j] == numBoard[(i + 1) % 3][j] || numBoard[i][j] == numBoard[(i + 2) % 3][j]) && !(numBoard[(i + 1) % 3][j] == 1 - turn || numBoard[(i + 2) % 3][j] == 1 - turn))
                    numThreats++;
                if(!(numBoard[(i + 1) % 3][j] == 1 - turn || numBoard[(i + 2) % 3][j] == 1 - turn))
                    numOpens++;
                if((i + j) % 2 == 0)
                {
                    if(i == j)
                    {
                        if((numBoard[i][j] == numBoard[(i + 1) % 3][(j + 1) % 3] || numBoard[i][j] == numBoard[(i + 2) % 3][(j + 2) % 3]) && !(numBoard[(i + 1) % 3][(j + 1) % 3] == 1 - turn || numBoard[(i + 2) % 3][(j + 2) % 3] == 1 - turn))
                            numThreats++;
                        if(!(numBoard[(i + 1) % 3][(j + 1) % 3] == 1 - turn || numBoard[(i + 2) % 3][(j + 2) % 3] == 1 - turn))
                            numOpens++;
                    }
                    if(i != j || i == 1)
                    {
                        if((numBoard[i][j] == numBoard[(i + 1) % 3][(j + 2) % 3] || numBoard[i][j] == numBoard[(i + 2) % 3][(j + 1) % 3]) && !(numBoard[(i + 1) % 3][(j + 2) % 3] == 1 - turn || numBoard[(i + 2) % 3][(j + 1) % 3] == 1 - turn))
                            numThreats++;
                        if(!(numBoard[(i + 1) % 3][(j + 2) % 3] == 1 - turn || numBoard[(i + 2) % 3][(j + 1) % 3] == 1 - turn))
                            numOpens++;
                    }
                }
                fitness[i][j] = numThreats + numOpens;
                if(numThreats > 1)
                    return new int[] {i, j};
                if(numThreats == 1)
                {
                    ArrayList<int[]>[] counter = forcedPlay(1 - turn);
                    numBoard[counter[1].get(0)[0]][counter[1].get(0)[1]] = 1 - turn;
                    ArrayList<int[]>[] currentCounter = forcedPlay(turn);
                    fitness[i][j] -= currentCounter[1].size();
                    numBoard[counter[1].get(0)[0]][counter[1].get(0)[1]] = -1;
                }
                numBoard[i][j] = -1;
            }
        }
        ArrayList<int[]> results = new ArrayList<>();
        int maxFitness = 0;
        for(int[] i : fitness)
        {
            for(int j : i)
            {
                if(j > maxFitness)
                    maxFitness = j;
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

    private static void displayBoard()
    {
        System.out.println(" " + board[0][0] + " | " + board[0][1] + " | " + board[0][2]);
        System.out.println("___|___|___");
        System.out.println(" " + board[1][0] + " | " + board[1][1] + " | " + board[1][2]);
        System.out.println("___|___|___");
        System.out.println(" " + board[2][0] + " | " + board[2][1] + " | " + board[2][2]);
        System.out.println("   |   |   ");
    }

    private static String findWinner()
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
