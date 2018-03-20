package com.mephone.fontello.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mephone.fontello.ShadowFont;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.util.TextUtils;

public class ShadowFrame extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static final int M_WIDTH = 400;

    private static float TEXT_SIZE = 50f;
    private static float PNG_SIZE = 60f;

    private JButton mDrawPngButton;
    private JButton mMakeShadowButton;
    private JButton mStopButton;
    private JTextField mPngSizeText;
    private JTextField mTextSizeText;
    private JTextArea mShadowTextArea;

    private ShadowFont mShadowFont = null;

    private KeyAdapter mKeyAdapter = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            int keyChar = e.getKeyChar();
            if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
            } else {
                e.consume();
            }
        }
    };

    public ShadowFrame() {
        mShadowFont = new ShadowFont();
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBounds(0, 0, M_WIDTH, 30);
        JPanel jpanelOne = new JPanel();
        jpanelOne.setLayout(new BoxLayout(jpanelOne, BoxLayout.X_AXIS));
        northPanel.add(Box.createVerticalStrut(5));
        northPanel.add(jpanelOne);
        northPanel.add(Box.createVerticalStrut(5));

        mPngSizeText = new JTextField();
        mPngSizeText.setText(PNG_SIZE + "");
        mPngSizeText.addKeyListener(mKeyAdapter);
        mTextSizeText = new JTextField();
        mTextSizeText.setText(TEXT_SIZE + "");
        mTextSizeText.addKeyListener(mKeyAdapter);

        jpanelOne.add(Box.createHorizontalStrut(20));
        jpanelOne.add(new JLabel("文字大小:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mTextSizeText);
        jpanelOne.add(Box.createHorizontalStrut(50));
        jpanelOne.add(new JLabel("图片大小:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mPngSizeText);
        jpanelOne.add(Box.createHorizontalStrut(20));

        add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        add(southPanel, BorderLayout.SOUTH);

        mDrawPngButton = new JButton("生成文字png");
        mDrawPngButton.addActionListener(this);
        southPanel.add(mDrawPngButton);

        mMakeShadowButton = new JButton("生成渐变png");
        mMakeShadowButton.addActionListener(this);
        southPanel.add(mMakeShadowButton);

        mStopButton = new JButton("中止");
        mStopButton.addActionListener(this);
        southPanel.add(mStopButton);

        mShadowTextArea = new JTextArea();
        mShadowTextArea.setLineWrap(true);
        mShadowTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mShadowTextArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void update() {
        MyLog.setTextArea(mShadowTextArea);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mDrawPngButton) {
            startDraw();
        } else if (e.getSource() == mMakeShadowButton) {
            startShadow();
        } else if (e.getSource() == mStopButton) {
            mShadowFont.stop();
        }
    }

    private void startShadow() {
        mDrawPngButton.setEnabled(false);
        mMakeShadowButton.setEnabled(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                mShadowFont.startMakeShadowFont();
                mDrawPngButton.setEnabled(true);
                mMakeShadowButton.setEnabled(true);
            }
        }).start();
    }

    private void startDraw() {
        String textSize = mTextSizeText.getText();
        String pngSize = mPngSizeText.getText();
        if (!TextUtils.isEmpty(textSize)) {
            mShadowFont.setTextSize(Float.parseFloat(textSize));
        }
        if (!TextUtils.isEmpty(pngSize)) {
            mShadowFont.setPngSize(Float.parseFloat(pngSize));
        }
        mDrawPngButton.setEnabled(false);
        mMakeShadowButton.setEnabled(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                mShadowFont.drawGB2312();
                mDrawPngButton.setEnabled(true);
                mMakeShadowButton.setEnabled(true);
            }
        }).start();
    }
}
