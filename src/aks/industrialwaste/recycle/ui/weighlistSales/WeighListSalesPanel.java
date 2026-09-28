package aks.industrialwaste.recycle.ui.weighlistSales;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.recycle.report.XlsxSalesReport;
import aks.industrialwaste.recycle.ui.Infobar;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class WeighListSalesPanel extends javax.swing.JPanel {

    private WeighListSalesTableModel model_;
    private WeighListSalesDetailTableModel dmodel_;
    private Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);    //メイリオ
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private Connection con_;

    public WeighListSalesPanel(Connection con) {
        initComponents();
        con_ = con;
        //売上高
        model_ = new WeighListSalesTableModel(con);
        jTable1.setModel(model_);
        jTable1.setColumnModel(new WeighListSalesTableColumnModel());
        jTable1.setFont(font14);
        jTable1.setRowHeight(30);
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowSelectionAllowed(true);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader jh = jTable1.getTableHeader();
        jh.setFont(font14);

        dmodel_ = new WeighListSalesDetailTableModel(con);
        jTable2.setModel(dmodel_);
        jTable2.setColumnModel(new WeighListSalesDetailTableColumnModel());
        jTable2.setFont(font14);
        jTable2.setRowHeight(30);
        jTable2.setColumnSelectionAllowed(false);
        jTable2.setRowSelectionAllowed(true);
        jTable2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jh = jTable2.getTableHeader();
        jh.setFont(font14);

        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(250, 30));
        startDateField_.setPreferredSize(new Dimension(250, 30));
        startPanel.add(startDateField_);

        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(250, 30));
        endDateField_.setPreferredSize(new Dimension(250, 30));
        endPanel.add(endDateField_);

        startDateField_.setValue(Calendar.getInstance());
        endDateField_.setValue(Calendar.getInstance());

        txt消費税率.setText(String.valueOf(Infobar.i消費税率));

        jButton4.setVisible(false);

        jButton2.setVisible(false);
        jButton5.setVisible(false);
        jButton6.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        endPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btn再表示 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txt消費税率 = new aks.swing.JTextFieldKeiryo();
        jLabel5 = new javax.swing.JLabel();
        cmb締め日 = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1920, 900));
        setMinimumSize(new java.awt.Dimension(1280, 900));
        setLayout(new java.awt.GridBagLayout());

        jLabel1.setBackground(new java.awt.Color(0, 0, 255));
        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("売上実績一覧");
        jLabel1.setMaximumSize(new java.awt.Dimension(90, 40));
        jLabel1.setMinimumSize(new java.awt.Dimension(90, 40));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(90, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(jLabel1, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMaximumSize(new java.awt.Dimension(100, 60));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 60));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 60));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton2.setText("得意先別売上高出力");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.setMaximumSize(new java.awt.Dimension(180, 30));
        jButton2.setMinimumSize(new java.awt.Dimension(180, 30));
        jButton2.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jButton2, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel7.setText("帳票出力(社外向け)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel1.add(jLabel7, gridBagConstraints);

        jPanel5.setMaximumSize(new java.awt.Dimension(100, 10));
        jPanel5.setMinimumSize(new java.awt.Dimension(100, 10));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 684, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel5, gridBagConstraints);

        jButton4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton4.setText("入金明細表出力");
        jButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton4.setMaximumSize(new java.awt.Dimension(160, 30));
        jButton4.setMinimumSize(new java.awt.Dimension(160, 30));
        jButton4.setPreferredSize(new java.awt.Dimension(160, 30));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        jPanel1.add(jButton4, gridBagConstraints);

        jButton5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton5.setText("排出業者別売上高出力");
        jButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton5.setMaximumSize(new java.awt.Dimension(180, 30));
        jButton5.setMinimumSize(new java.awt.Dimension(180, 30));
        jButton5.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jButton5, gridBagConstraints);

        jButton6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton6.setText("運搬業者別売上高出力");
        jButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton6.setMaximumSize(new java.awt.Dimension(180, 30));
        jButton6.setMinimumSize(new java.awt.Dimension(180, 30));
        jButton6.setPreferredSize(new java.awt.Dimension(180, 30));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jButton6, gridBagConstraints);

        jButton8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton8.setText("売上明細書出力");
        jButton8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton8.setMaximumSize(new java.awt.Dimension(160, 30));
        jButton8.setMinimumSize(new java.awt.Dimension(160, 30));
        jButton8.setPreferredSize(new java.awt.Dimension(160, 30));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 2);
        jPanel1.add(jButton8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 20, 20);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(650, 402));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(650, 402));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(650, 402));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTable1MousePressed(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTable1KeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jScrollPane1, gridBagConstraints);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        add(jPanel2, gridBagConstraints);

        jPanel3.setMaximumSize(new java.awt.Dimension(1920, 50));
        jPanel3.setMinimumSize(new java.awt.Dimension(1280, 50));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        startPanel.setMaximumSize(new java.awt.Dimension(280, 30));
        startPanel.setMinimumSize(new java.awt.Dimension(280, 30));
        startPanel.setPreferredSize(new java.awt.Dimension(280, 30));
        startPanel.setLayout(new javax.swing.BoxLayout(startPanel, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(startPanel, gridBagConstraints);

        endPanel.setMaximumSize(new java.awt.Dimension(280, 30));
        endPanel.setMinimumSize(new java.awt.Dimension(280, 30));
        endPanel.setPreferredSize(new java.awt.Dimension(280, 30));
        endPanel.setLayout(new javax.swing.BoxLayout(endPanel, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel3.add(endPanel, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("-");
        jLabel2.setMaximumSize(new java.awt.Dimension(20, 28));
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 28));
        jLabel2.setPreferredSize(new java.awt.Dimension(20, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel3.add(jLabel2, gridBagConstraints);

        btn再表示.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn再表示.setText("再表示");
        btn再表示.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn再表示.setMaximumSize(new java.awt.Dimension(130, 30));
        btn再表示.setMinimumSize(new java.awt.Dimension(130, 30));
        btn再表示.setPreferredSize(new java.awt.Dimension(130, 30));
        btn再表示.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn再表示ActionPerformed(evt);
            }
        });
        btn再表示.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn再表示KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(btn再表示, gridBagConstraints);

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 35));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 35));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 468, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jPanel4, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel3.setText("消費税率");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 0, 2);
        jPanel3.add(jLabel3, gridBagConstraints);

        txt消費税率.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt消費税率.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt消費税率.setText("8");
        txt消費税率.setMaximumSize(new java.awt.Dimension(40, 30));
        txt消費税率.setMinimumSize(new java.awt.Dimension(40, 30));
        txt消費税率.setPreferredSize(new java.awt.Dimension(40, 30));
        txt消費税率.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt消費税率KeyPressed(evt);
            }
        });
        jPanel3.add(txt消費税率, new java.awt.GridBagConstraints());

        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setText("得意先締め日");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel3.add(jLabel5, gridBagConstraints);

        cmb締め日.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmb締め日.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "5", "10", "15", "20", "25" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel3.add(cmb締め日, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setText("0:月末　20:20日締");
        jLabel6.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel3.add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jPanel3, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btn再表示ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn再表示ActionPerformed

        // TODO add your handling code here:
        if (txt消費税率.getText().length() > 0) {
            try {
                updateView();
            } catch (NumberFormatException ne) {
                //jPanel2
                JOptionPane.showMessageDialog(
                         jPanel2,
                         "税率は半角数字で入力してください",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn再表示ActionPerformed

    private void jTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MousePressed

        // TODO add your handling code here:
        updateView2();
    }//GEN-LAST:event_jTable1MousePressed

    private void jTable1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_UP) {
            updateView2();
        } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            updateView2();
        }

    }//GEN-LAST:event_jTable1KeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ReportAction(4);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txt消費税率KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt消費税率KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn再表示.requestFocus();
        }
    }//GEN-LAST:event_txt消費税率KeyPressed

    private void btn再表示KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn再表示KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (txt消費税率.getText().length() > 0) {
                try {
                    updateView();
                } catch (NumberFormatException ne) {
                    //jPanel2
                    JOptionPane.showMessageDialog(
                             jPanel2,
                             "税率は半角数字で入力してください",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_btn再表示KeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        //入金明細出力
        ReportAction(3);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        //排出業者別売上高実績
        ReportAction(5);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        //運搬業者別売上高実績
        ReportAction(6);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        //得意先別売上明細表
        ReportAction(2);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void updateView() {
        // 検索条件, ソート条件を設定
        SearchCondition[] search = null;
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("bill_id", "bill_id", ColumnType.INT4),
                              SortOrder.ASC)};

        model_.refresh(DBManager.convert(startDateField_.getValueAtDate()),
                 DBManager.convert(endDateField_.getValueAtDate()),
                 Integer.valueOf(txt消費税率.getText()),
                 Integer.valueOf(cmb締め日.getSelectedItem().toString()),
                 search,
                 sort);
    }

    private void updateView2() {
        int row = jTable1.getSelectedRow();
        int id = (Integer) model_.getValueAt(row, "bill_id");
        int option2 = (Integer) model_.getValueAt(row, "option2");

        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("dis_ent_id", "dis_ent_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("dis_office_id", "dis_office_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("term_date", "term_date", ColumnType.DATE),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("gate_out_date", "gate_out_date", ColumnType.TIMESTAMP),
                              SortOrder.ASC)};

        dmodel_.refresh(DBManager.convert(startDateField_.getValueAtDate()),
                 DBManager.convert(endDateField_.getValueAtDate()),
                 id,
                 option2,
                 sort);
    }

    private void ReportAction(int report_id) {

        try {
            Calendar c = startDateField_.getValue();
            Calendar c1 = endDateField_.getValue();
            SimpleDateFormat sdf_dd = new SimpleDateFormat("yyyyMMdd_");
            JFileChooser fc = new JFileChooser();

            fc.setSelectedFile(new File(DBManager.pass_,
                     (sdf_dd.format(DBManager.convert(c)).equals(sdf_dd.format(DBManager.convert(c1)))
                     ? sdf_dd.format(DBManager.convert(c))
                     : sdf_dd.format(DBManager.convert(c)) + sdf_dd.format(DBManager.convert(c1)))
                     + (report_id == 1 || report_id == 4 || report_id == 5 || report_id == 6 ? "売上高実績一覧表"
                              : report_id == 2 ? "売上高明細表"
                                       : report_id == 3 ? "入金明細表"
                                                : "") + ".xlsx"));

            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));

            if (XlsxSalesReport.SALES_REPORT1 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT1).print(con_, XlsxSalesReport.SALES_REPORT1, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );
            } else if (XlsxSalesReport.SALES_REPORT2 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT2).print(con_, XlsxSalesReport.SALES_REPORT2, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );

            } else if (XlsxSalesReport.SALES_REPORT3 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT3).print(con_, XlsxSalesReport.SALES_REPORT3, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );

            } else if (XlsxSalesReport.SALES_REPORT4 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT4).print(con_, XlsxSalesReport.SALES_REPORT4, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );

            } else if (XlsxSalesReport.SALES_REPORT5 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT5).print(con_, XlsxSalesReport.SALES_REPORT5, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );

            } else if (XlsxSalesReport.SALES_REPORT6 == report_id) {
                new XlsxSalesReport(XlsxSalesReport.SALES_REPORT6).print(con_, XlsxSalesReport.SALES_REPORT6, f.getPath(),
                         startDateField_.getValue(),
                         endDateField_.getValue(),
                         Integer.valueOf(txt消費税率.getText()),
                         Integer.valueOf(cmb締め日.getSelectedItem().toString())
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     this,
                     "EXCELファイルへの出力に失敗しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn再表示;
    private javax.swing.JComboBox<String> cmb締め日;
    private javax.swing.JPanel endPanel;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel startPanel;
    private javax.swing.JTextField txt消費税率;
    // End of variables declaration//GEN-END:variables
}
