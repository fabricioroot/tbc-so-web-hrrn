package manager;

import java.util.Vector;
import bean.Process;

/**
 *
 * @author Fabrício Reis
 */
public class HRRNAlgorithm {
    
    public HRRNAlgorithm() {
    }
    
    /* This method implements the algorithm HRRN (Highest Response Ratio Next) of processes management.
     * It returns the position where the process has the highest ratio is.
     */
    public int toExecute(Vector<Process> processes) {
        int out = 0;
        float aux = 0;
        
        for(int i = 0; i <= (processes.size() - 1); i++) {
            if(aux < processes.elementAt(i).getPriority()) {
                aux = processes.elementAt(i).getPriority();
                out = i;
            }
        }
        return out;
    }
    
    /*
     * This method finds and stores in a Vector<Integer> the positions of the possible processes being dispatched.
     * The last position of the returned vector stores the position of the process which is gonna be dispatched.
     */
    public Vector<Integer> findPositionsPossibleProcesses(Vector<Process> processes) {
        Vector<Integer> positions = new Vector<Integer>();
        float aux = 0;
        
        for(int i = 0; i <= (processes.size() - 1); i++) {
            if(aux < processes.elementAt(i).getPriority()) {
                aux = processes.elementAt(i).getPriority();
                positions.add(i);
            }
        }
        return positions;
    }
    
    /*
     * This method calculates the priorities of the processes in 'Vector<Process> processes'
     * and return it
     */
    
    public Vector<Process> calculatePriorities(Vector<Process> processes) {
        Vector<Process> out = new Vector<Process>();
        out = processes;
        float aux = 0;
        
        for(int i = 0; i <= (processes.size() - 1); i++) {
            aux = (1 + (processes.elementAt(i).getWaitingTime() / processes.elementAt(i).getLifeTime())); 
            out.elementAt(i).setPriority(aux);
        }
        return out;
    }
}
