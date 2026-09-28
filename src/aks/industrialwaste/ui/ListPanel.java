package aks.industrialwaste.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import jp.co.astax.awt.FontUtility;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 自動出荷分析システムのデータを一覧表示するパネルです.<br>
 * 自動出荷分析システムの各データを一覧表示する機能は, このクラスを継承して作成 すること.
 *
 * @author K.Kuroda
 * @version 2.0.0
 * @since 1.0.0
 */
public class ListPanel extends Panel {

    public static final int MAX_RECORD_COUNT = 3000;

    protected JLabel countField_;
    protected JTable dataTable_;

    public ListPanel() {
        super();
    }

    protected void showEditDialog(int mode) {
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        // データ件数
        countField_ = new JLabel("", JLabel.RIGHT);
        countField_.setBorder(BorderFactory.createLoweredBevelBorder());
        countField_.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 24));
        countField_.setBackground(Color.WHITE);
        countField_.setOpaque(true);
        northPane_.add(
                 countField_,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.1, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.BOTH,
                          new Insets(2, 2, 2, 2), 0, 0));
        // データテーブル
        dataTable_ = new JTable();
        centerPane_.add(
                 new JScrollPane(dataTable_),
                 new GridBagConstraints(
                          0, 0, 1, 1, 1.0, 1.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(5, 5, 5, 5), 0, 0));

        dataTable_.addMouseListener(
                 new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent me) {
                if (me.getClickCount() == 2 && dataTable_.getSelectedRow() >= 0) {
                    showEditDialog(0);
                }
            }
        });
    }
}
