package mummymaze;

import agent.Heuristic;

public class HeuristicDistanciaSaida extends Heuristic<MummyMazeProblem, MummyMazeState>{

    @Override
    public double compute(MummyMazeState state){

        return state.computeDistanciaSaida();
    }
    
    @Override
    public String toString(){
        return "Distance to the exit";
    }
}