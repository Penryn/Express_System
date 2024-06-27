package express;

import javax.swing.*;
import java.awt.*;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class MainFrame extends JFrame {
    private List expressList;

    // MainFrame构造函数
    public MainFrame() {
        expressList = new List();
        expressList = expressList.readCSVToExpressList("src/data/express.csv");
        initFrame();
        initMenu();
        // 设置窗口可见
        setVisible(true);
    }

    // 从文件中读取秘钥
    private String readSecretKeyFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 验证用户输入的秘钥
    private boolean authenticateUser(String secretKey) {
        // 弹出对话框，要求用户输入密码
        String inputKey = JOptionPane.showInputDialog(this, "请输入密码：", "密码验证", JOptionPane.PLAIN_MESSAGE);

        // 如果用户点击了取消按钮，退出程序
        if (inputKey == null) {
            System.exit(0);
        }

        // 检查用户输入的密码是否与预设的密码匹配
        return secretKey != null && secretKey.equals(inputKey);
    }

    // 初始化窗口,并鉴权
    public void initFrame() {
        // 设置窗口标题
        setTitle("快递运单管理系统");
        // 设置窗口大小
        setSize(800, 600);
        // 设置窗口关闭按钮的默认操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口居中
        setLocationRelativeTo(null);
        // 设置窗口大小不可变
        setResizable(false);
        setLayout(new BorderLayout());

        // 读取秘钥
        String secretKey = readSecretKeyFromFile("src/data/key.txt");
        while (!authenticateUser(secretKey)) {
            JOptionPane.showMessageDialog(this, "秘钥错误，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void initMenu() {
        // 创建左侧按钮面板
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(12, 10, 10, 10));

        // 添加按钮
        // 创建并添加按钮
        JButton addButton = new JButton("录入快递运单信息");
        JButton searchButton = new JButton("查询快递运单信息");
        JButton deleteButton = new JButton("删除快递运单信息");
        JButton updateButton = new JButton("修改快递运单信息");
        JButton sortButton = new JButton("时间排序运单信息");
        JButton exitButton = new JButton("退出");


        leftPanel.add(addButton);
        leftPanel.add(searchButton);
        leftPanel.add(deleteButton);
        leftPanel.add(updateButton);
        leftPanel.add(sortButton);
        leftPanel.add(exitButton);

        // 右侧显示区
        // 创建右侧面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // 显示文字
        JLabel label = new JLabel("欢迎使用快递运单管理系统", JLabel.CENTER);
        label.setFont(new Font("楷体", Font.BOLD, 30));
        rightPanel.add(label, BorderLayout.CENTER);


        // 创建分隔窗格
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(200); // 设置分隔窗格位置
        splitPane.setEnabled(false); // 禁用分隔窗格调整大小

        getContentPane().add(splitPane, BorderLayout.CENTER);

        // 为按钮添加监听器
        addButton.addActionListener(e -> createExpress(rightPanel));
        searchButton.addActionListener(e -> searchExpress(rightPanel));
        deleteButton.addActionListener(e -> deleteExpress(rightPanel));
        updateButton.addActionListener(e -> updateExpress(rightPanel));
        sortButton.addActionListener(e -> sortByTime(rightPanel));
        exitButton.addActionListener(e -> System.exit(0));


    }

    private void createExpress(JPanel rightPanel) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // 创建输入表单
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 使用GridLayout，每行两列，间距为10像素
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 添加边框以增加空白边距

        // 创建表单字段
        String[] labels = {
                "运单号：", "运单类型：", "寄件人地址信息：", "寄件人电话信息：","寄件人备注信息：", "寄件时间信息：",
                "收件人地址信息：", "收件人电话信息：", "收件人备注信息：", "收件时间信息：", "是否签收：", "是否为难寄快递：", "难寄快递原因：", "金额："
        };
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            formPanel.add(new JLabel(labels[i])); // 添加标签

            if (i == 1 || i == 10 || i == 11 || i == 12) {
                // 创建下拉选择框
                JComboBox<String> comboBox = getStringJComboBox(i);
                // 将下拉选择框添加到表单中
                formPanel.add(comboBox);
            }else if(i ==5||i ==9){
                JTextField textField = new JTextField("年-月-日");
                formPanel.add(textField);
                fields[i] = textField;
            }
            else {
                // 对于其他位置，创建文本字段
                JTextField textField = new JTextField();
                // 将文本字段添加到表单中
                formPanel.add(textField);
                // 将文本字段添加到fields数组中，以便后续访问
                fields[i] = textField;
            }
        }

        // 确定按钮
        JButton confirmButton = new JButton("确定");

        // 创建一个新的面板来包装确认按钮并使用FlowLayout使其居中
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirmButton);

        // 添加输入表单到右侧面板
        rightPanel.add(formPanel, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH); // 将确认按钮放在底部

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();

        // 添加确定按钮的监听器
        confirmButton.addActionListener(e -> {
            // 获取文本字段的值
            String[] values = new String[labels.length];
            for (int i = 0; i < fields.length; i++) {
                if (fields[i] != null) {
                    values[i] = fields[i].getText();
                } else {
                    // 获取下拉框的值
                    JComboBox<String> comboBox = (JComboBox<String>) formPanel.getComponent(i * 2 + 1);
                    values[i] = (String) comboBox.getSelectedItem();
                }
            }

            // 确保所有必填字段都已填写
            if (validateInput(values)) {
                // 创建Express对象
                Express express = new Express();
                express.setWaybillNumber(values[0]);
                express.setWaybillType(values[1]);

                Sender sender = new Sender();
                sender.setAddress(values[2]);
                sender.setPhone(values[3]);
                sender.setRemark(values[4]);
                Time senderTime = parseTime(values[5]); // 此处需要定义parseTime方法，用于解析时间字符串
                sender.setTime(senderTime);

                express.setSender(sender);

                Receiver receiver = new Receiver();
                receiver.setAddress(values[6]);
                receiver.setPhone(values[7]);
                receiver.setRemark(values[8]);
                Time receiverTime = parseTime(values[9]); // 此处需要定义parseTime方法，用于解析时间字符串
                receiver.setTime(receiverTime);

                express.setReceiver(receiver);

                express.setIsSign(parseBool(values[10]));
                express.setIsDifficult(parseBool(values[11]));
                express.setDifficultReason((values[12]));
                express.setAmount(Double.parseDouble(values[13]));

                // 添加到list
                expressList.add(express);
                expressList.writeExpressListToCSV("src/data/express.csv");
                expressList.print();

                // 显示成功消息
                JOptionPane.showMessageDialog(this, "添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 清除表单数据
                clearForm(fields);

            } else {
                // 显示错误消息或提示用户填写必填字段
                JOptionPane.showMessageDialog(this, "请填写所有必填字段", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private static JComboBox<String> getStringJComboBox(int i) {
        JComboBox<String> comboBox = new JComboBox<>();
        if (i == 1) {
            // 添加运单类型选项
            comboBox.addItem("寄件单");
            comboBox.addItem("收件单");
        } else if (i == 10 || i == 11) {
            // 添加是否签收/是否为难寄快递选项
            comboBox.addItem("是");
            comboBox.addItem("否");
        } else {
            // 添加难寄快递原因选项
            comboBox.addItem("地址模糊");
            comboBox.addItem("用户拒收");
            comboBox.addItem("电话无效");
            comboBox.addItem("收件人不在");
            comboBox.addItem("其他");
        }
        return comboBox;
    }

    private void searchExpress(JPanel rightPanel) {
        // 创建输入表单
        JPanel formPanel = new JPanel();

        // 创建标签和文本字段
        JLabel orderNumberLabel = new JLabel("需查询的订单号：");
        JTextField orderNumberField = new JTextField(20);

        // 添加标签和文本字段到表单中
        formPanel.add(orderNumberLabel);
        formPanel.add(orderNumberField);

        // 创建搜索按钮
        JButton searchButton = new JButton("搜索");

        // 添加搜索按钮的监听器
        searchButton.addActionListener(e -> {
            // 获取订单号
            String orderNumber = orderNumberField.getText();

            // 搜索订单
            Express express = expressList.search(orderNumber);

            // 如果找到订单，显示订单信息
            if (express != null) {
                // 创建新的面板来显示订单信息
                JPanel resultPanel = new JPanel();
                resultPanel.setLayout(new GridLayout(0, 2, 10, 10));
                resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // 显示订单信息
                resultPanel.add(new JLabel("运单号："));
                resultPanel.add(new JLabel(express.getWaybillNumber()));

                resultPanel.add(new JLabel("运单类型："));
                resultPanel.add(new JLabel(express.getWaybillType()));

                resultPanel.add(new JLabel("寄件人地址信息："));
                resultPanel.add(new JLabel(express.getSender().getAddress()));

                resultPanel.add(new JLabel("寄件人电话信息："));
                resultPanel.add(new JLabel(express.getSender().getPhone()));

                resultPanel.add(new JLabel("寄件人备注信息："));
                resultPanel.add(new JLabel(express.getSender().getRemark()));

                resultPanel.add(new JLabel("寄件时间信息："));
                resultPanel.add(new JLabel(express.getSender().getTime().getTime()));

                resultPanel.add(new JLabel("收件人地址信息："));
                resultPanel.add(new JLabel(express.getReceiver().getAddress()));

                resultPanel.add(new JLabel("收件人电话信息："));
                resultPanel.add(new JLabel(express.getReceiver().getPhone()));

                resultPanel.add(new JLabel("收件人备注信息："));
                resultPanel.add(new JLabel(express.getReceiver().getRemark()));

                resultPanel.add(new JLabel("收件时间信息："));
                resultPanel.add(new JLabel(express.getReceiver().getTime().getTime()));

                resultPanel.add(new JLabel("是否签收："));
                resultPanel.add(new JLabel(express.getIsSign() ? "是" : "否"));

                resultPanel.add(new JLabel("是否为难寄快递："));
                resultPanel.add(new JLabel(express.getIsDifficult() ? "是" : "否"));

                resultPanel.add(new JLabel("难寄快递原因："));
                resultPanel.add(new JLabel(express.getDifficultReason()));

                resultPanel.add(new JLabel("金额："));
                resultPanel.add(new JLabel(String.valueOf(express.getAmount())));

                // 清除右侧面板的所有组件
                rightPanel.removeAll();
                rightPanel.revalidate();
                rightPanel.repaint();

                // 添加订单信息面板到右侧面板的下方
                rightPanel.setLayout(new BorderLayout());
                rightPanel.add(formPanel, BorderLayout.NORTH);
                rightPanel.add(resultPanel, BorderLayout.CENTER);
            } else {
                // 如果未找到订单，显示错误消息
                JOptionPane.showMessageDialog(this, "未找到订单", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 将搜索按钮添加到表单中
        formPanel.add(searchButton);

        // 清除右侧面板的所有组件
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // 添加输入表单到右侧面板的顶部
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.NORTH);

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    public void deleteExpress(JPanel rightPanel) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // 创建输入表单
        JPanel formPanel = new JPanel();

        // 创建标签和文本字段
        JLabel orderNumberLabel = new JLabel("需删除的订单号：");
        JTextField orderNumberField = new JTextField(20);

        // 添加标签和文本字段到表单中
        formPanel.add(orderNumberLabel);
        formPanel.add(orderNumberField);

        // 创建删除按钮
        JButton deleteButton = new JButton("删除");

        // 将删除按钮添加到表单中
        formPanel.add(deleteButton);

        // 添加输入表单到右侧面板的顶部
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.NORTH);


        // 刷新右侧面板以显示原有的订单信息（第一页）
        refreshRightPanel(rightPanel, 1);

        // 添加删除按钮的监听器
        deleteButton.addActionListener(e -> {
            // 获取订单号
            String orderNumber = orderNumberField.getText();

            // 查找订单并删除
            boolean removed = expressList.remove(orderNumber);

            // 如果成功删除，则刷新右侧面板
            if (removed) {
                // 显示成功消息
                expressList.writeExpressListToCSV("src/data/express.csv");
                JOptionPane.showMessageDialog(rightPanel, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 刷新右侧面板以反映更改
                refreshRightPanel(rightPanel, 1);
            } else {
                // 显示错误消息
                JOptionPane.showMessageDialog(rightPanel, "未找到订单", "错误", JOptionPane.ERROR_MESSAGE);
            }

            // 清空订单号文本框
            orderNumberField.setText("");
        });

    }


    // 刷新右侧面板以反映更改
    private void refreshRightPanel(JPanel rightPanel, int currentPage) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();

        // 设置面板布局和边距
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输入表单
        JPanel formPanel = new JPanel();

        // 创建标签和文本字段
        JLabel orderNumberLabel = new JLabel("需删除的订单号：");
        JTextField orderNumberField = new JTextField(20);

        // 添加标签和文本字段到表单中
        formPanel.add(orderNumberLabel);
        formPanel.add(orderNumberField);

        // 创建删除按钮
        JButton deleteButton = new JButton("删除");

        // 将删除按钮添加到表单中
        formPanel.add(deleteButton);

        // 添加输入表单到右侧面板的顶部
        rightPanel.add(formPanel, BorderLayout.NORTH);

        // 添加删除按钮的监听器
        deleteButton.addActionListener(e -> {
            // 获取订单号
            String orderNumber = orderNumberField.getText();

            // 查找订单并删除
            boolean removed = expressList.remove(orderNumber);

            // 如果成功删除，则刷新右侧面板
            if (removed) {
                // 显示成功消息
                JOptionPane.showMessageDialog(rightPanel, "删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                // 刷新右侧面板以反映更改
                refreshRightPanel(rightPanel, 1);
            } else {
                // 显示错误消息
                JOptionPane.showMessageDialog(rightPanel, "未找到订单", "错误", JOptionPane.ERROR_MESSAGE);
            }

            // 清空订单号文本框
            orderNumberField.setText("");
        });

        // 创建面板来显示订单信息
        JPanel orderPanel = new JPanel();

        // 计算每页显示的订单数量
        int pageSize = 12;

        // 计算总页数
        int totalPages = (int) Math.ceil((double) expressList.getList().size() / pageSize);

        // 计算当前页的订单起始索引和结束索引
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, expressList.getList().size());

        // 设置订单信息面板的布局为 GridLayout，每行显示一条订单信息
        orderPanel.setLayout(new GridLayout(pageSize, 1, 15, 15));

        // 遍历订单列表并添加订单信息到面板中
        for (int i = startIndex; i < endIndex; i++) {
            Express express = expressList.getList().get(i);

            // 添加订单号
            orderPanel.add(new JLabel("订单号：" + express.getWaybillNumber()));
            // 添加运单类型
            orderPanel.add(new JLabel("运单类型：" + express.getWaybillType()));
        }

        // 将订单信息面板添加到右侧面板
        rightPanel.add(orderPanel, BorderLayout.CENTER);

        // 创建分页按钮面板
        JPanel paginationPanel = getjPanel(rightPanel, currentPage);

        // 将分页按钮面板添加到右侧面板的底部
        rightPanel.add(paginationPanel, BorderLayout.SOUTH);

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();
    }



    private JPanel getjPanel(JPanel rightPanel, int currentPage) {
        JPanel paginationPanel = new JPanel();
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 添加上一页按钮
        JButton prevButton = new JButton("上一页");
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                refreshRightPanel(rightPanel, currentPage - 1); // 显示上一页的订单信息
            }
        });
        paginationPanel.add(prevButton);

        // 添加当前页码标签
        JLabel pageLabel = new JLabel("当前页：" + currentPage);
        paginationPanel.add(pageLabel);

        // 添加下一页按钮
        JButton nextButton = new JButton("下一页");
        nextButton.addActionListener(e -> {
            int totalPages = (int) Math.ceil((double) expressList.getList().size() / 12); // 计算总页数
            if (currentPage < totalPages) {
                refreshRightPanel(rightPanel, currentPage + 1); // 显示下一页的订单信息
            }
        });
        paginationPanel.add(nextButton);
        return paginationPanel;
    }

    public void updateExpress(JPanel rightPanel) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // 创建输入表单
        JPanel formPanel = new JPanel();

        // 创建标签和文本字段
        JLabel orderNumberLabel = new JLabel("需修改的订单号：");
        JTextField orderNumberField = new JTextField(20);

        // 添加标签和文本字段到表单中
        formPanel.add(orderNumberLabel);
        formPanel.add(orderNumberField);

        // 创建搜索按钮
        JButton searchButton = new JButton("搜索");

        // 添加搜索按钮的监听器
        searchButton.addActionListener(e -> {
            // 获取订单号
            String orderNumber = orderNumberField.getText();

            // 搜索订单
            Express express = expressList.search(orderNumber);

            // 如果找到订单，显示订单信息并填入输入框
            if (express != null) {
                // 创建修改表单
                JPanel updateFormPanel = new JPanel();
                updateFormPanel.setLayout(new GridLayout(0, 2, 10, 10));
                updateFormPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // 创建表单字段
                String[] labels = {
                        "运单号：", "运单类型：", "寄件人地址信息：", "寄件人电话信息：", "寄件人备注信息：", "寄件时间信息：",
                        "收件人地址信息：", "收件人电话信息：", "收件人备注信息：", "收件时间信息：", "是否签收：", "是否为难寄快递：", "难寄快递原因：", "金额："
                };
                JTextField[] fields = new JTextField[labels.length];

                // 用于填充表单字段的初始值
                String[] initialValues = {
                        express.getWaybillNumber(),
                        express.getWaybillType(),
                        express.getSender().getAddress(),
                        express.getSender().getPhone(),
                        express.getSender().getRemark(),
                        express.getSender().getTime().getTime(),
                        express.getReceiver().getAddress(),
                        express.getReceiver().getPhone(),
                        express.getReceiver().getRemark(),
                        express.getReceiver().getTime().getTime(),
                        express.getIsSign() ? "是" : "否",
                        express.getIsDifficult() ? "是" : "否",
                        express.getDifficultReason(),
                        String.valueOf(express.getAmount())
                };

                for (int i = 0; i < labels.length; i++) {
                    updateFormPanel.add(new JLabel(labels[i])); // 添加标签

                    if (i == 10 || i == 11) {
                        // 创建下拉选择框
                        JComboBox<String> comboBox = new JComboBox<>(new String[]{"是", "否"});
                        comboBox.setSelectedItem(initialValues[i]);
                        updateFormPanel.add(comboBox);
                    }else if(i==1){
                        // 创建下拉选择框
                        JComboBox<String> comboBox = new JComboBox<>(new String[]{"寄件单", "收件单"});
                        comboBox.setSelectedItem(initialValues[i]);
                        updateFormPanel.add(comboBox);
                    } else if (i == 5 || i == 9) {
                        JTextField textField = new JTextField(initialValues[i]);
                        updateFormPanel.add(textField);
                        fields[i] = textField;
                    }else if (i == 12) {
                        // 创建下拉选择框
                        JComboBox<String> comboBox = new JComboBox<>(new String[]{"地址模糊", "用户拒收", "电话无效", "收件人不在", "其他"});
                        comboBox.setSelectedItem(initialValues[i]);
                        updateFormPanel.add(comboBox);
                    } else {
                        JTextField textField = new JTextField(initialValues[i]);
                        updateFormPanel.add(textField);
                        fields[i] = textField;
                    }
                }

                // 确定按钮
                JButton confirmButton = new JButton("确定");

                // 创建一个新的面板来包装确认按钮并使用FlowLayout使其居中
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                buttonPanel.add(confirmButton);

                // 添加修改表单到右侧面板
                rightPanel.removeAll();
                rightPanel.revalidate();
                rightPanel.repaint();
                rightPanel.setLayout(new BorderLayout());
                rightPanel.add(updateFormPanel, BorderLayout.CENTER);
                rightPanel.add(buttonPanel, BorderLayout.SOUTH); // 将确认按钮放在底部

                // 重新布局
                rightPanel.revalidate();
                rightPanel.repaint();

                // 添加确定按钮的监听器
                confirmButton.addActionListener(e1 -> {
                    // 获取文本字段的值
                    String[] values = new String[labels.length];
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i] != null) {
                            values[i] = fields[i].getText();
                        } else {
                            // 获取下拉框的值
                            JComboBox<String> comboBox = (JComboBox<String>) updateFormPanel.getComponent(i * 2 + 1);
                            values[i] = (String) comboBox.getSelectedItem();
                        }
                    }

                    // 确保所有必填字段都已填写
                    if (validateInput(values)) {
                        // 更新Express对象
                        express.setWaybillNumber(values[0]);
                        express.setWaybillType(values[1]);

                        Sender sender = express.getSender();
                        sender.setAddress(values[2]);
                        sender.setPhone(values[3]);
                        sender.setRemark(values[4]);
                        sender.setTime(parseTime(values[5])); // 此处需要定义parseTime方法，用于解析时间字符串

                        Receiver receiver = express.getReceiver();
                        receiver.setAddress(values[6]);
                        receiver.setPhone(values[7]);
                        receiver.setRemark(values[8]);
                        receiver.setTime(parseTime(values[9])); // 此处需要定义parseTime方法，用于解析时间字符串
                        express.setSender(sender);
                        express.setReceiver(receiver);

                        express.setIsSign(values[10].equals("是"));
                        express.setIsDifficult(values[11].equals("是"));
                        express.setDifficultReason(values[12]);
                        express.setAmount(Double.parseDouble(values[13]));

                        // 更新订单信息
                        expressList.writeExpressListToCSV("src/data/express.csv");

                        // 显示成功消息
                        JOptionPane.showMessageDialog(rightPanel, "修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                        // 清除表单数据
                        clearForm(fields);
                    } else {
                        // 显示错误消息或提示用户填写必填字段
                        JOptionPane.showMessageDialog(rightPanel, "请填写所有必填字段", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else {
                // 如果未找到订单，显示错误消息
                JOptionPane.showMessageDialog(rightPanel, "未找到订单", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 将搜索按钮添加到表单中
        formPanel.add(searchButton);

        // 添加输入表单到右侧面板的顶部
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.NORTH);

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    int currentPage = 1;
    public void sortByTime(JPanel rightPanel) {
        sortByTime(rightPanel, true); // 默认顺序排序
    }

    public void sortByTime(JPanel rightPanel, boolean ascendingOrder) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();
        rightPanel.revalidate();
        rightPanel.repaint();

        // 获取排序后的列表
        List sortedList = expressList.sortByTime(ascendingOrder);

        // 设置每页显示的订单数量和每行显示的列数
        int pageSize = 12;
        int columnsPerPage = 2;

        // 创建面板来显示订单信息和选择排序方式
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 创建面板来显示订单信息
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new GridLayout(0, columnsPerPage, 15, 15)); // 每行两列

        // 计算当前页的订单起始索引和结束索引
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(pageSize, sortedList.getList().size());

        // 遍历排序后的订单列表并添加订单信息到面板中
        for (int i = startIndex; i < endIndex; i++) {
            Express express = sortedList.getList().get(i);

            // 添加订单号和收件时间
            orderPanel.add(new JLabel("订单号：" + express.getWaybillNumber()));
            orderPanel.add(new JLabel("收件时间：" + express.getReceiver().getTime().getTime()));
        }

        // 将订单信息面板添加到主面板中
        contentPanel.add(orderPanel, BorderLayout.CENTER);

        // 创建排序方式选择组件
        JRadioButton ascendingButton = new JRadioButton("顺序", ascendingOrder);
        JRadioButton descendingButton = new JRadioButton("逆序", !ascendingOrder);

        // 添加按钮到按钮组
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ascendingButton);
        buttonGroup.add(descendingButton);

        // 创建面板来放置排序方式选择组件
        JPanel radioPanel = new JPanel();
        radioPanel.add(new JLabel("排序方式:"));
        radioPanel.add(ascendingButton);
        radioPanel.add(descendingButton);

        // 添加监听器，根据选择的排序方式重新排序并更新订单信息显示
        ascendingButton.addActionListener(e -> sortByTime(rightPanel, true));
        descendingButton.addActionListener(e -> sortByTime(rightPanel, false));

        // 将排序方式选择面板添加到主面板中
        contentPanel.add(radioPanel, BorderLayout.SOUTH);

        // 创建并添加分页按钮面板
        JPanel paginationPanel = getPaginationPanel(rightPanel, sortedList.getList().size(), pageSize, columnsPerPage);

        // 将主面板添加到右侧面板
        rightPanel.add(contentPanel, BorderLayout.CENTER);
        rightPanel.add(paginationPanel, BorderLayout.SOUTH);

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private JPanel getPaginationPanel(JPanel rightPanel, int totalOrders, int pageSize, int columnsPerPage) {
        JPanel paginationPanel = new JPanel();

        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");

        // 添加上一页按钮的监听器
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                refreshRightPanel(rightPanel, pageSize, columnsPerPage);
            }
        });

        // 添加下一页按钮的监听器
        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                refreshRightPanel(rightPanel, pageSize, columnsPerPage);
            }
        });

        paginationPanel.add(prevButton);
        paginationPanel.add(nextButton);

        return paginationPanel;
    }

    private void refreshRightPanel(JPanel rightPanel, int pageSize, int columnsPerPage) {
        // 清除右侧面板的所有组件
        rightPanel.removeAll();

        // 设置面板布局和边距
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel contentPanel = new JPanel(new BorderLayout());
        // 创建面板来显示订单信息
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new GridLayout(0, columnsPerPage, 15, 15)); // 每行两列

        // 计算当前页的订单起始索引和结束索引
        int startIndex = (currentPage - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, expressList.getList().size());

        // 获取排序后的列表
        List sortedList = expressList.sortByTime(true);

        // 遍历订单列表并添加订单信息到面板中
        for (int i = startIndex; i < endIndex; i++) {
            Express express = sortedList.getList().get(i);

            // 添加订单号和收件时间
            orderPanel.add(new JLabel("订单号：" + express.getWaybillNumber()));
            orderPanel.add(new JLabel("收件时间：" + express.getReceiver().getTime().getTime()));
        }

        // 计算剩余的空白行数
        int remainingRows = pageSize - (endIndex - startIndex);
        for (int i = 0; i < remainingRows; i++) {
            // 添加空白标签来保持间距
            orderPanel.add(new JLabel());
            orderPanel.add(new JLabel());
        }

        contentPanel.add(orderPanel, BorderLayout.CENTER);

        // 创建排序方式选择组件
        JRadioButton ascendingButton = new JRadioButton("顺序", true);
        JRadioButton descendingButton = new JRadioButton("逆序", false);

        // 添加按钮到按钮组
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(ascendingButton);
        buttonGroup.add(descendingButton);

        // 创建面板来放置排序方式选择组件
        JPanel radioPanel = new JPanel();
        radioPanel.add(new JLabel("排序方式:"));
        radioPanel.add(ascendingButton);
        radioPanel.add(descendingButton);

        // 添加监听器，根据选择的排序方式重新排序并更新订单信息显示
        ascendingButton.addActionListener(e -> sortByTime(rightPanel, true));
        descendingButton.addActionListener(e -> sortByTime(rightPanel, false));

        // 将排序方式选择面板添加到主面板中
        contentPanel.add(radioPanel, BorderLayout.SOUTH);

        // 将订单信息面板添加到右侧面板
        rightPanel.add(contentPanel, BorderLayout.CENTER);

        // 创建并添加分页按钮面板
        JPanel paginationPanel = getPaginationPanel(rightPanel, expressList.getList().size(), pageSize, columnsPerPage);
        rightPanel.add(paginationPanel, BorderLayout.SOUTH);

        // 重新布局
        rightPanel.revalidate();
        rightPanel.repaint();
    }




    // 验证输入字段是否已填写
    private boolean validateInput(String[] values) {
        for (String value : values) {
            if (value.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // 清除表单数据
    private void clearForm(JTextField[] fields) {
        for (JTextField field : fields) {
            if (field != null) {
                field.setText(""); // 清除文本字段的内容
            }
        }
    }

    private Time parseTime(String time){
        String[] timeArray = time.split("-");
        return new Time(Integer.parseInt(timeArray[0]), Integer.parseInt(timeArray[1]), Integer.parseInt(timeArray[2]));
    }

    private Boolean parseBool(String bool){
        return bool.equals("是");
    }


}
