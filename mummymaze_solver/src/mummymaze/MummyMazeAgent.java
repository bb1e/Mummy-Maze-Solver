package mummymaze;

import agent.Agent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class MummyMazeAgent extends Agent<MummyMazeState>{
    
    protected MummyMazeState initialEnvironment;

    
    public MummyMazeAgent(MummyMazeState environemt) {
        super(environemt);
        initialEnvironment = (MummyMazeState) environemt.clone();
        heuristics.add(new HeuristicDistanciaSaida());
        heuristics.add(new HeuristicDistanciaInimigoMaisProximo());
        heuristics.add(new HeuristicDistanciaSaidaEDistanciaInimigoProximo());
        heuristic = heuristics.get(0);
    }
            
    public MummyMazeState resetEnvironment(){
        environment = (MummyMazeState) initialEnvironment.clone();
        return environment;
    }

    //existe uma função q transforma uma string num array
    //igualar a matriz a cada array

    public MummyMazeState readInitialStateFromFile(File file) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(file);

        char[][] matrix = new char [13][13];
        int keyLine=0;
        int keyColumn=0;
        int exitColumn=0;
        int exitLine=0;
        LinkedList<Coordenadas> traps = new LinkedList<Coordenadas>();
        LinkedList<Coordenadas> doors = new LinkedList<Coordenadas>();

        for (int i = 0; i < 13; i++) {
            String line = scanner.nextLine();
            matrix[i]=line.toCharArray();
            for (int j = 0; j < 13; j++) {
                if (matrix[i][j] == 'S') {
                    exitLine = i;
                    exitColumn = j;
                }
                if (matrix[i][j] == 'A') {
                    traps.add(new Coordenadas(i, j));
                    //isTrapPresent = true;
                }
                if (matrix[i][j] == 'C') {
                    keyLine = i;
                    keyColumn = j;
                    //isKeyPresent = true;
                }

                if (matrix[i][j] == '=' || matrix[i][j] == '_' || matrix[i][j] == '"' || matrix[i][j] == ')') {
                    doors.add(new Coordenadas(i, j));
                }

            }
        }
        initialEnvironment = new MummyMazeState(matrix, keyLine, keyColumn, exitLine, exitColumn, traps, doors);
        resetEnvironment();
        return environment;
    }
}
