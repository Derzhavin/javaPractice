package com.codebind.Managers;

import com.codebind.ButtonState;
import com.codebind.GraphicsPanel;
import com.codebind.algorithmComponents.Algorithm;
import com.codebind.algorithmComponents.AlgorithmButtons;

import javax.swing.*;

public class AlgorithmEventManager {
    private static final int BASIC_DO_STEP_TIMER_DELAY = 200;
    private static final AlgorithmEventManager instance = new AlgorithmEventManager();

    private Algorithm currentAlgorithm;
    private AlgorithmButtons algorithmButtons;

    private GraphicsPanel graphicsPanel;
    private JLabel display;
    private Timer doStepTimer = new Timer(BASIC_DO_STEP_TIMER_DELAY, e->sendCommand("Сделать шаг вперед"));

    public static AlgorithmEventManager getInstance() {
        return instance;
    }

    public boolean isInitialized() {
        return currentAlgorithm.isInitialized();
    }

    public void sendCommand(String command) {
        Algorithm.AlgorithmState algorithmState = null;

        switch (command) {
            case "Запустить алгоритм":
                doStepTimer.start();
                break;
            case "Остановить алгоритм":
                doStepTimer.stop();
                break;
            case "Сделать шаг вперед":
                algorithmState = doForwardStep();
                break;
            case "Сделать шаг назад":
                algorithmState = doBackwardStep();
                break;
            case "Результат":
                algorithmState = jumpToFinal();
                break;
            case "Начало":
                algorithmState = jumpToStart();
                break;
        }

        if (algorithmState != null) {
            repaint(algorithmState.stepsColorDataBase);
            updateButtons(algorithmState.initialized, algorithmState.step);
            sendMessage(algorithmState.stepsColorDataBase.stepName);
        }
    }

    private void repaint(Algorithm.StepsColorDataBase currentStepColorData) {
        currentStepColorData.resetColors();
        graphicsPanel.repaint();
    }

    private void updateButtons(boolean initialized, int step) {
        if (step == 2) {
            algorithmButtons.buttons.get(0).setState(ButtonState.INACTIVE);
            algorithmButtons.buttons.get(0).changeState();
            algorithmButtons.buttons.get(0).setEnabled(false);
            algorithmButtons.buttons.get(2).setEnabled(false);
            algorithmButtons.buttons.get(4).setEnabled(false);
        }
        else {
            algorithmButtons.buttons.get(0).setEnabled(true);
            algorithmButtons.buttons.get(2).setEnabled(true);
            algorithmButtons.buttons.get(4).setEnabled(true);
        }

        if (step == 0) {
            algorithmButtons.buttons.get(3).setEnabled(false);
            algorithmButtons.buttons.get(5).setEnabled(false);
        }
        else {
            algorithmButtons.buttons.get(3).setEnabled(true);
            algorithmButtons.buttons.get(5).setEnabled(true);
        }

        if (!initialized) {
            algorithmButtons.buttons.get(3).setEnabled(false);
            algorithmButtons.buttons.get(5).setEnabled(false);
        }
    }

    private void sendMessage(String message) {
        display.setText(message);
    }

    private Algorithm.AlgorithmState doForwardStep() {
        currentAlgorithm.doForwardStep();
        return currentAlgorithm.getState();
    }

    private Algorithm.AlgorithmState doBackwardStep() {
        currentAlgorithm.doBackwardStep();
        return currentAlgorithm.getState();
    }

    private Algorithm.AlgorithmState jumpToFinal() {
        Algorithm.AlgorithmState algorithmState;

        do {
            currentAlgorithm.doForwardStep();
            algorithmState = currentAlgorithm.getState();
        } while (algorithmState.step != 2);

        return algorithmState;
    }

    private Algorithm.AlgorithmState jumpToStart() {
        Algorithm.AlgorithmState algorithmState;

        do {
            currentAlgorithm.doBackwardStep();
            algorithmState = currentAlgorithm.getState();
        } while (algorithmState.step != 0);

        return algorithmState;
    }

    public void reset() {
        doStepTimer.stop();
        graphicsPanel.setGraphBasicColor();
        updateButtons(false, 1);
        currentAlgorithm.reset();
    }

    public void setAlgorithm(Algorithm algorithm) {
        currentAlgorithm = algorithm;
        currentAlgorithm.setGraph(GraphEventManager.getInstance().getGraph());
    }

    public void setGraphicsPanel(GraphicsPanel graphicsPanel) {
        this.graphicsPanel = graphicsPanel;
    }

    public void setDisplay(JLabel display) {
        this.display = display;
    }

    public void setAlgorithmButtons(AlgorithmButtons algorithmButtons) {
        this.algorithmButtons = algorithmButtons;
    }

    public void setDelay(int delay) {
        doStepTimer.setDelay(delay);
    }

    public Algorithm getAlgorithm() {
        return currentAlgorithm;
    }
}
