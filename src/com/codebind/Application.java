package com.codebind;

import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.GraphStates;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Application implements ActionListener {
    public JFrame frame;
    private GraphicsPanel graphicsPanel;
    private JMenuBar menuBar;
    private JPanel statusBar;
    JPanel instrumentPanel;

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
        ImageIcon iconArrow = new ImageIcon("img/Стрелка.png");
        ImageIcon iconCross = new ImageIcon("img/Крестик.png");
        ImageIcon iconGear = new ImageIcon("img/Шестерёнка.png");
        ImageIcon iconBroom = new ImageIcon("img/Метла.png");

        JMenuItem itemAddVertices = new JMenuItem("Добавить вершины", iconPlus);
        JMenuItem itemConnectVertices = new JMenuItem("Соединить вершины", iconArrow);
        JMenuItem itemDelete = new JMenuItem("Удалить", iconCross);
        JMenu subMenuAlgorithm = new JMenu("Алгоритм");
        JMenuItem itemClearScene = new JMenuItem("Очистить полотно", iconBroom);
        JMenuItem itemTransfer = new JMenuItem("Перемещение", iconArrow);
        JMenuItem itemNothing = new JMenuItem("Перемещение", iconArrow);
        JMenuItem itemConnectAllNodes = new JMenuItem("Соеденить всё", iconArrow);

        subMenuAlgorithm.setIcon(iconGear);
        JMenuItem itemAlgorithm = new JMenuItem("<Конкретный алгоритм>");

        subMenuAlgorithm.add(itemAlgorithm);

        itemAddVertices.addActionListener(this);
        itemConnectVertices.addActionListener(this);
        itemTransfer.addActionListener(this);
        itemClearScene.addActionListener(this);
        itemDelete.addActionListener(this);
        itemNothing.addActionListener(this);
        itemConnectAllNodes.addActionListener(this);

        menuAction.add(itemAddVertices);
        menuAction.add(itemConnectVertices);
        menuAction.add(itemDelete);
        menuAction.add(subMenuAlgorithm);
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

        instrumentPanel = createInstrumentPanel();

        frame.add(graphicsPanel);
        frame.add(instrumentPanel, BorderLayout.NORTH);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
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

    public JPanel createInstrumentPanel(){

        String[] icons = {"img/mouse.png","img/add.png","img/path.png","img/delete.png","img/alg.png","img/clean.png", "img/Перемещение.png"};
        String[] commands = {"Ничего не делать","Добавить вершины","Соединить вершины",
                "Удалить","Алгоритм","Очистить полотно", "Перемещение"};

        JPanel instrumentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for(int i =0; i < icons.length; i++ ){
            Button button = new Button();
            button.setIcon(new ImageIcon(icons[i]));
            button.setActionCommand(commands[i]);
            button.setPreferredSize(new Dimension(32, 32));
            button.addActionListener(this);
            instrumentPanel.add(button);
        }

        return instrumentPanel;
    }
}