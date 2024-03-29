import IO.InputReader;
import IO.OutputWriter;
import Managers.AlgorithmEventManager;
import Managers.GraphEventManager;
import Widgets.ButtonState;
import Widgets.GraphicsPanel;
import Widgets.Button;
import Snapshots.GraphCaretaker;
import ViewCompomemts.DrawGraph;
import algorithmComponents.AlgorithmButtons;
import algorithmComponents.Algorithms;
import graphComponents.GraphStates;
import graphComponents.RandomGraphCreator;
import graphComponents.Triangulator;

import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import javax.swing.*;
import static java.awt.Cursor.getPredefinedCursor;


class Application implements ActionListener {
    public JFrame frame;
    private static GraphicsPanel graphicsPanel;
    private JMenuBar menuBar;
    private JPanel statusBar;
    JPanel toolBar;
    private HashMap<String, Button> buttonHashMap = new HashMap<String, Button>();
    private HashMap<String, Button> singleActiveButtonHashMap = new HashMap<>();

    public Application() {
        Image iconOfApp = new ImageIcon("resources/img/Иконка приложения.png").getImage();

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

        String[] actionIcons = {"resources/img/Добавить(small).png","resources/img/Направленное ребро(small).png","resources/img/Ненаправленное ребро(small).png",
                "resources/img/Удалить(small).png","resources/img/Очистить(small).png","resources/img/Перемещение(small).png","resources/img/Cоединить все(small).png", "resources/img/Создать случайный граф(small).png"};

        for(int i = 0; i < actioncommands.length; i++){
            JMenuItem action = new JMenuItem(actioncommands[i], new ImageIcon(actionIcons[i]));
            action.addActionListener(this);
            menuAction.add(action);
        }

        JMenu itemSubMenuAlgorithm = new JMenu("Алгоритм");
        itemSubMenuAlgorithm.setIcon(new ImageIcon("resources/img/Алгоритм(small).png"));

        JMenuItem itemKosaraju = new JMenuItem("Косарайю");
        JMenuItem itemDFS = new JMenuItem("Поиск в глубину");

        JMenuItem StepBack = new JMenuItem("Отмена",KeyEvent.VK_Z);
        StepBack.setIcon(new ImageIcon("resources/img/Отмена(small).png"));
        StepBack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        JMenuItem StepUp = new JMenuItem("Отмена отмены",KeyEvent.VK_Y);
        StepUp.setIcon(new ImageIcon("resources/img/Up(small).png"));
        StepUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        itemSubMenuAlgorithm.add(itemKosaraju);
        itemSubMenuAlgorithm.add(itemDFS);

        menuAction.add(StepBack);
        menuAction.add(StepUp);

        StepBack.addActionListener(this);
        StepUp.addActionListener(this);
        itemDFS.addActionListener(this);
        itemKosaraju.addActionListener(this);
        itemOpenFile.addActionListener(this);
        itemSaveGraph.addActionListener(this);

        menuAction.add(itemSubMenuAlgorithm);

        ImageIcon iconQuestion = new ImageIcon("resources/img/Вопрос.png");
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
    }


    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        JLabel labelAction = (JLabel)statusBar.getComponents()[0];

