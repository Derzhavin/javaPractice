package com.codebind;

import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.GraphStates;
import com.codebind.graphComonents.RandomGraphCreator;
import com.codebind.graphComonents.Triangulator;
import com.codebind.viewComponents.DrawGraph;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;


class Application implements ActionListener {
    public JFrame frame;
    private static GraphicsPanel graphicsPanel;
    private JMenuBar menuBar;
    private JPanel statusBar;
    JPanel toolBar;
    private HashMap<String, Button> buttonHashMap = new HashMap<>();
    private HashMap<String, Button> singleActiveButtonHashMap = new HashMap<>();

    public Application() {
        Image iconOfApp = new ImageIcon("img/Иконка приложения.png").getImage();

        frame = new JFrame("Graph application");
        frame.setIconImage(iconOfApp);

        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        menuBar = new JMenuBar();

        JMenu menuFile = new JMenu("Файл");
        JMenu menuAction = new JMenu("Действие");
        JMenu menuHelp = new JMenu("Справка");

        menuFile.setMnemonic(KeyEvent.VK_F);

        menuBar.add(menuFile);
        menuBar.add(menuAction);
        menuBar.add(menuHelp);

        JMenuItem itemOpenFile = new JMenuItem("Открыть", KeyEvent.VK_O);
        JMenuItem itemSaveGraph = new JMenuItem("Сохранить граф", KeyEvent.VK_S);

        itemOpenFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        itemSaveGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));

        menuFile.add(itemOpenFile);
        menuFile.add(itemSaveGraph);


        String[] actioncommands = {"Добавить вершины","Добавить ориентированное ребро","Добавить неориентированное ребро",
                "Удалить вершины и рёбра","Очистить полотно","Перемещение","Соединить все вершины","Создать случайный граф"};

        String[] actionIcons = {"img/Добавить(small).png","img/Направленное ребро(small).png","img/Ненаправленное ребро(small).png",
                "img/Удалить(small).png","img/Очистить(small).png","img/Перемещение(small).png","img/Cоединить все(small).png", "img/Создать случайный граф(small).png"};

        for(int i = 0; i < actioncommands.length; i++){
            JMenuItem action = new JMenuItem(actioncommands[i], new ImageIcon(actionIcons[i]));
            action.addActionListener(this);
            menuAction.add(action);
        }

        JMenu itemSubMenuAlgorithm = new JMenu("Алгоритм");
        itemSubMenuAlgorithm.setIcon(new ImageIcon("img/Алгоритм(small).png"));

        JMenuItem itemKosaraju = new JMenuItem("Косарайю");
        JMenuItem itemDFS = new JMenuItem("Поиск в глубину");

        itemSubMenuAlgorithm.add(itemKosaraju);
        itemSubMenuAlgorithm.add(itemDFS);

        itemDFS.addActionListener(this);
        itemKosaraju.addActionListener(this);
        itemOpenFile.addActionListener(this);
        itemSaveGraph.addActionListener(this);

        menuAction.add(itemSubMenuAlgorithm);

        ImageIcon iconQuestion = new ImageIcon("img/Вопрос.png");
        JMenuItem itemAboutProgram = new JMenuItem("О программе", iconQuestion);

        menuHelp.add(itemAboutProgram);
        itemAboutProgram.addActionListener(this);

        graphicsPanel = new GraphicsPanel();

        statusBar = new JPanel(new GridLayout(1,3,0,0));

        BoundedRangeModel model = new DefaultBoundedRangeModel(500, 0, 0, 2000);

        JSlider slider = new JSlider(model);
        slider.addChangeListener(e -> Algorithms.setDelay(slider.getValue()));

        JLabel labelAction = new JLabel();

        JLabel labelNodes = new JLabel("Nodes: " + 0);
        JLabel labelEdges = new JLabel("Edges: " + 0);

        JPanel panelNodesEdges= new JPanel(new GridLayout(1, 2, 0, 0));

        panelNodesEdges.add(labelNodes);
        panelNodesEdges.add(labelEdges);

        graphicsPanel.setPanelNodesEdges(panelNodesEdges);

        statusBar.add(labelAction);
        statusBar.add(panelNodesEdges);
        statusBar.add(slider);

        toolBar = createToolBar();

        frame.add(graphicsPanel);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);

        GraphEventManager.getInstance().setGraphicsPanel(graphicsPanel);
        Algorithms.init();
    }


    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        JLabel labelAction = (JLabel)statusBar.getComponents()[0];

        if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM) {
            if (!command.equals("Запустить алгоритм") &&
                    !command.equals("Остановить алгоритм") &&
                    !command.equals("Сделать шаг вперед") &&
                    !command.equals("Сделать шаг назад")) {
                Algorithms.currentAlgorithm.reset();
            }
        }

        switch(command) {
            case "Открыть":
                labelAction.setText("Открыть");
                InputReader newOne = new InputReader();

                if(newOne.FileOpen) {
                    graphicsPanel.setGraph(new DrawGraph(newOne.initFromData()));
                    Algorithms.currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
                }

                break;
            case "Сохранить граф":
                OutputWriter saveOne = new OutputWriter();
                saveOne.saveGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "Добавить вершины":
                labelAction.setText("Добавление вершин");
                graphicsPanel.setGraphState(GraphStates.CREATE_NODE);
                break;
            case "Добавить ориентированное ребро":
                labelAction.setText("Добавление ориентированных рёбер");
                graphicsPanel.setGraphState(GraphStates.CONNECT_NODE);
                GraphEventManager.getInstance().setNodeConnectionType(true);
                break;
            case "Добавить неориентированное ребро":
                labelAction.setText("Добавление неориентированных рёбер");
                graphicsPanel.setGraphState(GraphStates.CONNECT_NODE);
                GraphEventManager.getInstance().setNodeConnectionType(false);
                break;
            case "Перемещение":
                labelAction.setText("Перемещение");
                graphicsPanel.setGraphState(GraphStates.MOVE_NODE);
                break;
            case "Очистить полотно":
                labelAction.setText("Очищение полотна");
                GraphEventManager.getInstance().removeGraph();
                break;
            case "Удалить вершины и рёбра":
                labelAction.setText("Удаление вершин и рёбер");
                graphicsPanel.setGraphState(GraphStates.DELETE_NODE);
                break;
            case "Соединить все вершины":
                labelAction.setText("Соединение всех вершин");
                GraphEventManager.getInstance().connectAllVertices();
                break;
            case "Создать случайный граф":
                graphicsPanel.setGraph(RandomGraphCreator.create(
                        1 + new Random(System.currentTimeMillis()).nextInt(16),
                        0.25D, graphicsPanel.getWidth(),
                        graphicsPanel.getHeight(),
                        true));
                Algorithms.currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "Триангулировать":
                Triangulator.triangulate(GraphEventManager.getInstance().getGraph());
                break;
            case "Алгоритм":
                GraphEventManager.getInstance().setState(GraphStates.ALGORITHM);
                Algorithms.currentAlgorithm.reset();
                Algorithms.currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
                Algorithms.currentAlgorithm.setGraphicsPanel(graphicsPanel);
                break;
            case "Запустить алгоритм":
                Algorithms.currentAlgorithm.continueIfStoped();
                break;
            case "Сделать шаг вперед":
                if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM &&
                        Algorithms.currentAlgorithm.isInitialized()) {
                    Algorithms.currentAlgorithm.doStep();
                }
                break;
            case "Сделать шаг назад":
                if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM &&
                        Algorithms.currentAlgorithm.isInitialized()) {
                    Algorithms.currentAlgorithm.doBackwardsStep();
                }
                break;
            case "Остановить алгоритм":
                if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM &&
                        Algorithms.currentAlgorithm.isInitialized()) {
                    Algorithms.currentAlgorithm.stop();
                }
                break;
            case "Поиск в глубину":
                labelAction.setText("DFS");
                Algorithms.selectAlgorithmByName("DFS");
                Algorithms.currentAlgorithm.reset();
                Algorithms.currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
                Algorithms.currentAlgorithm.setGraphicsPanel(graphicsPanel);
                break;
            case "Косарайю":
                labelAction.setText("Kosaraju");
                Algorithms.selectAlgorithmByName("Kosaraju");
                Algorithms.currentAlgorithm.reset();
                Algorithms.currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
                Algorithms.currentAlgorithm.setGraphicsPanel(graphicsPanel);
                break;
            case "О программе":
                labelAction.setText("О программе");
                openHelp();
                break;
            default:
                labelAction.setText("");
                graphicsPanel.setGraphState(GraphStates.NOTHING);
        }

        ButtonClicked(command);

        graphicsPanel.updatePanelNodesEdges();
        graphicsPanel.updateUI();
    }

    public void ButtonClicked(String command) {
        if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM) {
            buttonHashMap.get("Запустить алгоритм").setEnabled(true);
            buttonHashMap.get("Остановить алгоритм").setEnabled(true);
            buttonHashMap.get("Сделать шаг вперед").setEnabled(true);
            buttonHashMap.get("Сделать шаг назад").setEnabled(true);
        }
        else {
            buttonHashMap.get("Запустить алгоритм").setEnabled(false);
            buttonHashMap.get("Остановить алгоритм").setEnabled(false);
            buttonHashMap.get("Сделать шаг вперед").setEnabled(false);
            buttonHashMap.get("Сделать шаг назад").setEnabled(false);
        }

        if (singleActiveButtonHashMap.containsKey(command)) {
            for (Button button : singleActiveButtonHashMap.values()) {
                button.setState(ButtonState.INACTIVE);
                button.changeState();
            }

            singleActiveButtonHashMap.get(command).setState(ButtonState.ACTIVE);
            singleActiveButtonHashMap.get(command).changeState();
        }

        if (command.equals("Запустить алгоритм") && !Algorithms.currentAlgorithm.isInitialized()) {
            buttonHashMap.get("Запустить алгоритм").setState(ButtonState.INACTIVE);
        }
        else if (command.equals("Запустить алгоритм")) {
            buttonHashMap.get("Запустить алгоритм").setState(ButtonState.ACTIVE);
            buttonHashMap.get("Запустить алгоритм").changeState();
        }
        else {
            buttonHashMap.get("Запустить алгоритм").setState(ButtonState.INACTIVE);
            buttonHashMap.get("Запустить алгоритм").changeState();
        }
    }



    public JPanel createToolBar(){
        String[] icons = {"img/Перемещение.png",
                "img/Добавить.png",
                "img/Cоединить все.png",
                "img/Направленное ребро.png",
                "img/Ненаправленное ребро.png",
                "img/Удалить.png",
                "img/Алгоритм.png",
                "img/Очистить.png",
                "img/Создать случайный граф.png",
                "img/Триангуляция.png",
                "img/Запустить алгоритм.png",
                "img/Остановить алгоритм.png",
                "img/Сделать шаг вперед.png",
                "img/Сделать шаг назад.png"
        };

        String[] commands = {
                "Перемещение",
                "Добавить вершины",
                "Соединить все вершины",
                "Добавить ориентированное ребро",
                "Добавить неориентированное ребро",
                "Удалить вершины и рёбра",
                "Алгоритм",
                "Очистить полотно",
                "Создать случайный граф",
                "Триангулировать",
                "Запустить алгоритм",
                "Остановить алгоритм",
                "Сделать шаг вперед",
                "Сделать шаг назад"
        };

        ArrayList<String> singleActiveCommands = new ArrayList<>();
        singleActiveCommands.add("Перемещение");
        singleActiveCommands.add("Добавить вершины");
        singleActiveCommands.add("Добавить ориентированное ребро");
        singleActiveCommands.add("Добавить неориентированное ребро");
        singleActiveCommands.add("Удалить вершины и рёбра");
        singleActiveCommands.add("Алгоритм");


        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(new Color(219, 232, 254));

        for(int i = 0; i < icons.length; i++ ){
            Button button = new Button();
            button.setIcon(new ImageIcon(icons[i]));
            button.setActionCommand(commands[i]);
            button.setPreferredSize(new Dimension(32, 32));
            button.addActionListener(this);

            button.setBackground(new Color(219, 232, 254));
            button.setFocusPainted(false);

            if (commands[i].equals("Запустить алгоритм") ||
                    commands[i].equals("Остановить алгоритм") ||
                    commands[i].equals("Сделать шаг вперед") ||
                    commands[i].equals("Сделать шаг назад")) {
                button.setEnabled(false);
            }

            toolBar.add(button);
            buttonHashMap.put(commands[i], button);
        }

        for (String buttonCommand : buttonHashMap.keySet()) {
            if (singleActiveCommands.contains(buttonCommand)) {
                singleActiveButtonHashMap.put(buttonCommand, buttonHashMap.get(buttonCommand));
            }
        }

        toolBar.setBorder(BorderFactory.createRaisedBevelBorder());

        return toolBar;
    }

    public void openHelp() {
        File htmlFile = new File("html/Help.html");
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch(IOException exception) {
            System.out.println("IO");
        }
    }
}