/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.a2_214;

import GeneticAlgorithm.GA;
import GeneticAlgorithm.GeneticAlgorithm;
import BackTrackingAlgorithm.BA;
import BackTrackingAlgorithm.BacktrackingAlgorithm;
import java.util.Scanner;

/**
 *
 * @author c3rea
 */
public class Main {

    public static void main(String[] args) {
     Scanner scanner = new Scanner(System.in);
     boolean running = true;
     String choice;
     
     while(running){
       
        System.out.println("\nPlease choose one of the following options");

     
        System.out.println("Press 1 to run and graph the nQueens on a chessboard");
        System.out.println("Press 2 to run multiple trials and collect data from the algorithm");
        System.out.println("Press 3 to analyze and display the results after running multiple trials ");
        System.out.println("Press 4 to race the algorithms to find out which one is more efficient ");
        System.out.println("Press Q or q to exit");
        choice = scanner.next();
        
        switch(choice){
            
            case "1":
                runAlgorithms();
                break;
            case "2":
                analyzeAlgorithms();
                break;
            case "3":
                AlgorithmComparison.createComparisonTable();
                break;
            case "4":
                RaceNQueens.main(null);
                break;
            case "Q":
                running =false;
                break;
            case "q":
                running =false;
                break;
            default: System.out.println("Not a valid input");
        } 
     }
     
        
        
    }
    
    
    public static void runAlgorithms(){     
       int algo;
       Scanner scanner = new Scanner(System.in);
       boolean running = false;     
       do{
            System.out.println("\nPlease choose an algorithm to run");
            System.out.println("Press 1 to solve Nqueens using Backtracking");
            System.out.println("Press 2 to solve Nqueens using a Genetic Algorithm");
            algo = scanner.nextInt();
            
          switch(algo){
            case 1:
                BA ba = new BA();
                ba.runBA();
                running= false;
                break;
                
            case 2:
                GA ga = new GA();
                ga.runGA();
                running = false;
                break;
                
            default:System.out.println("Not a valid choice");
                running =true;       
          }
       }while(running); 
    }
    
    public static void analyzeAlgorithms(){     
       int algoa;
       Scanner scanner = new Scanner(System.in);
       boolean running = false;     
       do{
            System.out.println("\nPlease choose an algorithm to Analyze");
            System.out.println("Press 1 to analyze the  Genetic  Algorithm");
            System.out.println("Press 2 to analyze the Backtracking Algorithm");
            algoa = scanner.nextInt();
            
          switch(algoa){
            case 1:
                GeneticAlgorithm.runMultipleTrials();
                running= false;
                break;
                
            case 2:
               BacktrackingAlgorithm.runMultipleTrials();
                running = false;
                break;
                
            default:System.out.println("Not a valid choice");
                running =true;       
          }
       }while(running); 
    }
    
}
