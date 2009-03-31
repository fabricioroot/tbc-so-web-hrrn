package thread;

import java.awt.Dialog.ModalExclusionType;
import java.awt.Dialog.ModalityType;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import bean.Process;
import gui.MainScreen;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import manager.Calculator;
import manager.HRRNAlgorithm;

/**
 *
 * @author Fabrício Reis
 */
public class AlgorithmStepsThread implements Runnable {
    
    MainScreen mainScreen;
    JButton jButtonAlgorithmSteps;
    JButton jButtonReport; 
    Vector<Process> processesList;
    Vector<Process> reportBase;
    int timeCounter;
    JPanel jPanelCPU;
    JPanel jPanelReadyProcesses;
    JProgressBar jProgressBarExecution;
    JLabel jLabelShowBurstTime;
    JLabel jLabelShowCreationTime;
    JLabel jLabelTimeCounter;
    JLabel jLabelCPU;
    boolean isJButtonOkClicked = false;
    JDialog jDialogNextStep;
    JButton jButtonOkNextStep;
    JLabel jLabelAtDialogNextStep;
    Calculator calculator = new Calculator();
    JTextField block, block1, block2, block3, block4;

    public AlgorithmStepsThread(MainScreen mainScreen, JButton jButtonAlgorithmSteps, JButton jButtonReport, Vector<Process> processesList, Vector<Process> reportBase,
                                int timeCounter, JPanel jPanelCPU, JPanel jPanelReadyProcesses, JProgressBar jProgressBarExecution, JLabel jLabelShowBurstTime,
                                JLabel jLabelShowCreationTime, JLabel jLabelTimeCounter, JLabel jLabelCPU){
        
        this.mainScreen = mainScreen;
        this.jButtonAlgorithmSteps = jButtonAlgorithmSteps;
        this.jButtonReport = jButtonReport;
        this.processesList = processesList;
        this.reportBase = reportBase;
        this.timeCounter = timeCounter;
        this.jPanelCPU = jPanelCPU;
        this.jPanelReadyProcesses = jPanelReadyProcesses;
        this.jProgressBarExecution = jProgressBarExecution;
        this.jLabelShowBurstTime = jLabelShowBurstTime;
        this.jLabelShowCreationTime = jLabelShowCreationTime;
        this.jLabelTimeCounter = jLabelTimeCounter;
        this.jLabelCPU = jLabelCPU;
    }

    public JTextField getBlock() {
        return block;
    }

    public JTextField getBlock1() {
        return block1;
    }

    public JTextField getBlock2() {
        return block2;
    }
    
    public JTextField getBlock3() {
        return block3;
    }

    public JProgressBar getJProgressBarExecution() {
        return jProgressBarExecution;
    }

    public Vector<Process> getProcessesList() {
        return processesList;
    }

    public Vector<Process> getReportBase() {
        return reportBase;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public JDialog getJDialogNextStep() {
        return jDialogNextStep;
    }

    public void setJDialogNextStep(JDialog jDialogNextStep) {
        this.jDialogNextStep = jDialogNextStep;
    }

    public void run() {
        this.jButtonAlgorithmSteps.setEnabled(false);
        
        if (!this.processesList.isEmpty()) {
            
            HRRNAlgorithm algorithm = new HRRNAlgorithm();
            this.processesList = algorithm.calculatePriorities(this.processesList);
            this.mainScreen.paintProcessesList(this.processesList);

            if(this.reportBase == null) {
                this.reportBase = new Vector<Process>();
            }
            this.reportBase.add(this.calculator.waitingTimeAndTurnAround(this.processesList, algorithm.toExecute(this.processesList), this.timeCounter));

            Vector<Integer> positionsPossibleProcesses = new Vector<Integer>();

            this.jDialogNextStep = new JDialog();
            this.jDialogNextStep.setModalityType(ModalityType.MODELESS);
            this.jDialogNextStep.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
            //this.jDialogNextStep.setAlwaysOnTop(true);
            this.jDialogNextStep.setResizable(false);
            this.jDialogNextStep.setBounds(600, 520, 231, 118);
            this.jDialogNextStep.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            this.jDialogNextStep.setLayout(null);
            this.jDialogNextStep.setTitle("BUSCANDO PROCESSO IDEAL ...");

            this.jButtonOkNextStep = new JButton("OK");
            this.jButtonOkNextStep.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            this.jButtonOkNextStep.setBorderPainted(true);
            this.jButtonOkNextStep.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            this.jButtonOkNextStep.setBounds(80, 35, 60, 30);

            this.jButtonOkNextStep.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    isJButtonOkClicked = true;
                }
            });

