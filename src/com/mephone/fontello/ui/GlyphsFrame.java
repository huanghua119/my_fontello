package com.mephone.fontello.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.mephone.fontello.GlyphsPath;
import com.mephone.fontello.util.StringTools;
import com.mephone.fontello.util.TextUtils;

public class GlyphsFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static final int M_WIDTH = 400;
    public static final int M_HEIGHT = 400;

    private JTextField mXPieceText;
    private JTextField mYPieceText;

    private JTextArea mPathDataText;
    private JButton mCalculateButton;
    private JCheckBox mReverseCheck;

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GlyphsFrame gf = new GlyphsFrame();
                gf.setVisible(true);
            }
        });
    }

    private KeyAdapter mKeyAdapter = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            int keyChar = e.getKeyChar();
            if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)
                    || keyChar == 46) {
            } else {
                e.consume();
            }
        }
    };

    public GlyphsFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension dim = this.getToolkit().getScreenSize();
        this.setBounds((int) (dim.getWidth() - M_WIDTH) / 2,
                (int) (dim.getHeight() - M_WIDTH) / 2, M_WIDTH, M_HEIGHT);
        this.setTitle("字体工具");
        // this.setResizable(false);
        // 禁用窗体的装饰
        // this.setUndecorated(true);
        // this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        getContentPane().setLayout(new BorderLayout());

        JPanel northLayout = new JPanel();
        northLayout.setLayout(new BoxLayout(northLayout, BoxLayout.X_AXIS));

        mXPieceText = new JTextField();
        mXPieceText.addKeyListener(mKeyAdapter);
        mYPieceText = new JTextField();
        mYPieceText.addKeyListener(mKeyAdapter);

        northLayout.add(Box.createHorizontalStrut(20));
        northLayout.add(new JLabel("pieceX:  "));
        northLayout.add(mXPieceText);

        northLayout.add(Box.createHorizontalStrut(20));
        northLayout.add(new JLabel("pieceY:  "));
        northLayout.add(mYPieceText);
        northLayout.add(Box.createHorizontalStrut(20));
        getContentPane().add(northLayout, BorderLayout.NORTH);

        mPathDataText = new JTextArea();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mPathDataText);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel bottomLayout = new JPanel();
        bottomLayout.setLayout(new BorderLayout());

        JPanel bTopLayout = new JPanel();
        bTopLayout.setLayout(new FlowLayout());
        bottomLayout.add(bTopLayout, BorderLayout.SOUTH);

        mReverseCheck = new JCheckBox();
        mCalculateButton = new JButton("计算");
        mCalculateButton.addActionListener(this);

        // bTopLayout.add(new JLabel("反向计算:  "));
        // bTopLayout.add(mReverseCheck);
        bTopLayout.add(mCalculateButton);

        getContentPane().add(bottomLayout, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mCalculateButton) {
            String xPiece = mXPieceText.getText();
            String yPiece = mYPieceText.getText();
            if (TextUtils.isEmpty(xPiece) && TextUtils.isEmpty(yPiece)) {
                showAlert(this, "pieceX和pieceY至少要有一个不为空!");
                return;
            }
            String data = mPathDataText.getText();
            if (TextUtils.isEmpty(data)) {
                showAlert(this, "数据不能为空!");
                return;
            }
            float pieceX = 0;
            float pieceY = 0;
            if (!TextUtils.isEmpty(xPiece)) {
                pieceX = StringTools.parseFloat(xPiece, 0);
            } else {
                pieceX = StringTools.parseFloat(yPiece, 0);
            }
            if (!TextUtils.isEmpty(yPiece)) {
                pieceY = StringTools.parseFloat(yPiece, 0);
            } else {
                pieceY = StringTools.parseFloat(xPiece, 0);
            }
            GlyphsPath gp = new GlyphsPath();
            gp.startCalculate(this, data, pieceX, pieceY);
        }
    }

    private void showAlert(Component frame, String data) {
        if (frame != null) {
            JOptionPane.showMessageDialog(frame, data, "提示！",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            System.out.println(data);
        }
    }
}
