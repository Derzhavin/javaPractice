package com.codebind;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

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

        statusBar = new JPanel();

        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setLayout(new BorderLayout());

        BoundedRangeModel model = new DefaultBoundedRangeModel(30, 0, 0, 200);

        JSlider slider = new JSlider(model);

        statusBar.add(slider, BorderLayout.EAST);

        graphicsPanel = new GraphicsPanel();

        instrumentPanel = createInstrumentPanel();

        frame.add(graphicsPanel);
        frame.add(instrumentPanel, BorderLayout.NORTH);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        if (command.equals("Добавить вершины")) {
            graphicsPanel.setGraphState(GraphState.CREATE_NODE);
        } else if (command.equals("Соединить вершины")) {
            graphicsPanel.setGraphState(GraphState.CONNECT_NODE);
        } else if (command.equals("Перемещение")) {
            graphicsPanel.setGraphState(GraphState.MOVE_NODE);
        } else if (command.equals("Очистить полотно")) {
            graphicsPanel.getGraph().removeGraph();
        } else if (command.equals("Удалить")) {
            graphicsPanel.setGraphState(GraphState.DELETE_NODE);
        } else if (command.equals("Соеденить всё")) {
            graphicsPanel.getGraph().connectAllVertices();
        }else if(command.equals("Ничего не делать")){
            graphicsPanel.setGraphState(GraphState.NOTHING);

            Component[] instrumentPanelComponents = instrumentPanel.getComponents();

            for(Component instrumentPanelComponent: instrumentPanelComponents){
                Button button = (Button) instrumentPanelComponent;
                if(button.getState() == ButtonState.ACTIVE){
                    button.setState(ButtonState.INACTIVE);
                    button.changeState();
                }
            }

        }
        graphicsPanel.updateUI();
    }

    public JPanel createInstrumentPanel(){

        String[] icons = {"img/mouse.png","img/add.png","img/path.png","img/delete.png","img/alg.png","img/clean.png"};
        String[] commands = {"Ничего не делать","Добавить вершины","Соединить вершины",
                "Удалить","Алгоритм","Очистить полотно"};

        JPanel instrumentPanel = new JPanel(new GridLayout(1,6,0,0));

        for(int i =0; i < icons.length; i++ ){
            Button button = new Button();
            button.setIcon(new ImageIcon(icons[i]));
            button.setActionCommand(commands[i]);
            //button.setBackground(new Color(205, 210, 255));
            button.setPreferredSize(new Dimension(32, 32));
            button.addActionListener(this);
            instrumentPanel.add(button);
        }

        return instrumentPanel;
    }
}