            this.jLabelAtDialogNextStep = new JLabel("Clique em 'OK' para o próximo passo");
            this.jLabelAtDialogNextStep.setBounds(5, 3, 500, 30);

            this.jDialogNextStep.add(this.jLabelAtDialogNextStep);
            this.jDialogNextStep.add(this.jButtonOkNextStep);
            
            this.jLabelShowBurstTime.setVisible(true);
            this.jLabelShowCreationTime.setVisible(true);
            
            block = new JTextField();
            block.setText("j");
            block.setBackground(new java.awt.Color(255, 255, 102));
            block.setForeground(new java.awt.Color(0, 0, 0));
            block.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block.setEditable(false);
            this.jPanelReadyProcesses.add(block);

            // Used to format the float values
            DecimalFormat df = new DecimalFormat("#0.00");

            // Here is painted the first yellow block on the first possible "process" to be executed
            int orientationAxisY = 25;
            this.jDialogNextStep.setVisible(true);
            block.setBounds(15, orientationAxisY, 30, 30);
            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(0).getWaitingTime() + " / " + (int)this.processesList.elementAt(0).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(0).getPriority()));
            do {
                if (this.isJButtonOkClicked) {
                    this.jDialogNextStep.setVisible(false);
                }
            } while (!this.isJButtonOkClicked);
            this.isJButtonOkClicked = false;
            this.jDialogNextStep.setVisible(false);

            // Here is changed the value of 'block' to paint the first green block on the first possible "process" to be executed
            this.jLabelShowCreationTime.setText("Prioridade em \"i\" (1 + " + (int)this.processesList.elementAt(0).getWaitingTime() + " / " + (int)this.processesList.elementAt(0).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(0).getPriority()));
            block.setText("i");
            block.setBackground(new java.awt.Color(0, 255, 0));
            block.setToolTipText("Possível escalonado");
            this.jDialogNextStep.setVisible(true);
            do {
                if (this.isJButtonOkClicked) {
                    this.jDialogNextStep.setVisible(false);
                }
            } while (!this.isJButtonOkClicked);
            this.isJButtonOkClicked = false;

            positionsPossibleProcesses = algorithm.findPositionsPossibleProcesses(this.processesList);

            int j = 0;
            if (positionsPossibleProcesses.size() > 1) {
                
                // Here happens the steps between blocks yellow and green till it reaches the last possible process (white block)
                for(j = 1; j <= (positionsPossibleProcesses.size() - 1); j++) {
                    block1 = new JTextField();
                    block1.setText("j");
                    block1.setBackground(new java.awt.Color(255, 255, 102));
                    block1.setForeground(new java.awt.Color(0, 0, 0));
                    block1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                    block1.setEditable(false);
                    this.jPanelReadyProcesses.add(block1);

                    if(positionsPossibleProcesses.elementAt(j) <= 8) {
                        // First row
                        this.jDialogNextStep.setVisible(true);
                        int i = positionsPossibleProcesses.elementAt(j-1);
                        block1.setBounds(15+(i*35), orientationAxisY, 30, 30);
                        this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i).getWaitingTime() + " / " + (int)this.processesList.elementAt(i).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i).getPriority()));
                        i++;
                        while (i <= positionsPossibleProcesses.elementAt(j)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                block1.setBounds(15+(i*35), orientationAxisY, 30, 30);
                                this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i).getWaitingTime() + " / " + (int)this.processesList.elementAt(i).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i).getPriority()));
                                i++;
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                        i--;
                        block1.setBounds(15+(i*35), orientationAxisY, 30, 30);
                        this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i).getWaitingTime() + " / " + (int)this.processesList.elementAt(i).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i).getPriority()));
                    }
                    else {
                        if (positionsPossibleProcesses.elementAt(j-1) <= 8) {
                            // First row
                            this.jDialogNextStep.setVisible(true);
                            int i = positionsPossibleProcesses.elementAt(j-1);
                            block1.setBounds(15+(i*35), orientationAxisY, 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i).getWaitingTime() + " / " + (int)this.processesList.elementAt(i).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i).getPriority()));
                            while (i <= 8) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    i++;
                                    block1.setBounds(15+(i*35), orientationAxisY, 30, 30);
                                    this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i).getWaitingTime() + " / " + (int)this.processesList.elementAt(i).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i).getPriority()));
                                }
                            }

                            // Second row
                            i = 0;
                            block1.setBounds(15, (orientationAxisY + 70), 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(9).getWaitingTime() + " / " + (int)this.processesList.elementAt(9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(9).getPriority()));
                            i++;
                            while (i <= (positionsPossibleProcesses.elementAt(j) - 9)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    block1.setBounds(15+(i*35), (orientationAxisY + 70), 30, 30);
                                    this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(i+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i+9).getPriority()));
                                    i++;
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            i--;
                            block1.setBounds(15+(i*35), (orientationAxisY + 70), 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(i+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i+9).getPriority()));
                        }
                        else {
                            // Second row
                            this.jDialogNextStep.setVisible(true);
                            int i = (positionsPossibleProcesses.elementAt(j-1) - 9);
                            block1.setBounds(15+(i*35), (orientationAxisY + 70), 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(i+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i+9).getPriority()));
                            i++;
                            while (i <= (positionsPossibleProcesses.elementAt(j) - 9)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    block1.setBounds(15+(i*35), (orientationAxisY + 70), 30, 30);
                                    this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(i+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i+9).getPriority()));
                                    i++;
                                }
                            }
                            this.jDialogNextStep.setVisible(false);
                            i--;
                            block1.setBounds(15+(i*35), (orientationAxisY + 70), 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(i+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(i+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(i+9).getPriority()));
                        }
                    }

                    this.jDialogNextStep.setVisible(true);
                    do {
                        if (this.isJButtonOkClicked) {
                            this.jDialogNextStep.setVisible(false);
                        }
                    } while (!this.isJButtonOkClicked);
                    this.isJButtonOkClicked = false;

                    this.mainScreen.paintProcessesList(this.processesList);
                    this.jPanelReadyProcesses.add(block1);

                    block1.setText("i");
                    block1.setBackground(new java.awt.Color(0, 255, 0));
                    this.jLabelShowCreationTime.setText("Prioridade em \"i\" (1 + " + (int)this.processesList.elementAt(positionsPossibleProcesses.elementAt(j)).getWaitingTime() + " / " + (int)this.processesList.elementAt(positionsPossibleProcesses.elementAt(j)).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(positionsPossibleProcesses.elementAt(j)).getPriority()));

                    block2 = new JTextField();
                    block2 = block1;
                    block1 = null;
                    block = null;

                    this.jDialogNextStep.setVisible(true);
                    do {
                        if (this.isJButtonOkClicked) {
                            this.jDialogNextStep.setVisible(false);
                        }
                    } while (!this.isJButtonOkClicked);
                    this.isJButtonOkClicked = false;

                    //Aqui talvez seja preciso atualizar o valor de processesList
                    
                    
                    // It refreshes 'positionsPossibleProcesses', in case some new shorter process goes in
                    positionsPossibleProcesses = algorithm.findPositionsPossibleProcesses(this.processesList);
                }
            }

            // Here is tested if there are blocks to jump from the last green one till the end of the white blocks
            if (positionsPossibleProcesses.lastElement() < (this.processesList.size() - 1)) {
                block3 = new JTextField();
                block3.setText("j");
                block3.setBackground(new java.awt.Color(255, 255, 102));
                block3.setForeground(new java.awt.Color(0, 0, 0));
                block3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                block3.setEditable(false);
                this.jPanelReadyProcesses.add(block3);

                if((this.processesList.size() - 1) <= 8) {
                    // First row
                    this.jDialogNextStep.setVisible(true);
                    j = positionsPossibleProcesses.lastElement();
                    block3.setBounds(15+(j*35), orientationAxisY, 30, 30);
                    this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j).getWaitingTime() + " / " + (int)this.processesList.elementAt(j).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j).getPriority()));
                    j++;
                    while (j <= (this.processesList.size() - 1)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            block3.setBounds(15+(j*35), orientationAxisY, 30, 30);
                            this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j).getWaitingTime() + " / " + (int)this.processesList.elementAt(j).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j).getPriority()));
                            j++;
                        }
                    }
                    this.jDialogNextStep.setVisible(false);
                }
                else {
                    if (positionsPossibleProcesses.lastElement() <= 8) {
                        // First row
                        this.jDialogNextStep.setVisible(true);
                        j = positionsPossibleProcesses.lastElement();
                        block3.setBounds(15+(j*35), orientationAxisY, 30, 30);
                        this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j).getWaitingTime() + " / " + (int)this.processesList.elementAt(j).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j).getPriority()));
                        while (j <= 8) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block3.setBounds(15+(j*35), orientationAxisY, 30, 30);
                                this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j).getWaitingTime() + " / " + (int)this.processesList.elementAt(j).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j).getPriority()));
                            }
                        }

                        // Second row
                        j = 0;
                        block3.setBounds(15, (orientationAxisY + 70), 30, 30);
                        this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(9).getWaitingTime() + " / " + (int)this.processesList.elementAt(9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(9).getPriority()));
                        j++;
                        while (j <= (this.processesList.size() - 10)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                block3.setBounds(15+(j*35), (orientationAxisY + 70), 30, 30);
                                this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(j+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j+9).getPriority()));
                                j++;
                            }
                        }
                        this.jDialogNextStep.setVisible(false);

                    }
                    else {
                        // Second row
                        this.jDialogNextStep.setVisible(true);
                        j = (positionsPossibleProcesses.lastElement() - 9);
                        block3.setBounds(15+(j*35), (orientationAxisY + 70), 30, 30);
                        this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(j+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j+9).getPriority()));
                        j++;
                        while (j <= (this.processesList.size() - 10)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                block3.setBounds(15+(j*35), (orientationAxisY + 70), 30, 30);
                                this.jLabelShowBurstTime.setText("Prioridade em \"j\" (1 + " + (int)this.processesList.elementAt(j+9).getWaitingTime() + " / " + (int)this.processesList.elementAt(j+9).getLifeTime() +  ") = " + df.format(this.processesList.elementAt(j+9).getPriority()));
                                j++;
                            }
                        }
                        this.jDialogNextStep.setVisible(false);
                    }
                }
            }

            this.jDialogNextStep.setVisible(true);
            do {
                if (this.isJButtonOkClicked) {
                    this.jDialogNextStep.setVisible(false);
                }
            } while (!this.isJButtonOkClicked);
            this.isJButtonOkClicked = false;

            Process process = new Process();
            process = this.processesList.elementAt(algorithm.toExecute(this.processesList));
            this.processesList.remove(process);
            this.mainScreen.paintProcessesList(this.processesList);

            block4 = new JTextField();
            block4.setBackground(new java.awt.Color(255, 51, 0));
            block4.setForeground(new java.awt.Color(0, 0, 0));
            block4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block4.setEditable(false);
            block4.setBounds(35, 20, 30, 30);
            
            this.jPanelCPU.add(block4);
            block4.setText("P" + String.valueOf(process.getId()));
            block4.setToolTipText("Tempo de burst = " + String.valueOf(process.getLifeTime()));
            this.jProgressBarExecution.setVisible(true);
            this.jLabelShowBurstTime.setText("Tempo de burst de P" + String.valueOf(process.getId()) + " = " + String.valueOf((int)process.getLifeTime()));
            this.jLabelShowCreationTime.setText("Tempo na criação de P" + String.valueOf(process.getId()) + " = " + String.valueOf((int)process.getCreationTime()));
            
            this.jDialogNextStep.setTitle("PROCESSANDO P" + String.valueOf(process.getId()) + " ...");
            this.jDialogNextStep.setVisible(true);
            j = 0;
            int aux = 0;
            while (j <= (process.getLifeTime() - 1)) {
                if (this.isJButtonOkClicked) {
                    this.isJButtonOkClicked = false;
                    j++;
                    aux = 100 / (int)process.getLifeTime();
                    this.timeCounter++;
                    this.jLabelTimeCounter.setText(String.valueOf(this.timeCounter));
                    this.jProgressBarExecution.setValue(j*aux);
                    this.jProgressBarExecution.getUI().update(this.jProgressBarExecution.getGraphics(), this.jProgressBarExecution);
                }
            }
            
            // This bit is used to show to the user the last interaction (when 'jProgressBarExecution' is 100%) without increase 'timeCounter'
            while (j == process.getLifeTime()) {
                if (this.isJButtonOkClicked) {
                    this.isJButtonOkClicked = false;
                    aux = 100 / (int)process.getLifeTime();
                    j++;
                    this.jProgressBarExecution.setValue(j*aux);
                    this.jProgressBarExecution.getUI().update(this.jProgressBarExecution.getGraphics(), this.jProgressBarExecution);
                }
            }
            this.jDialogNextStep.setVisible(false);

            this.jPanelCPU.removeAll();
            this.jPanelCPU.repaint();
            this.jPanelCPU.add(this.jLabelCPU);
            this.jButtonReport.setEnabled(true);
            this.jProgressBarExecution.setVisible(false);
            this.jLabelShowBurstTime.setText("");
            this.jLabelShowCreationTime.setText("");
            
            if(this.processesList.size() > 0) {
                this.jButtonAlgorithmSteps.setEnabled(true);
                // It refreshes the wainting times of the processes
                this.processesList = this.calculator.waitingTimes(this.processesList, this.timeCounter);
                this.mainScreen.paintProcessesList(this.processesList);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Não há processos na lista de processos prontos!", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
        }
    }
}
