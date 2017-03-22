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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mephone.fontello.FontelloService;
import com.mephone.fontello.bean.SvgConfig;
import com.mephone.fontello.config.MyLog;
import com.mephone.fontello.config.SystemConfig;
import com.mephone.fontello.util.TextUtils;

public class FontelloFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = -1186812329395127121L;

    private static final int M_WIDTH = 400;
    private static final int M_HEIGHT = 400;

    private JFileChooser mFileChooser = null;

    private JPanel mSouthPanel;
    private JPanel mNorthPanel;
    private JButton mConfigButton;
    private JButton mFontelloButton;
    private JButton mSelectButton;
    private JTextArea mTextArea;

    private JTextField mUnitesText;
    private JTextField mAscentText;
    private JTextField mFontNameText;

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
        init();
        loadData();
    }

    private void init() {
        this.setLayout(new BorderLayout());

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

        this.add(mNorthPanel, BorderLayout.NORTH);

        mSouthPanel = new JPanel();
        mSouthPanel.setLayout(new FlowLayout());
        this.add(mSouthPanel, BorderLayout.SOUTH);

        mSelectButton = new JButton("选择svg图片");
        mSelectButton.addActionListener(this);
        mSouthPanel.add(mSelectButton);

        mConfigButton = new JButton("生成配置文件");
        mConfigButton.addActionListener(this);
        mSouthPanel.add(mConfigButton);

        mFontelloButton = new JButton("生成字体");
        mFontelloButton.addActionListener(this);
        mSouthPanel.add(mFontelloButton);

        mTextArea = new JTextArea();
        mTextArea.setLineWrap(true);
        mTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(mTextArea);

        this.add(scrollPane, BorderLayout.CENTER);
        MyLog.setTextArea(mTextArea);

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

}
