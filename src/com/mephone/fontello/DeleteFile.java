package com.mephone.fontello;

import java.io.File;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.mephone.fontello.util.TextUtils;

public class DeleteFile {

    private final static String LINE_HEAD = "Image 2 path:";

    public static void main(String[] args) {
        DeleteFile df = new DeleteFile();
        df.deleteXml(args);
    }

    private void deleteXml(String[] args) {
        String filePath = "s1.xml";
        if (args != null && args.length > 0) {
            filePath = args[args.length - 1];
        }
        File file = new File(filePath);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder domParser = factory.newDocumentBuilder();
            Document document = domParser.parse(file);
            NodeList groupList = document.getElementsByTagName("Group");
            if (groupList != null && groupList.getLength() > 0) {
                int size = groupList.getLength();
                for (int k = 0; k < size; k++) {
                    Node node = groupList.item(k);
                    Element groupEle = (Element) node;
                    NodeList imageList = groupEle.getElementsByTagName("Image");
                    if (imageList != null && imageList.getLength() > 0) {
                        Element imageEle = (Element) imageList.item(0);
                        String fileName = imageEle.getAttribute("FileName");
                        if (!TextUtils.isEmpty(fileName)) {
                            fileName = fileName.trim();
                            File deleteFile = new File(fileName);
                            boolean isDelete = false;
                            if (deleteFile.exists()) {
                                isDelete = deleteFile.delete();
                                System.out.println("file:" + fileName
                                        + " isDelete:" + isDelete);
                            } else {
                                System.out.println(fileName + " not found!");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(System.in);
        System.out.println("按回车键退出！");
        String name = in.nextLine();
        System.exit(0);
    }

    private void deleteFile1(String[] args) {
        String filePath = "DupsRecordLogtest.txt";
        if (args != null && args.length > 0) {
            filePath = args[args.length - 1];
        }
        File file = new File(filePath);

        if (file.exists()) {
            String text = TextUtils.readFile(filePath);
            String[] lines = text.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith(LINE_HEAD)) {
                    String deletePath = line.substring(LINE_HEAD.length())
                            .trim();
                    File deleteFile = new File(deletePath);
                    boolean isDelete = false;
                    if (deleteFile.exists()) {
                        isDelete = deleteFile.delete();
                    }
                    System.out.println("file:" + deletePath + " isDelete:"
                            + isDelete);
                }
            }
        } else {
            System.out.println(filePath + " not found!");
        }
        Scanner in = new Scanner(System.in);
        System.out.println("按回车键退出！");
        String name = in.nextLine();
        System.exit(0);
    }
}
