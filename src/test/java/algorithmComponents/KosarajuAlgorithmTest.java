package algorithmComponents;

import IO.InputReader;
import Managers.AlgorithmEventManager;
import Managers.GraphEventManager;
import ViewCompomemts.DrawGraph;
import Widgets.Button;
import graphComponents.GraphStates;
import graphComponents.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Widgets.GraphicsPanel;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class KosarajuAlgorithmTest {
    private static GraphicsPanel graphicsPanel = new GraphicsPanel();
    private HashMap<String, Button> buttonHashMap = new HashMap<String, Button>();
    private JLabel labelAction = new JLabel();
    private String dirKosaraju = "resources/testing/testing_Kosaraju/";

    @BeforeEach
    void simulate_a_frame() {
        String commands[] = {"Запустить алгоритм",
                    "Остановить алгоритм",
                    "Сделать шаг вперед",
                    "Сделать шаг назад",
                    "Отмена",
                    "Отмена отмены",
                    "Результат",
                    "Начало"};

        for(int i = 0; i < commands.length; i++ ) {
            Button button = new Button();
            buttonHashMap.put(commands[i], button);
        }

        Algorithms.init();
        GraphEventManager.getInstance().setGraphicsPanel(graphicsPanel);
        AlgorithmEventManager.getInstance().setGraphicsPanel(graphicsPanel);
        AlgorithmEventManager.getInstance().setDisplay(labelAction);
        AlgorithmEventManager.getInstance().setAlgorithmButtons(new AlgorithmButtons(buttonHashMap.get("Запустить алгоритм"),
                buttonHashMap.get("Остановить алгоритм"),
                buttonHashMap.get("Сделать шаг вперед"),
                buttonHashMap.get("Сделать шаг назад"),
                buttonHashMap.get("Результат"),
                buttonHashMap.get("Начало")));
        buttonHashMap.get("Запустить алгоритм").setEnabled(true);
        buttonHashMap.get("Остановить алгоритм").setEnabled(true);
        buttonHashMap.get("Сделать шаг вперед").setEnabled(true);
        buttonHashMap.get("Сделать шаг назад").setEnabled(false);
        buttonHashMap.get("Отмена").setEnabled(false);
        buttonHashMap.get("Отмена отмены").setEnabled(false);
        buttonHashMap.get("Результат").setEnabled(true);
        buttonHashMap.get("Начало").setEnabled(false);
    }

    void tuneKosarajuAlgorithm() {
        labelAction.setText("Kosaraju");
        Algorithms.init();
        Algorithms.selectAlgorithmByName("Kosaraju");
        GraphEventManager.getInstance().setState(GraphStates.ALGORITHM);
        AlgorithmEventManager.getInstance().reset();
        AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
        Node selectedNode = GraphEventManager.getInstance().getGraph().getNodes().get(0);
        AlgorithmEventManager.getInstance().getAlgorithm().initialize(selectedNode);
        AlgorithmEventManager.getInstance().sendCommand("Результат");
    }

    ArrayList<ArrayList<String>> parse(String answerFileName) {
        File answerFile = new File(answerFileName);
        String line = null;

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(answerFile)))){
            line = reader.readLine();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] strComponents = line.split("\\|");

        ArrayList<ArrayList<String>> namesOfnodesInComponents = new ArrayList<>();

        for(String strComponent: strComponents) {
            String[] strNamesOfNodes = strComponent.split(",");
            ArrayList<String> namesOfNodes = new ArrayList<>();
            for(String strNameOfNode: strNamesOfNodes) {
                namesOfNodes.add(strNameOfNode);
            }
            namesOfnodesInComponents.add(namesOfNodes);
        }

        return namesOfnodesInComponents;
    }

    Node getNodeFromUnChecked(HashSet<Node> unChecked, String nameOfNode) {
        for(Node node:unChecked) {
            if (node.getView().getName().equals(nameOfNode)) {
                return node;
            }
        }
        return null;
    }
    void checkDrawNodes(ArrayList<ArrayList<String>> namesOfNodesInComponents) {
        HashSet<Node> unChecked = new HashSet<>();
        HashSet<Color> colorsOfComponents = new HashSet<>();

        for(Node node :GraphEventManager.getInstance().getGraph().getNodes()) {
            unChecked.add(node);
        }

        while(!unChecked.isEmpty() && !namesOfNodesInComponents.isEmpty()) {
            ArrayList<String> namesOfNodesInComponent = namesOfNodesInComponents.remove(namesOfNodesInComponents.size()-1);

            String nameOfNode = namesOfNodesInComponent.remove(namesOfNodesInComponent.size()-1);

            Node memberNode = getNodeFromUnChecked(unChecked, nameOfNode);

            if (memberNode == null) {
                fail();
            }

            unChecked.remove(memberNode);

            Color componentColor = memberNode.getView().getColor();

            if (!colorsOfComponents.isEmpty()) {
                if (colorsOfComponents.contains(componentColor)) {
                    fail();
                }
            }

            colorsOfComponents.add(componentColor);

            if (namesOfNodesInComponent.size() > 1) {
                while (!namesOfNodesInComponent.isEmpty() && !unChecked.isEmpty()) {
                    nameOfNode = namesOfNodesInComponent.remove(namesOfNodesInComponent.size() - 1);
                    memberNode = getNodeFromUnChecked(unChecked, nameOfNode);

                    if (memberNode == null) {
                        fail();
                    }

                    if (componentColor != memberNode.getView().getColor()) {
                        fail();
                    }

                    unChecked.remove(memberNode);
                }
            }
        }
    }
   @Test
    void lots_of_empty_stack_in_DFS_1() {
       InputReader newOne = new InputReader(dirKosaraju+"test_1/dataset_1.xml");

       if (!newOne.FileOpen) {
           fail();
       }

       DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

       tuneKosarajuAlgorithm();

       checkDrawNodes(parse(dirKosaraju+"test_1/answer_1.txt"));
   }

    @Test
    void one_node_2() {
        InputReader newOne = new InputReader(dirKosaraju+"test_2/dataset_2.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_2/answer_2.txt"));
    }

    @Test
    void lots_of_disconennected_nodes_3() {
        InputReader newOne = new InputReader(dirKosaraju+"test_3/dataset_3.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_3/answer_3.txt"));
    }

    @Test
    void two_disconennected_components_4() {
        InputReader newOne = new InputReader(dirKosaraju+"test_4/dataset_4.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_4/answer_4.txt"));
    }

    @Test
    void three_conennected_components_5() {
        InputReader newOne = new InputReader(dirKosaraju+"test_5/dataset_5.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_5/answer_5.txt"));
    }

    @Test
    void two_components_connected_by_two_parallel_edges_6() {
        InputReader newOne = new InputReader(dirKosaraju+"test_6/dataset_6.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_6/answer_6.txt"));
    }

    @Test
    void one_big_connected_component_7() {
        InputReader newOne = new InputReader(dirKosaraju+"test_7/dataset_7.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_7/answer_7.txt"));
    }

    @Test
    void one_component_consisting_of_components_8() {
        InputReader newOne = new InputReader(dirKosaraju+"test_8/dataset_8.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_8/answer_8.txt"));
    }

    @Test
    void chain_of_components_each_consisting_of_one_node_9() {
        InputReader newOne = new InputReader(dirKosaraju+"test_9/dataset_9.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_9/answer_9.txt"));
    }

    @Test
    void closed_chain_of_nodes_it_is_one_component_10() {
        InputReader newOne = new InputReader(dirKosaraju+"test_10/dataset_10.xml");

        if (!newOne.FileOpen) {
            fail();
        }

        DrawGraph drawGraph = new DrawGraph(newOne.initFromData());

        tuneKosarajuAlgorithm();
        checkDrawNodes(parse(dirKosaraju+"test_10/answer_10.txt"));
    }
}