package com.codebind;

//import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.GraphStates;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Application implements ActionListener {
    public JFrame frame;
    private static GraphicsPanel graphicsPanel;
    private JMenuBar menuBar;
    private JPanel statusBar;
    JPanel toolBar;

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

        ImageIcon iconPlus = new ImageIcon("img/Плюс.png");
        ImageIcon iconDirectedEdge = new ImageIcon("img/Направленное ребро(1).png");
        ImageIcon iconUndirectedEdge = new ImageIcon("img/Ненаправленное ребро(1).png");
        ImageIcon iconCross = new ImageIcon("img/Крестик.png");
        ImageIcon iconGear = new ImageIcon("img/Шестерёнка.png");
        ImageIcon iconBroom = new ImageIcon("img/Метла.png");
        ImageIcon iconTransfer = new ImageIcon("img/Перемещение(1).png");
        ImageIcon iconConnectAllNodes = new ImageIcon("img/Соединить всё.png");

        JMenuItem itemAddVertices = new JMenuItem("Добавить вершины", iconPlus);
        JMenuItem itemAddDirectedEdge = new JMenuItem("Добавить ориентированное ребро", iconDirectedEdge);
        JMenuItem itemAddUndirectedEdge = new JMenuItem("Добавить неориентированное ребро", iconUndirectedEdge);
        JMenuItem itemDelete = new JMenuItem("Удалить вершины и рёбра", iconCross);
        JMenu itemSubMenuAlgorithm = new JMenu("Алгоритм");
        JMenuItem itemClearScene = new JMenuItem("Очистить полотно", iconBroom);
        JMenuItem itemTransfer = new JMenuItem("Перемещение", iconTransfer);
        JMenuItem itemConnectAllNodes = new JMenuItem("Соеденить все вершины", iconConnectAllNodes);

        itemSubMenuAlgorithm.setIcon(iconGear);
        JMenuItem itemKosaraju = new JMenuItem("Косарайю");
        JMenuItem itemDFS = new JMenuItem("Поиск в глубину");

        itemSubMenuAlgorithm.add(itemKosaraju);
        itemSubMenuAlgorithm.add(itemDFS);

        itemAddVertices.addActionListener(this);
        itemAddDirectedEdge.addActionListener(this);
        itemAddUndirectedEdge.addActionListener(this);
        itemTransfer.addActionListener(this);
        itemClearScene.addActionListener(this);
        itemDelete.addActionListener(this);
        itemConnectAllNodes.addActionListener(this);
        itemOpenFile.addActionListener(this);

        menuAction.add(itemAddVertices);
        menuAction.add(itemAddUndirectedEdge);
        menuAction.add(itemAddDirectedEdge);
        menuAction.add(itemDelete);
        menuAction.add(itemSubMenuAlgorithm);
        menuAction.add(itemClearScene);
        menuAction.add(itemTransfer);
        menuAction.add(itemConnectAllNodes);

        ImageIcon iconQuestion = new ImageIcon("img/Вопрос.png");

        JMenuItem itemAboutProgram = new JMenuItem("О программе", iconQuestion);

        menuHelp.add(itemAboutProgram);

        graphicsPanel = new GraphicsPanel();

        statusBar = new JPanel(new GridLayout(1,3,0,0));

        BoundedRangeModel model = new DefaultBoundedRangeModel(30, 0, 0, 200);

        JSlider slider = new JSlider(model);

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

    public static GraphicsPanel getPanel() {
        return graphicsPanel;
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        JLabel labelAction = (JLabel)statusBar.getComponents()[0];

        if (command.equals("Добавить вершины")) {
            labelAction.setText("Добавление вершин");
            graphicsPanel.setGraphState(GraphStates.CREATE_NODE);
        } else if (command.equals("Соединить вершины")) {
            labelAction.setText("Соединение вершин");
            graphicsPanel.setGraphState(GraphStates.CONNECT_NODE);
            GraphEventManager.getInstance().setNodeConnectionType(true);
        } else if (command.equals("Перемещение")) {
            labelAction.setText("Перемещение");
            graphicsPanel.setGraphState(GraphStates.MOVE_NODE);
        } else if (command.equals("Очистить полотно")) {
            labelAction.setText("Очищение полотна");
            GraphEventManager.getInstance().removeGraph();
        } else if (command.equals("Удалить")) {
            labelAction.setText("Удаление вершин и рёбер");
            graphicsPanel.setGraphState(GraphStates.DELETE_NODE);
        } else if (command.equals("Соеденить всё")) {
            labelAction.setText("Соединение всех вершин");
            GraphEventManager.getInstance().connectAllVertices();
        } else if(command.equals("Открыть")){
            labelAction.setText("Открыть");
            InputReader newOne = new InputReader();
            if(newOne.FileOpen){ graphicsPanel.setGraph(new DrawGraph(newOne.initFromData())); }
        } else if (command.equals("Алгоритм")) {
            labelAction.setText("Алгоритм");
            GraphEventManager.getInstance().setAlgorithm(Algorithms.getAlgorithmByName("DFS"));
            graphicsPanel.setGraphState(GraphStates.ALGORITHM);
        }else if (command.equals("Ориентированное ребро")) {
            GraphEventManager.getInstance().setNodeConnectionType(true);
        }else if (command.equals("Неориентированное ребро")) {
            GraphEventManager.getInstance().setNodeConnectionType(false);
        }else if(command.equals("Ничего не делать")){
            labelAction.setText("");
            graphicsPanel.setGraphState(GraphStates.NOTHING);
            Component[] instrumentPanelComponents = instrumentPanel.getComponents();

            for(Component instrumentPanelComponent: instrumentPanelComponents){
                Button button = (Button) instrumentPanelComponent;
                if(button.getState() == ButtonState.ACTIVE){
                    button.setState(ButtonState.INACTIVE);
                    button.changeState();
                    }
                }
        }

        graphicsPanel.updatePanelNodesEdges();
        graphicsPanel.updateUI();
    }

    public JPanel createToolBar(){

        String[] icons = {  "img/add.png",
                "img/Направленное ребро.png",
                "img/Ненаправленное ребро.png",
                "img/delete.png",
                "img/alg.png",
                "img/clean.png",
                "img/Перемещение.png"
        };

        String[] commands = {   "Добавить вершины",
                                "Добавить ориентированное ребро",
                                "Добавить неориентированное ребро",
                                "Удалить вершины и рёбра",
                                "Алгоритм",
                                "Очистить полотно",
                                "Перемещение"
        };

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for(int i =0; i < icons.length; i++ ){
            Button button = new Button();
            button.setIcon(new ImageIcon(icons[i]));
            button.setActionCommand(commands[i]);
            button.setPreferredSize(new Dimension(32, 32));
            button.addActionListener(this);
            toolBar.add(button);
        }

        return toolBar;
    }
}