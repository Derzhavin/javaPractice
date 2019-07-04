package com.codebind;

import com.codebind.algorithmComponents.DFSAlgorithm;
import com.codebind.graphComonents.GraphEventManager;
import com.codebind.graphComonents.GraphStates;
import com.codebind.viewComponents.DrawGraph;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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


        String[] actioncommands = {"Добавить вершины","Добавить ориентированное ребро","Добавить неориентированное ребро",
                "Удалить вершины и рёбра","Очистить полотно","Перемещение","Соединить все вершины"};

        String[] actionIcons = {"img/Добавить(small).png","img/Направленное ребро(small).png","img/Ненаправленное ребро(small).png",
                "img/Удалить(small).png","img/Очистить(small).png","img/Перемещение(small).png","img/Cоединить все(small).png"};

        for(int i= 0; i< actioncommands.length; i++){
            JMenuItem action = new JMenuItem(actioncommands[i],new ImageIcon(actionIcons[i]));
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
            Algorithms.currentAlgorithm.reset();
        }


        switch(command) {
            case "Открыть":
                labelAction.setText("Открыть");
                InputReader newOne = new InputReader();
                if(newOne.FileOpen){ graphicsPanel.setGraph(new DrawGraph(newOne.initFromData()));}
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
            case "Алгоритм":
                GraphEventManager.getInstance().setState(GraphStates.ALGORITHM);
                Algorithms.currentAlgorithm.sayHello();
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

    public void ButtonClicked(String command){
        if(command.equals("Очистить полотно") || command.equals("Соединить все вершины")) return;

        Component[] mas = frame.getContentPane().getComponents();
        JPanel panel = (JPanel)mas[1];
        mas = panel.getComponents();
        for(int i = 0; i < mas.length; i++){
            Button but = (Button)mas[i];
            if (but.getActionCommand().equals(command)){
                but.setState(ButtonState.ACTIVE);
            }
            else but.setState(ButtonState.INACTIVE);
            but.changeState();
        }
    }

    public JPanel createToolBar(){

        String[] icons = {"img/Перемещение.png",
                "img/Добавить.png",
                "img/Направленное ребро.png",
                "img/Ненаправленное ребро.png",
                "img/Удалить.png",
                "img/Алгоритм.png",
                "img/Очистить.png"
        };

        String[] commands = {"Перемещение",
                "Добавить вершины",
                "Добавить ориентированное ребро",
                "Добавить неориентированное ребро",
                "Удалить вершины и рёбра",
                "Алгоритм",
                "Очистить полотно"
        };

        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        toolBar.setBackground(new Color(219, 232, 254));

        for(int i =0; i < icons.length; i++ ){
            Button button = new Button();
            button.setIcon(new ImageIcon(icons[i]));
            button.setActionCommand(commands[i]);
            button.setPreferredSize(new Dimension(32, 32));
            button.addActionListener(this);

            button.setBackground(new Color(219, 232, 254));
            button.setFocusPainted(false);
            toolBar.add(button);
        }
        toolBar.setBorder(BorderFactory.createRaisedBevelBorder());
        return toolBar;
    }

    public void openHelp() {
        File htmlFile = new File("src\\com\\codebind\\Help.html");
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch(IOException exception) {
            System.out.println("IO");
        }
    }
}