        if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM) {
            if (!command.equals("Запустить алгоритм") &&
                    !command.equals("Остановить алгоритм") &&
                    !command.equals("Сделать шаг вперед") &&
                    !command.equals("Сделать шаг назад") &&
                    !command.equals("Результат") &&
                    !command.equals("Начало")) {
                AlgorithmEventManager.getInstance().reset();
            }
        }

        if (!command.equals("Добавить ориентированное ребро") &&
                !command.equals("Добавить неориентированное ребро")) {
            GraphEventManager.getInstance().resetConnectData();
        }

        switch(command) {
            case "Открыть":
                labelAction.setText("Открыть");
                InputReader newOne = new InputReader();

                if(newOne.FileOpen) {
                    graphicsPanel.setGraph(new DrawGraph(newOne.initFromData()));
                    AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
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
                GraphCaretaker.push(GraphEventManager.getInstance().getGraph().save());
                GraphEventManager.getInstance().removeGraph();
                break;
            case "Удалить вершины и рёбра":
                labelAction.setText("Удаление вершин и рёбер");
                graphicsPanel.setGraphState(GraphStates.DELETE_NODE);
                break;
            case "Соединить все вершины":
                labelAction.setText("Соединение всех вершин");
                GraphCaretaker.push(GraphEventManager.getInstance().getGraph().save());
                GraphEventManager.getInstance().connectAllVertices();
                break;
            case "Создать случайный граф":
                GraphCaretaker.push(GraphEventManager.getInstance().getGraph().save());
                graphicsPanel.setGraph(RandomGraphCreator.create(
                        1 + new Random(System.currentTimeMillis()).nextInt(16),
                        0.25D, graphicsPanel.getWidth(),
                        graphicsPanel.getHeight(),
                        true));
                AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "Триангулировать":
                GraphCaretaker.push(GraphEventManager.getInstance().getGraph().save());
                Triangulator.triangulate(GraphEventManager.getInstance().getGraph());
                break;
            case "Алгоритм":
                GraphEventManager.getInstance().setState(GraphStates.ALGORITHM);
                AlgorithmEventManager.getInstance().reset();
                AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "Запустить алгоритм":
            case "Остановить алгоритм":
            case "Результат":
                if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM &&
                        AlgorithmEventManager.getInstance().isInitialized()) {
                    AlgorithmEventManager.getInstance().sendCommand(command);
                }
                break;
            case "Сделать шаг вперед":
            case "Сделать шаг назад":
            case "Начало":
                if (GraphEventManager.getInstance().getState() == GraphStates.ALGORITHM &&
                        AlgorithmEventManager.getInstance().isInitialized()) {
                    AlgorithmEventManager.getInstance().sendCommand("Остановить алгоритм");
                    AlgorithmEventManager.getInstance().sendCommand(command);
                }
                break;
            case "Поиск в глубину":
                labelAction.setText("DFS");
                Algorithms.selectAlgorithmByName("DFS");
                AlgorithmEventManager.getInstance().reset();
                AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "Косарайю":
                labelAction.setText("Kosaraju");
                Algorithms.selectAlgorithmByName("Kosaraju");
                AlgorithmEventManager.getInstance().reset();
                AlgorithmEventManager.getInstance().getAlgorithm().setGraph(GraphEventManager.getInstance().getGraph());
                break;
            case "О программе":
                labelAction.setText("О программе");
                openHelp();
                break;
            case "Отмена":
                if (!AlgorithmEventManager.getInstance().isInitialized()) {
                    GraphEventManager.getInstance().getGraph().restore(GraphCaretaker.pop());
                }
                break;
            case "Отмена отмены":
                if (!AlgorithmEventManager.getInstance().isInitialized()) {
                    GraphEventManager.getInstance().getGraph().restore(GraphCaretaker.poll());
                }
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
        if (command.equals("Алгоритм")) {
            buttonHashMap.get("Запустить алгоритм").setEnabled(true);
            buttonHashMap.get("Остановить алгоритм").setEnabled(true);
            buttonHashMap.get("Сделать шаг вперед").setEnabled(true);
            buttonHashMap.get("Сделать шаг назад").setEnabled(false);
            buttonHashMap.get("Отмена").setEnabled(false);
            buttonHashMap.get("Отмена отмены").setEnabled(false);
            buttonHashMap.get("Результат").setEnabled(true);
            buttonHashMap.get("Начало").setEnabled(false);
        }
        else if (GraphEventManager.getInstance().getState() != GraphStates.ALGORITHM) {
            buttonHashMap.get("Запустить алгоритм").setEnabled(false);
            buttonHashMap.get("Остановить алгоритм").setEnabled(false);
            buttonHashMap.get("Сделать шаг вперед").setEnabled(false);
            buttonHashMap.get("Сделать шаг назад").setEnabled(false);
            buttonHashMap.get("Результат").setEnabled(false);
            buttonHashMap.get("Начало").setEnabled(false);

            if (!GraphCaretaker.isEmpty()) {
                buttonHashMap.get("Отмена").setEnabled(true);
            }

            if (!GraphCaretaker.isFull()) {
                buttonHashMap.get("Отмена отмены").setEnabled(true);
            }
        }

        if (singleActiveButtonHashMap.containsKey(command)) {
            for (Button button : singleActiveButtonHashMap.values()) {
                button.setState(ButtonState.INACTIVE);
                button.changeState();
            }

            singleActiveButtonHashMap.get(command).setState(ButtonState.ACTIVE);
            singleActiveButtonHashMap.get(command).changeState();
        }

        if (command.equals("Запустить алгоритм") && !AlgorithmEventManager.getInstance().isInitialized()) {
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
        String[] icons = {"resources/img/Перемещение.png",
                "resources/img/Добавить.png",
                "resources/img/Cоединить все.png",
                "resources/img/Направленное ребро.png",
                "resources/img/Ненаправленное ребро.png",
                "resources/img/Удалить.png",
                "resources/img/Очистить.png",
                "resources/img/Создать случайный граф.png",
                "resources/img/Триангуляция.png",
                "resources/img/Отмена.png",
                "resources/img/Up.png",
                "resources/img/Алгоритм.png",
                "resources/img/Запустить алгоритм.png",
                "resources/img/Остановить алгоритм.png",
                "resources/img/Сделать шаг вперед.png",
                "resources/img/Сделать шаг назад.png",
                "resources/img/Конец.png",
                "resources/img/Начало.png"
        };

        String[] commands = {
                "Перемещение",
                "Добавить вершины",
                "Соединить все вершины",
                "Добавить ориентированное ребро",
                "Добавить неориентированное ребро",
                "Удалить вершины и рёбра",
                "Очистить полотно",
                "Создать случайный граф",
                "Триангулировать",
                "Отмена",
                "Отмена отмены",
                "Алгоритм",
                "Запустить алгоритм",
                "Остановить алгоритм",
                "Сделать шаг вперед",
                "Сделать шаг назад",
                "Результат",
                "Начало"
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

            if(commands[i].equals("Запустить алгоритм")){
                for(int j = 0; j < 8; j++) {
                    toolBar.add(new JSeparator(SwingConstants.VERTICAL));
                }
            }

            button.setToolTipText(commands[i]);
            button.setCursor(getPredefinedCursor(Cursor.HAND_CURSOR));
            button.setBorderPainted(false);

            if (commands[i].equals("Запустить алгоритм") ||
                    commands[i].equals("Остановить алгоритм") ||
                    commands[i].equals("Сделать шаг вперед") ||
                    commands[i].equals("Сделать шаг назад") ||
                    commands[i].equals("Отмена") ||
                    commands[i].equals("Отмена отмены") ||
                    commands[i].equals("Результат") ||
                    commands[i].equals("Начало")) {
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

        GraphCaretaker.button1 = buttonHashMap.get("Отмена");
        GraphCaretaker.button2 = buttonHashMap.get("Отмена отмены");

        toolBar.setBorder(BorderFactory.createRaisedBevelBorder());

        return toolBar;
    }

    public void openHelp() {
        File htmlFile = new File("resources/html/Help.html");
        try {
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch(IOException exception) {
            System.out.println("IO");
        }
    }
}