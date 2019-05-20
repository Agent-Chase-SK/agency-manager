/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv168.agencymanagergui;

import cz.muni.fi.pv168.agencymanager.common.Main;
import cz.muni.fi.pv168.agencymanager.common.ValidationException;
import cz.muni.fi.pv168.agencymanager.entity.Agent;
import cz.muni.fi.pv168.agencymanager.entity.Mission;
import cz.muni.fi.pv168.agencymanager.manager.AgentManager;
import cz.muni.fi.pv168.agencymanager.manager.AgentManagerImpl;
import cz.muni.fi.pv168.agencymanager.manager.MissionManager;
import cz.muni.fi.pv168.agencymanager.manager.MissionManagerImpl;
import cz.muni.fi.pv168.agencymanager.status.AgentStatus;
import cz.muni.fi.pv168.agencymanager.status.MissionStatus;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jakub
 */
public class AgencyFrame extends javax.swing.JFrame {
    private final MissionManager missionManager;
    private final AgentManager agentManager;
    
    private final AgentTableModel agentTableModel = new AgentTableModel();
    private final MissionTableModel missionTableModel = new MissionTableModel();
    
    private boolean agentSelectedBool = false;
    private boolean missionSelectedBool1 = false;
    private boolean missionSelectedBool2 = false;
    private final ResourceBundle bundle = java.util.ResourceBundle.getBundle("AgencyManager");
    
    private int selectedAgent1;
    private int selectedAgent2;
    private int selectedMission;
    
    private final static Logger LOG = LoggerFactory.getLogger(AgencyFrame.class);
    
    final ToString toStringLoc = (final Object object) -> bundle.getString(object.toString());

