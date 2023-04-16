package mummymaze;

import agent.Heuristic;

public class HeuristicDistanciaInimigoMaisProximo extends Heuristic<MummyMazeProblem, MummyMazeState> {

    @Override
    public double compute(MummyMazeState state) {

        return state.computeDistanciaInimigoProximo();
    }
    
    @Override
    public String toString(){
        return "Distance to closest enemy";
    }    
}