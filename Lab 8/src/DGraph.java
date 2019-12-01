import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class DGraph {
    //lista de muchii
    private List<List<Integer>> neighbourList;
    //lista de noduri
    private List<Integer> nodes;
    //nrOfNoudes
    private int nrOfNodes;

    DGraph(int nodeCount) {
        //initializare noduri si lita de vecini
        this.neighbourList = new ArrayList<>(nodeCount);
        this.nodes = new ArrayList<>();
        this.nrOfNodes = nodeCount;

        for (int i = 0; i < nodeCount; i++) {
            this.neighbourList.add(new ArrayList<>());
            this.nodes.add(i);
        }

        this.addHamiltonianAndRandomNeighbours();
    }

    void addEdge(int a, int b) {
        //adaugare muchie (de la a la b)
        this.neighbourList.get(a).add(b);
    }

    List<Integer> neighboursOf(int node) {
        return this.neighbourList.get(node);
    }

    List<Integer> getNodes() {
        return nodes;
    }

    int size() {
        return this.neighbourList.size();
    }

    void addHamiltonianAndRandomNeighbours() {

        List<Integer> nodes = this.getNodes();

        java.util.Collections.shuffle(nodes);

        //here we generate a  hammiltonian cycle
        for (int i = 1; i < nodes.size(); i++) {
            this.addEdge(nodes.get(i - 1), nodes.get(i));
        }

        this.addEdge(nodes.get(nodes.size() - 1), nodes.get(0));


        //generate random edges
        Random random = new Random();

        for (int i = 0; i < nrOfNodes / 2; i++) {
            int nodeA = random.nextInt(nrOfNodes - 1);
            int nodeB = random.nextInt(nrOfNodes - 1);

            this.addEdge(nodeA, nodeB);
        }
    }

}