    /**
     * Creates new form AgencyFrame
     */
    public AgencyFrame() {
        try {
            this.missionManager = new MissionManagerImpl(Main.getDataSource(), Clock.systemDefaultZone());
            this.agentManager = new AgentManagerImpl(Main.getDataSource());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        initComponents();
        changeAgentStatusAction.setEnabled(false);
        addMissionAction.setEnabled(false);
        changeMissionStatusAction.setEnabled(false);
        jTable1.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!agentSelectedBool) {
                changeAgentStatusAction.setEnabled(true);
                addMissionAction.setEnabled(true);
                agentSelectedBool = true;
            }
        });
        jTabbedPane1.addChangeListener((ChangeEvent e) -> {
            if (jTabbedPane1.getSelectedIndex() == 0) {
                changeMissionStatusAction.setEnabled(missionSelectedBool1);
                addMissionAction.setEnabled(agentSelectedBool);
            } else {
                changeMissionStatusAction.setEnabled(missionSelectedBool2);
                addMissionAction.setEnabled(false);
            }
        });
        
        jTable2.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!missionSelectedBool1) {
                changeMissionStatusAction.setEnabled(true);
                missionSelectedBool1 = true;
            }
        });
        
        jTable3.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            if (!missionSelectedBool2) {
                changeMissionStatusAction.setEnabled(true);
                missionSelectedBool2 = true;
            }
        });
        
        jDialogAgentStatus.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                changeAgentStatusAction.setEnabled(true);
                jComboBox1.setSelectedItem(AgentStatus.ACTIVE);
            }
        });
        jDialogAddAgent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                addAgentAction.setEnabled(true);
                jComboBox2.setSelectedItem(AgentStatus.ACTIVE);
                jTextField1.setText("");
                jLabelWarnT1.setText("");
            }
        });
        jDialogAddMission.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                addMissionAction.setEnabled(true);
                jComboBox3.setSelectedItem(MissionStatus.SCHEDULED);
                jComboBox4.setSelectedItem("1");
                jComboBox5.setSelectedItem(Month.JANUARY);
                jTextField2.setText("");
                jTextField3.setText("");
                jTextField4.setText("");
                jLabelWarnT2.setText("");
                jLabelWarnT3.setText("");
                jLabelWarnT4.setText("");
            }
        });
        jDialogMissionStatus.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                changeMissionStatusAction.setEnabled(true);
                jComboBox6.setSelectedItem(AgentStatus.ACTIVE);
            }
        });
        jComboBox1.setRenderer(new ToStringListCellRenderer(jComboBox1.getRenderer(), toStringLoc));
        jComboBox2.setRenderer(new ToStringListCellRenderer(jComboBox2.getRenderer(), toStringLoc));
        jComboBox3.setRenderer(new ToStringListCellRenderer(jComboBox3.getRenderer(), toStringLoc));
        jComboBox5.setRenderer(new ToStringListCellRenderer(jComboBox5.getRenderer(), toStringLoc));
        jComboBox6.setRenderer(new ToStringListCellRenderer(jComboBox6.getRenderer(), toStringLoc));
        
        jDialogAddAgent.pack();
        jDialogAddMission.pack();
        jDialogAgentStatus.pack();
        jDialogMissionStatus.pack();
        
        LOG.info("Initialized components");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogAgentStatus = new javax.swing.JDialog();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jDialogAddAgent = new javax.swing.JDialog();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton8 = new javax.swing.JButton();
        jLabelWarnT1 = new javax.swing.JLabel();
        jDialogAddMission = new javax.swing.JDialog();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jLabelWarnT3 = new javax.swing.JLabel();
        jLabelWarnT2 = new javax.swing.JLabel();
        jLabelWarnT4 = new javax.swing.JLabel();
        jDialogMissionStatus = new javax.swing.JDialog();
        jLabel10 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jButton11 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jDialogAgentStatus.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox1.setModel(new DefaultComboBoxModel(AgentStatus.values()));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("AgencyManager"); // NOI18N
        jButton7.setText(bundle.getString("changeStatus")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("changeAgentStatusPrompt")); // NOI18N

        javax.swing.GroupLayout jDialogAgentStatusLayout = new javax.swing.GroupLayout(jDialogAgentStatus.getContentPane());
        jDialogAgentStatus.getContentPane().setLayout(jDialogAgentStatusLayout);
        jDialogAgentStatusLayout.setHorizontalGroup(
            jDialogAgentStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAgentStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogAgentStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jDialogAgentStatusLayout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogAgentStatusLayout.setVerticalGroup(
            jDialogAgentStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAgentStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialogAgentStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7))
                .addContainerGap())
        );

        jDialogAddAgent.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText(bundle.getString("addAgentPrompt")); // NOI18N

        jLabel3.setText(bundle.getString("codeName")); // NOI18N

        jLabel4.setText(bundle.getString("status")); // NOI18N

        jComboBox2.setModel(new DefaultComboBoxModel(AgentStatus.values()));

        jButton8.setText(bundle.getString("add")); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAddAgentLayout = new javax.swing.GroupLayout(jDialogAddAgent.getContentPane());
        jDialogAddAgent.getContentPane().setLayout(jDialogAddAgentLayout);
        jDialogAddAgentLayout.setHorizontalGroup(
            jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddAgentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jDialogAddAgentLayout.createSequentialGroup()
                        .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogAddAgentLayout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8))
                            .addGroup(jDialogAddAgentLayout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelWarnT1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jDialogAddAgentLayout.setVerticalGroup(
            jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddAgentLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelWarnT1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jDialogAddAgentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText(bundle.getString("addMissionPrompt")); // NOI18N

        jLabel6.setText(bundle.getString("codeName")); // NOI18N

        jLabel7.setText(bundle.getString("status")); // NOI18N

        jComboBox3.setModel(new DefaultComboBoxModel(MissionStatus.values()));

        jLabel8.setText(bundle.getString("date")); // NOI18N

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jComboBox5.setModel(new DefaultComboBoxModel(Month.values()));

        jLabel9.setText(bundle.getString("location")); // NOI18N

        jButton9.setText(bundle.getString("add")); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAddMissionLayout = new javax.swing.GroupLayout(jDialogAddMission.getContentPane());
        jDialogAddMission.getContentPane().setLayout(jDialogAddMissionLayout);
        jDialogAddMissionLayout.setHorizontalGroup(
            jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                        .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelWarnT4, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9))
                            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelWarnT3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelWarnT2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jDialogAddMissionLayout.setVerticalGroup(
            jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAddMissionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelWarnT2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addComponent(jLabelWarnT3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jButton9)
                        .addGroup(jDialogAddMissionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelWarnT4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel10.setText(bundle.getString("changeMissionStatusPrompt")); // NOI18N

        jComboBox6.setModel(new DefaultComboBoxModel(MissionStatus.values()));

        jButton11.setText(bundle.getString("changeStatus")); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogMissionStatusLayout = new javax.swing.GroupLayout(jDialogMissionStatus.getContentPane());
        jDialogMissionStatus.getContentPane().setLayout(jDialogMissionStatusLayout);
        jDialogMissionStatusLayout.setHorizontalGroup(
            jDialogMissionStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogMissionStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogMissionStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogMissionStatusLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(jDialogMissionStatusLayout.createSequentialGroup()
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11)))
                .addContainerGap())
        );
        jDialogMissionStatusLayout.setVerticalGroup(
            jDialogMissionStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogMissionStatusLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogMissionStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("agencyManager")); // NOI18N

        jButton1.setAction(addAgentAction);
        jButton1.setText(bundle.getString("addAgent")); // NOI18N

        jButton2.setAction(changeAgentStatusAction);
        jButton2.setText(bundle.getString("changeAgentStatus")); // NOI18N

        jButton3.setAction(addMissionAction);
        jButton3.setText(bundle.getString("addMission")); // NOI18N

        jButton4.setAction(changeMissionStatusAction);
        jButton4.setText(bundle.getString("changeMissionStatus")); // NOI18N

        jTable1.setModel(agentTableModel);
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(missionTableModel);
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(0, 121, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)))
        );

        jTabbedPane1.addTab(bundle.getString("agents"), jPanel1); // NOI18N

        jTable3.setModel(missionTableModel);
        jScrollPane3.setViewportView(jTable3);

        jButton6.setAction(changeMissionStatusAction);
        jButton6.setText(bundle.getString("changeStatus")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 648, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("missions"), jPanel2); // NOI18N

        jMenu1.setText(bundle.getString("agent")); // NOI18N

        jMenuItem1.setAction(addAgentAction);
        jMenuItem1.setText(bundle.getString("add")); // NOI18N
        jMenu1.add(jMenuItem1);

        jMenuItem3.setAction(changeAgentStatusAction);
        jMenuItem3.setText(bundle.getString("changeStatus")); // NOI18N
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText(bundle.getString("mission")); // NOI18N

        jMenuItem2.setAction(addMissionAction);
        jMenuItem2.setText(bundle.getString("add")); // NOI18N
        jMenu2.add(jMenuItem2);

        jMenuItem4.setAction(changeMissionStatusAction);
        jMenuItem4.setText(bundle.getString("changeStatus")); // NOI18N
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(bundle.getString("agents")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        AgentStatus status = (AgentStatus) jComboBox1.getSelectedItem();
        
        changeAgentStatusWorker = new ChangeAgentStatusWorker(selectedAgent1, status);
        changeAgentStatusWorker.addPropertyChangeListener(progressListener);
        changeAgentStatusWorker.execute();
        
        jDialogAgentStatus.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        jLabelWarnT1.setText("");
        String codeName = jTextField1.getText();
        if (codeName.isEmpty()) {
            jLabelWarnT1.setText(bundle.getString("warnEmptyField"));
            return;
        }
        AgentStatus status = (AgentStatus) jComboBox2.getSelectedItem();
        addAgentWorker = new AddAgentWorker(codeName, status);
        addAgentWorker.addPropertyChangeListener(progressListener);
        addAgentWorker.execute();

        jDialogAddAgent.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jLabelWarnT2.setText("");
        jLabelWarnT3.setText("");
        jLabelWarnT4.setText("");
        String codeName = jTextField2.getText();
        if (codeName.isEmpty()) {
            jLabelWarnT2.setText(bundle.getString("warnEmptyField"));
            return;
        }
        MissionStatus status = (MissionStatus) jComboBox3.getSelectedItem();
        int day = Integer.parseInt((String)jComboBox4.getSelectedItem());
        Month month = (Month) jComboBox5.getSelectedItem();
        int year;
        try {
            year = Integer.parseInt(jTextField3.getText());
        } catch (NumberFormatException e) {
            jLabelWarnT3.setText(bundle.getString("warnYearNotInt"));
            return;
        }
        if (year <= 0) {
            jLabelWarnT3.setText(bundle.getString("warnYearNotInt"));
            return;
        }
        LocalDate date;
        try {
            date = LocalDate.of(year,month,day);
        } catch (DateTimeException e) {
            if (jLabelWarnT3.getText().isEmpty()) {
                jLabelWarnT3.setText(bundle.getString("warnInvalidDate"));
            }
            return;
        }
        String location = jTextField4.getText();
        if (location.isEmpty()) {
            jLabelWarnT4.setText(bundle.getString("warnEmptyField"));
            return;
        }
        if (!testDateStatus(date, status)) {
            if (jLabelWarnT3.getText().isEmpty()) {
                jLabelWarnT3.setText(bundle.getString("warnStatDate"));
            }
            return;
        }
        
        addMissionWorker = new AddMissionWorker(codeName, status, date, location, selectedAgent2);
        addMissionWorker.addPropertyChangeListener(progressListener);
        addMissionWorker.execute();
        
        jDialogAddMission.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        MissionStatus status = (MissionStatus) jComboBox6.getSelectedItem();
        
        changeMissionStatusWorker = new ChangeMissionStatusWorker(selectedMission, status);
        changeMissionStatusWorker.addPropertyChangeListener(progressListener);
        changeMissionStatusWorker.execute();
        
        jDialogMissionStatus.dispose();
    }//GEN-LAST:event_jButton11ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.error("Error in AgencyFrame", ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AgencyFrame().setVisible(true);
        });
    }
    
    private class AddAgentAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            addAgentAction.setEnabled(false);
            jDialogAddAgent.setVisible(true);
        }
    }
    
    private class ChangeAgentStatusAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            changeAgentStatusAction.setEnabled(false);
            jDialogAgentStatus.setVisible(true);
            selectedAgent1 = jTable1.getSelectedRow();
        }
    }
    
    private class AddMissionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            addMissionAction.setEnabled(false);
            jDialogAddMission.setVisible(true);
            selectedAgent2 = jTable1.getSelectedRow();
        }
    }
    
    private class ChangeMissionStatusAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            changeMissionStatusAction.setEnabled(false);
            jDialogMissionStatus.setVisible(true);
            if (jTabbedPane1.getSelectedIndex() == 0) {
                selectedMission = jTable2.getSelectedRow();
            } else {
                selectedMission = jTable3.getSelectedRow();
            }
        }
    }
    
    private final AddAgentAction addAgentAction = new AddAgentAction();
    private final ChangeAgentStatusAction changeAgentStatusAction = new ChangeAgentStatusAction();
    private final AddMissionAction addMissionAction = new AddMissionAction();
    private final ChangeMissionStatusAction changeMissionStatusAction = new ChangeMissionStatusAction();
    
    private class ChangeAgentStatusWorker extends SwingWorker<Void, Void> {
        private final int index;
        private final AgentStatus status;

        public ChangeAgentStatusWorker(int index, AgentStatus status) {
            this.index = index;
            this.status = status;
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            Agent agent = agentManager.findAgentById(new Long(index+1));
            setProgress(50);
            agent.setStatus(status);
            agentManager.updateAgent(agent);
            setProgress(100);
            return null;
        }

        @Override
        protected void done() {
            changeAgentStatusAction.setEnabled(true);
            changeAgentStatusWorker = null;
            agentTableModel.updatedCell(index, 1);
        }
        
    }
    
    private class AddAgentWorker extends SwingWorker<Void, Void> {
        private final Agent agent;

        public AddAgentWorker(String codeName, AgentStatus status) {
            agent = new Agent();
            agent.setCodeName(codeName);
            agent.setStatus(status);
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            agentManager.createAgent(agent);
            setProgress(100);
            return null;
        }

        @Override
        protected void done() {
            addAgentAction.setEnabled(true);
            addAgentWorker = null;
            agentTableModel.insertedRow(Math.toIntExact(agent.getId())-1);
        }
        
    }
    
    private class AddMissionWorker extends SwingWorker<Void, Void> {
        private final Mission mission;

        public AddMissionWorker(String codeName, MissionStatus status, LocalDate date, String location, int agent) {
            mission = new Mission();
            mission.setCodeName(codeName);
            mission.setStatus(status);
            mission.setDate(date);
            mission.setLocation(location);
            mission.setAgentId(new Long(agent+1));
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            missionManager.createMission(mission);
            setProgress(100);
            return null;
        }

        @Override
        protected void done() {
            addMissionAction.setEnabled(true);
            addMissionWorker = null;
            missionTableModel.insertedRow(Math.toIntExact(mission.getId())-1);
        }
        
    }
    
    private class ChangeMissionStatusWorker extends SwingWorker<Void, Void> {
        private final int index;
        private final MissionStatus status;
        private boolean succOp = true;

        public ChangeMissionStatusWorker(int index, MissionStatus status) {
            this.index = index;
            this.status = status;
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(0);
            Mission mission = missionManager.findMissionById(new Long(index+1));
            setProgress(50);
            mission.setStatus(status);
            try {
                missionManager.updateMission(mission);
            } catch (ValidationException e) {
                succOp = false;
            }
            setProgress(100);
            return null;
        }

        @Override
        protected void done() {
            changeMissionStatusAction.setEnabled(true);
            changeMissionStatusWorker = null;
            if (succOp) {
                missionTableModel.updatedCell(index, 1);
            } else {
                JOptionPane.showMessageDialog(null, bundle.getString("warnStatDate"));
            }
        }
        
    }
    
    private ChangeAgentStatusWorker changeAgentStatusWorker;
    private AddAgentWorker addAgentWorker;
    private AddMissionWorker addMissionWorker;
    private ChangeMissionStatusWorker changeMissionStatusWorker;
    
    private PropertyChangeListener progressListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("progress")) {
                jProgressBar1.setValue((Integer) evt.getNewValue());
            }
        }
    };
    
    private static boolean testDateStatus(LocalDate date, MissionStatus status) {
        LocalDate today = LocalDate.now(Clock.systemDefaultZone());
        if (status == MissionStatus.SCHEDULED && date.isBefore(today)) {
            return false;
        }
        return !(status != MissionStatus.SCHEDULED && date.isAfter(today));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JDialog jDialogAddAgent;
    private javax.swing.JDialog jDialogAddMission;
    private javax.swing.JDialog jDialogAgentStatus;
    private javax.swing.JDialog jDialogMissionStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelWarnT1;
    private javax.swing.JLabel jLabelWarnT2;
    private javax.swing.JLabel jLabelWarnT3;
    private javax.swing.JLabel jLabelWarnT4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
