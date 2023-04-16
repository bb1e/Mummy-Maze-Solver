package searchmethods;

import agent.Action;
import agent.Problem;
import agent.Solution;
import agent.State;

import java.util.List;

public class IterativeDeepeningSearch extends DepthLimitedSearch {


    @Override
    public Solution search(Problem problem) {
        statistics.reset();
        statistics.numGeneratedSates = 0; //specific to this algorithm
        stopped = false;
        limit = 0;
        Solution solution;
        int previousNumGeneratedStates;
        do {
            previousNumGeneratedStates = statistics.numGeneratedSates;
            solution = graphSearch(problem);
            limit++;
        } while (solution == null && statistics.numGeneratedSates != previousNumGeneratedStates);

        return solution;
    }

    @Override
    protected Solution graphSearch(Problem problem) {
        frontier.clear();
        frontier.add(new Node(problem.getInitialState()));
        statistics.numGeneratedSates++; //specific to this algorithm

        while (!frontier.isEmpty() && !stopped) {
            Node n = (Node) frontier.poll();
            int numSuccessorsSize = 0;
            if (n.getDepth() < limit) {
                State state = n.getState();
                List<Action> actions = problem.getActions(state);
                numSuccessorsSize = actions.size();
                for(Action action : actions){
                    State successor = problem.getSuccessor(state, action);
                    if (n.getDepth() == limit - 1 && problem.isGoal(successor)) {
                        Node successorNode = new Node(successor, n);
                        return new Solution(problem, successorNode);
                    }
                    addSuccessorToFrontier(successor, n);
                }
            }
            computeStatistics(numSuccessorsSize);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Iterative deepening search";
    }
}


