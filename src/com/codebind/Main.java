package com.codebind;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

class Main implements ActionListener {
    private GraphicsPanel graphicsPanel;

    public enum GraphStates {
        CREATE_NODE,
        CONNECT_NODE,
        MOVE_NODE,
        NOTHING
    }

    public Main() {
        Image iconOfApp = new ImageIcon("Иконка приложения.png").getImage();

        JFrame frame = new JFrame("Graph application");
        frame.setIconImage(iconOfApp);

        frame.setMinimumSize(new Dimension(800,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

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

        ImageIcon iconPlus = new ImageIcon("Плюс.png");
        ImageIcon iconArrow = new ImageIcon("Стрелка.png");
        ImageIcon iconCross = new ImageIcon("Крестик.png");
        ImageIcon iconGear = new ImageIcon("Шестерёнка.png");
        ImageIcon iconBroom = new ImageIcon("Метла.png");

        JMenuItem itemAddVertices = new JMenuItem("Добавить вершины", iconPlus);
        JMenuItem itemConnectVertices = new JMenuItem("Соединить вершины", iconArrow);
        JMenuItem itemDelete = new JMenuItem("Удалить", iconCross);
        JMenu subMenuAlgorithm = new JMenu("Алгоритм");
        JMenuItem itemClearScene = new JMenuItem("Очистить полотно", iconBroom);
        JMenuItem itemNothing = new JMenuItem("Перемещение", iconArrow);

        subMenuAlgorithm.setIcon(iconGear);
        JMenuItem itemAlgorithm = new JMenuItem("<Конкретный алгоритм>");

        subMenuAlgorithm.add(itemAlgorithm);

        itemAddVertices.addActionListener(this);
        itemConnectVertices.addActionListener(this);
        itemNothing.addActionListener(this);

        menuAction.add(itemAddVertices);
        menuAction.add(itemConnectVertices);
        menuAction.add(itemDelete);
        menuAction.add(subMenuAlgorithm);
        menuAction.add(itemClearScene);
        menuAction.add(itemNothing);

        ImageIcon iconQuestion = new ImageIcon("Вопрос.png");

        JMenuItem itemAboutProgram = new JMenuItem("О программе", iconQuestion);

        menuHelp.add(itemAboutProgram);

        JPanel statusBar = new JPanel();

        statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusBar.setLayout(new BorderLayout());

        BoundedRangeModel model = new DefaultBoundedRangeModel(30, 0, 0, 200);

        JSlider slider = new JSlider(model);

        statusBar.add(slider, BorderLayout.EAST);

        graphicsPanel = new GraphicsPanel();

        frame.add(graphicsPanel);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String comStr = ae.getActionCommand();

        if(comStr.equals("Добавить вершины")) {
            graphicsPanel.setGraphState(GraphStates.CREATE_NODE);
        }
        else if (comStr.equals("Соединить вершины")) {
            graphicsPanel.setGraphState(GraphStates.CONNECT_NODE);
        }
        else if (comStr.equals("Перемещение")) {
            graphicsPanel.setGraphState(GraphStates.MOVE_NODE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities. invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        } ) ;
    }
}
