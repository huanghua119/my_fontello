package com.mephone.fontello.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mephone.fontello.FontelloService;
import com.mephone.fontello.bean.SvgConfig;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.CommonUtils;
import com.mephone.fontello.util.TextUtils;

public class FontelloFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = -1186812329395127121L;

    private static final int M_WIDTH = 400;
    private static final int M_HEIGHT = 400;

    private JFileChooser mFileChooser = null;

    private JPanel mFontelloLayout;
    private JPanel mSouthPanel;
    private JPanel mNorthPanel;
    private JButton mConfigButton;
    private JButton mFontelloButton;
    private JButton mSelectButton;
    private JTextArea mFontelloTextArea;

    private JTextField mUnitesText;
    private JTextField mAscentText;
    private JTextField mFontNameText;

    private JPanel mPng2SvgLayout;
    private JButton mCutPngButton;
    private JButton mPng2SvgButton;
    private JTextField mCutRowsText;
    private JTextField mCutColsText;
    private JTextField mCutWidthText;
    private JTextField mCutHeightText;
    private JTextField mCutSideWidthText;
    private JTextField mCutSideHeightText;
    private JTextArea mPng2SvgTextArea;

    private JMenuItem mPng2SvgItem;
    private JMenuItem mFontelloItem;

    private FontelloService mService = FontelloService.getInstance();
    private SvgConfig mConfig = null;

    private KeyAdapter mKeyAdapter = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            int keyChar = e.getKeyChar();
            if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {
            } else {
                e.consume();
            }
        }
    };

    public FontelloFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension dim = this.getToolkit().getScreenSize();
        this.setBounds((int) (dim.getWidth() - M_WIDTH) / 2,
                (int) (dim.getHeight() - M_WIDTH) / 2, M_WIDTH, M_HEIGHT);
        this.setTitle("义启字库生成器 ");
        this.setResizable(false);
        // 禁用窗体的装饰
        // this.setUndecorated(true);
        // this.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        getContentPane().setLayout(new BorderLayout());
        createFontelloLayout();
        createMenuBar();
        createPngSvgLayout();

        MyLog.setTextArea(mFontelloTextArea);
        getContentPane().add(mFontelloLayout, BorderLayout.CENTER);

        loadData();
        showNoActiveDialog();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("功能");
        mPng2SvgItem = new JMenuItem("png转svg");
        mPng2SvgItem.addActionListener(this);
        mFontelloItem = new JMenuItem("fontello工具");
        mFontelloItem.addActionListener(this);
        menu.add(mFontelloItem);
        menu.add(mPng2SvgItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void createPngSvgLayout() {
        mPng2SvgLayout = new JPanel();
        mPng2SvgLayout.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.setBounds(0, 0, M_WIDTH, 30);
        JPanel jpanelOne = new JPanel();
        jpanelOne.setLayout(new BoxLayout(jpanelOne, BoxLayout.X_AXIS));
        northPanel.add(Box.createVerticalStrut(5));
        northPanel.add(jpanelOne);
        northPanel.add(Box.createVerticalStrut(5));

        mCutColsText = new JTextField();
        mCutRowsText = new JTextField();
        mCutWidthText = new JTextField();
        mCutHeightText = new JTextField();
        mCutSideWidthText = new JTextField();
        mCutSideHeightText = new JTextField();
        mCutColsText.addKeyListener(mKeyAdapter);
        mCutRowsText.addKeyListener(mKeyAdapter);
        mCutWidthText.addKeyListener(mKeyAdapter);
        mCutHeightText.addKeyListener(mKeyAdapter);
        mCutSideWidthText.addKeyListener(mKeyAdapter);
        mCutSideHeightText.addKeyListener(mKeyAdapter);

        jpanelOne.add(Box.createHorizontalStrut(20));
        jpanelOne.add(new JLabel("行:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mCutRowsText);
        jpanelOne.add(Box.createHorizontalStrut(50));
        jpanelOne.add(new JLabel("列:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mCutColsText);
        jpanelOne.add(Box.createHorizontalStrut(20));

        JPanel jpanelTwo = new JPanel();
        jpanelTwo.setLayout(new BoxLayout(jpanelTwo, BoxLayout.X_AXIS));
        northPanel.add(jpanelTwo);
        northPanel.add(Box.createVerticalStrut(5));
        jpanelTwo.add(Box.createHorizontalStrut(20));
        jpanelTwo.add(new JLabel("宽:"));
        jpanelTwo.add(Box.createHorizontalStrut(10));
        jpanelTwo.add(mCutWidthText);
        jpanelTwo.add(Box.createHorizontalStrut(50));
        jpanelTwo.add(new JLabel("高:"));
        jpanelTwo.add(Box.createHorizontalStrut(10));
        jpanelTwo.add(mCutHeightText);
        jpanelTwo.add(Box.createHorizontalStrut(20));

        JPanel jpanelThree = new JPanel();
        jpanelThree.setLayout(new BoxLayout(jpanelThree, BoxLayout.X_AXIS));
        northPanel.add(jpanelThree);
        northPanel.add(Box.createVerticalStrut(5));
        jpanelThree.add(Box.createHorizontalStrut(20));
        jpanelThree.add(new JLabel("左右黑边:"));
        jpanelThree.add(Box.createHorizontalStrut(10));
        jpanelThree.add(mCutSideWidthText);
        jpanelThree.add(Box.createHorizontalStrut(50));
        jpanelThree.add(new JLabel("上下黑边:"));
        jpanelThree.add(Box.createHorizontalStrut(10));
        jpanelThree.add(mCutSideHeightText);
        jpanelThree.add(Box.createHorizontalStrut(20));

        mPng2SvgLayout.add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());
        mPng2SvgLayout.add(southPanel, BorderLayout.SOUTH);

        mCutPngButton = new JButton("图片切割");
        mCutPngButton.addActionListener(this);
        southPanel.add(mCutPngButton);

        mPng2SvgButton = new JButton("png转Svg");
        mPng2SvgButton.addActionListener(this);
        southPanel.add(mPng2SvgButton);

        mPng2SvgTextArea = new JTextArea();
        mPng2SvgTextArea.setLineWrap(true);
        mPng2SvgTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mPng2SvgTextArea);
        mPng2SvgLayout.add(scrollPane, BorderLayout.CENTER);
    }

    private void createFontelloLayout() {
        mFontelloLayout = new JPanel();
        mFontelloLayout.setLayout(new BorderLayout());

        mNorthPanel = new JPanel();

        mNorthPanel.setLayout(new BoxLayout(mNorthPanel, BoxLayout.Y_AXIS));
        mNorthPanel.setBounds(0, 0, M_WIDTH, 30);

        JPanel jpanelOne = new JPanel();
        jpanelOne.setLayout(new BoxLayout(jpanelOne, BoxLayout.X_AXIS));
        mNorthPanel.add(Box.createVerticalStrut(5));
        mNorthPanel.add(jpanelOne);
        mNorthPanel.add(Box.createVerticalStrut(5));

        mUnitesText = new JTextField();
        mAscentText = new JTextField();
        mUnitesText.addKeyListener(mKeyAdapter);
        mAscentText.addKeyListener(mKeyAdapter);
        mFontNameText = new JTextField();

        jpanelOne.add(Box.createHorizontalStrut(20));
        jpanelOne.add(new JLabel("units_per_em:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mUnitesText);
        jpanelOne.add(Box.createHorizontalStrut(50));
        jpanelOne.add(new JLabel("ascent:"));
        jpanelOne.add(Box.createHorizontalStrut(10));
        jpanelOne.add(mAscentText);
        jpanelOne.add(Box.createHorizontalStrut(20));

        JPanel jpanelTwo = new JPanel();
        jpanelTwo.setLayout(new BoxLayout(jpanelTwo, BoxLayout.X_AXIS));
        mNorthPanel.add(jpanelTwo);
        mNorthPanel.add(Box.createVerticalStrut(5));
        jpanelTwo.add(Box.createHorizontalStrut(20));
        jpanelTwo.add(new JLabel("font_name:"));
        jpanelTwo.add(Box.createHorizontalStrut(10));
        jpanelTwo.add(mFontNameText);
        jpanelTwo.add(Box.createHorizontalStrut(20));

        mFontelloLayout.add(mNorthPanel, BorderLayout.NORTH);

        mSouthPanel = new JPanel();
        mSouthPanel.setLayout(new FlowLayout());
        mFontelloLayout.add(mSouthPanel, BorderLayout.SOUTH);

        mSelectButton = new JButton("选择svg图片");
        mSelectButton.addActionListener(this);
        mSouthPanel.add(mSelectButton);

        mConfigButton = new JButton("生成配置文件");
        mConfigButton.addActionListener(this);
        mSouthPanel.add(mConfigButton);

        mFontelloButton = new JButton("生成字体");
        mFontelloButton.addActionListener(this);
        mSouthPanel.add(mFontelloButton);

        mFontelloTextArea = new JTextArea();
        mFontelloTextArea.setLineWrap(true);
        mFontelloTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mFontelloTextArea);

        mFontelloLayout.add(scrollPane, BorderLayout.CENTER);

        mFileChooser = new JFileChooser();
        mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        mFileChooser.setDialogTitle("请选择包含svg图片的目录!");
    }

    private void loadData() {
        File file = new File(SystemConfig.FileSystem.SVG_DIR);
        if (file.exists() && file.isDirectory()) {
            showSvgFileToArea(file);
        }
        mUnitesText.setText(SystemConfig.DefalutConfig.unitsPerEm);
        mAscentText.setText(SystemConfig.DefalutConfig.ascent);
        mFontNameText.setText(SystemConfig.DefalutConfig.fontName);

        File configFile = new File(SystemConfig.FileSystem.CONFIG_FILE);
        if (configFile.exists()) {
            mFontelloButton.setEnabled(true);
            MyLog.w("data目录下有配置文件，可以直接生成字库,或重新生成配置文件!");
        }

        mCutColsText.setText(SystemConfig.DefalutConfig.sCUT_PNG_COLS + "");
        mCutRowsText.setText(SystemConfig.DefalutConfig.sCUT_PNG_ROWS + "");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mSelectButton) {
            int state = mFileChooser.showOpenDialog(null);
            if (state == 1) {
                return;
            } else {
                File f = mFileChooser.getSelectedFile();
                showSvgFileToArea(f);
            }
        } else if (e.getSource() == mConfigButton) {
            buildConfig();
        } else if (e.getSource() == mFontelloButton) {
            startFontello();
        } else if (e.getSource() == mPng2SvgItem) {
            getContentPane().remove(mFontelloLayout);
            getContentPane().add(mPng2SvgLayout, BorderLayout.CENTER);
            MyLog.setTextArea(mPng2SvgTextArea);
            mPng2SvgLayout.updateUI();
        } else if (e.getSource() == mFontelloItem) {
            getContentPane().remove(mPng2SvgLayout);
            getContentPane().add(mFontelloLayout, BorderLayout.CENTER);
            MyLog.setTextArea(mFontelloTextArea);
            mFontelloLayout.updateUI();
        } else if (e.getSource() == mCutPngButton) {
            startCutPng();
        } else if (e.getSource() == mPng2SvgButton) {
            startPng2Svg();
        }
    }

    private void startFontello() {
        mFontelloButton.setEnabled(false);
        mConfigButton.setEnabled(false);
        mSelectButton.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyLog.w("正在生成 字库文件......");
                boolean ok = mService.startWork(2, mConfig);
                endFontello(ok);
            }
        }.start();
    }

    private void endFontello(boolean ok) {
        if (ok) {
            MyLog.w("字库已生成，请查看应用data目录!");
        } else {
            MyLog.w("字库生成失败，请重试!");
        }
        mFontelloButton.setEnabled(true);
        mConfigButton.setEnabled(true);
        mSelectButton.setEnabled(true);
    }

    private void buildConfig() {
        if (mConfig == null) {
            mConfig = new SvgConfig();
        }
        if (TextUtils.isEmpty(mConfig.getSvgDir())) {
            MyLog.w("没有导入svg图片，请重新导入!");
            return;
        }
        String ascent = mAscentText.getText();
        String unitsPerEm = mUnitesText.getText();
        String fontName = mFontNameText.getText();
        if (TextUtils.isEmpty(unitsPerEm)) {
            MyLog.w("请输入 ascent 值!");
            return;
        }
        if (TextUtils.isEmpty(ascent)) {
            MyLog.w("请输入 units_per_em 值!");
            return;
        }
        if (TextUtils.isEmpty(ascent)) {
            MyLog.w("字体名称为空,将使用默认名称 !");
            fontName = SystemConfig.DefalutConfig.fontName;
        }
        mConfig.setAscent(ascent);
        mConfig.setUnitsPerEm(unitsPerEm);
        mConfig.setName(fontName);
        mFontelloButton.setEnabled(false);
        mConfigButton.setEnabled(false);
        mSelectButton.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyLog.w("正在生成 config.json文件......");
                boolean ok = mService.startWork(1, mConfig);
                if (ok) {
                    MyLog.w("config.json文件已生成，点击 生成字体 按钮生成字库文件！!");
                    mFontelloButton.setEnabled(true);
                } else {
                    MyLog.w("config.json文件生成失败，请重试!");
                    mFontelloButton.setEnabled(false);
                }
                mConfigButton.setEnabled(true);
                mSelectButton.setEnabled(true);
            }
        }.start();
    }

    private void showSvgFileToArea(File file) {
        mFontelloButton.setEnabled(false);
        if (!file.exists() || !file.isDirectory()) {
            MyLog.w("选择的目录不存在或不是目录!");
            mConfigButton.setEnabled(false);
            return;
        } else {
            File[] svgFiles = file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile()
                            && pathname.getName().endsWith(".svg");
                }
            });
            if (svgFiles == null || svgFiles.length <= 0) {
                MyLog.w("目录中没有svg图片，请重新选择");
                mConfigButton.setEnabled(false);
            } else {
                mConfigButton.setEnabled(true);
                mConfig = new SvgConfig();
                mConfig.setSvgDir(file.getAbsolutePath());
                for (File svg : svgFiles) {
                    MyLog.w(svg.getName());
                }
                MyLog.w("共 " + svgFiles.length
                        + " 个svg文件, 点击 生成配置文件 生成config.json！");
            }
        }
    }

    private void startCutPng() {
        String cutCols = mCutColsText.getText();
        String cutRows = mCutRowsText.getText();
        String cutWidth = mCutWidthText.getText();
        String cutHeight = mCutHeightText.getText();
        String sideWidth = mCutSideWidthText.getText();
        String sideHeight = mCutSideHeightText.getText();
        if (!TextUtils.isEmpty(cutCols)) {
            SystemConfig.DefalutConfig.sCUT_PNG_COLS = Integer
                    .parseInt(cutCols);
        } else {
            SystemConfig.DefalutConfig.sCUT_PNG_COLS = 0;
        }
        if (!TextUtils.isEmpty(cutRows)) {
            SystemConfig.DefalutConfig.sCUT_PNG_ROWS = Integer
                    .parseInt(cutRows);
        } else {
            SystemConfig.DefalutConfig.sCUT_PNG_ROWS = 0;
        }
        if (!TextUtils.isEmpty(cutWidth)) {
            SystemConfig.DefalutConfig.sPNG_WIDTH = Integer.parseInt(cutWidth);
        } else {
            SystemConfig.DefalutConfig.sPNG_WIDTH = 0;
        }
        if (!TextUtils.isEmpty(cutHeight)) {
            SystemConfig.DefalutConfig.sPNG_HEIGHT = Integer
                    .parseInt(cutHeight);
        } else {
            SystemConfig.DefalutConfig.sPNG_HEIGHT = 0;
        }
        if (!TextUtils.isEmpty(sideWidth)) {
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_WIDTH = Integer
                    .parseInt(sideWidth);
        } else {
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_WIDTH = 0;
        }
        if (!TextUtils.isEmpty(sideHeight)) {
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_HEIGHT = Integer
                    .parseInt(sideHeight);
        } else {
            SystemConfig.DefalutConfig.sPNG_BLACK_SIDE_HEIGHT = 0;
        }
        mCutPngButton.setEnabled(false);
        mPng2SvgButton.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyLog.w("开始切图.....");
                mService.doButtonCmd(FontelloService.CMD_CUT_PNG, "");
                MyLog.w("切图完成,请查看data/png/目录");
                mCutPngButton.setEnabled(true);
                mPng2SvgButton.setEnabled(true);
            }
        }.start();
    }

    private void startPng2Svg() {
        mCutPngButton.setEnabled(false);
        mPng2SvgButton.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();
                MyLog.w("开始转换.....");
                mService.doButtonCmd(FontelloService.CMD_PNG_2_SVG, "");
                MyLog.w("切图完成,请查看data/svg/目录");
                mCutPngButton.setEnabled(true);
                mPng2SvgButton.setEnabled(true);
            }
        }.start();
    }

    private void showNoActiveDialog() {
        if (!FontelloService.getInstance().isActivation()) {
            String mac = CommonUtils.getLocalMac();
            JOptionPane.showMessageDialog(this, "请提供电脑的 MAC地址给相关人员\n MAC:"
                    + mac, "你的电脑未激活!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
