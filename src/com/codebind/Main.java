package com.codebind;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

class Main implements ActionListener {
    GraphicsPanel graphicsPanel;

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

        subMenuAlgorithm.setIcon(iconGear);
        JMenuItem itemAlgorithm = new JMenuItem("<Конкретный алгоритм>");

        subMenuAlgorithm.add(itemAlgorithm);

        itemAddVertices.addActionListener(this);
        menuAction.add(itemAddVertices);
        menuAction.add(itemConnectVertices);
        menuAction.add(itemDelete);
        menuAction.add(subMenuAlgorithm);
        menuAction.add(itemClearScene);

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
        addInst();
        frame.add(graphicsPanel);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae){
        String comStr = ae.getActionCommand();
        MyButton tbut = (MyButton)ae.getSource();
        tbut.pressed = true;
        if(comStr.equals("Добавить вершины")){
            graphicsPanel.AddVertex = !graphicsPanel.AddVertex;
            tbut.newstate();
        }
        if(comStr.equals("Ничего не делать")){
            graphicsPanel.AddVertex = false;
            Component[] all = graphicsPanel.getComponents();
            JPanel p = (JPanel) all[0];
            Component[] buts = p.getComponents();
            for(Component cbut: buts){
                MyButton but = (MyButton)cbut;
                if(but.pressed == true){
                    but.pressed = false;
                    but.newstate();

                }
            }

        }
        graphicsPanel.updateUI();
    }
    public static void main(String[] args) {
        SwingUtilities. invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        } ) ;
    }

    public void addInst(){

        String[] icons = {"mouse.png","add.png","path.png","delete.png","alg.png","clean.png"};
        String[] com = {"Ничего не делать","Добавить вершины","Соединить вершины",
                "Удалить","Алгоритм","Очистить полотно"};
        //Container inst = new Container();
        JPanel inst = new JPanel(new GridLayout(3,2,0,0));
        //inst.setSize(64, (icons.length/2)*32);
        MyButton button;
        //inst.setBackground(new Color(165, 165, 197));
        for(int i =0; i < icons.length; i++ ){
            button = new MyButton();
            button.setIcon        (new ImageIcon(icons[i]));
            //button.setRolloverIcon(new ImageIcon("mouse.png" ));
            //button.setPressedIcon ();
            //button.setDisabledIcon(new ImageIcon("mouse.png"));
            // Убираем все ненужные рамки и закраску
            //button.setBorderPainted(false);
            //button.setFocusPainted(false);
            //button.setContentAreaFilled(false);

            button.setActionCommand(com[i]);
            button.setBackground(new Color(205, 210, 255));

            //button.addActionListener();
            button.setPreferredSize(new Dimension(32,32));
            button.addActionListener(this);
            inst.add(button);
        }
        //inst.setMinimumSize();

        graphicsPanel.add(inst);
        graphicsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    }
}

class MyButton extends JButton{
    public boolean pressed = false;
    void newstate() {
        if (pressed) {
            this.setBackground(new Color(165, 165, 197));
        } else {
            this.setBackground(new Color(205, 210, 255));
        }
    }
}
