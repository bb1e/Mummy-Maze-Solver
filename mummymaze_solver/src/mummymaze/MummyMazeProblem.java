package mummymaze;

import agent.Action;
import agent.Problem;

import java.util.LinkedList;
import java.util.List;

//as mumias movem se para o pé do heroi se naõ move se para cima


public class MummyMazeProblem extends Problem<MummyMazeState> {

    protected List<Action> actions;


    public MummyMazeProblem(MummyMazeState initialState) {
        super(initialState);
        actions = new LinkedList<Action>() {{
            add(new ActionDown());
            add(new ActionUp());
            add(new ActionRight());
            add(new ActionLeft());
            add(new ActionDontMove());
        }};

    }

    @Override
    public List<Action<MummyMazeState>> getActions(MummyMazeState state) {
        List<Action<MummyMazeState>> possibleActions = new LinkedList<>();

        if(state.isMorreu()) {

            return possibleActions;

        }

        for (Action action : actions) {

            if (action.isValid(state)) {

                possibleActions.add(action);

            }
        }

        return possibleActions;

    }

    @Override
    public MummyMazeState getSuccessor(MummyMazeState state, Action action){
        MummyMazeState successor = state.clone();
        action.execute(successor);
        return successor;
    }

    @Override
    public boolean isGoal(MummyMazeState state) {

        if (state.getHeroLine() == state.getExitLine() && state.getHeroColumn() == state.getExitC()) {
            return true;
        }
        return false;
    }

    @Override
    public double computePathCost(List<Action> path) {
        return path.size();
    }


}
