package mummymaze;

import agent.Heuristic;

public class HeuristicDistanciaSaidaEDistanciaInimigoProximo extends Heuristic<MummyMazeProblem, MummyMazeState> {

    @Override
    public double compute(MummyMazeState state) {

        return state.computeDistanciaSaida() + state.computeDistanciaInimigoProximo();
    }

    @Override
    public String toString(){
        return "Distance to exit + closest enemy";
    }
}
