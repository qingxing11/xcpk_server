package com.wt.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wt.util.io.FileTool;

public class WordFilter {

//    public static final String[] filter={
//
//    };
//
//    public static final String[] filter2={
//            "台独","藏独","法轮功","江泽民",
//    };
//
//    /**
//     * 判断昵称是否合法
//     * @param user_nick
//     * @return
//     */
//    public static boolean contains(String user_nick) {
//        try {
//            for (int i = 0; i < filter.length; i++) {
//                if(user_nick.contains(filter[i])){
//                    return true;
//                }
//            }
//            for (int i = 0; i < filter2.length; i++) {
//                if(user_nick.contains(filter2[i])){
//                    return true;
//                }
//            }
//            return false;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//
//    }


//
//    public static boolean chkFilterWord(String strChk)
//    {
//        for (String word : ServerCache.arrayList_filterword)
//        {
//            if (strChk.lastIndexOf(word) != -1)
//            {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    public static String chkFilterWordAnReplace(String strChk, String rep)
//    {
//        String word = null;
//
//        for (String tmp: ServerCache.arrayList_filterword)
//        {
//            if (strChk.contains(tmp))
//            {
//                word = tmp;
//                break;
//            }
//        }
//
//        return word == null ? strChk : strChk.replaceAll(word, rep);
//    }

    private static class TrieNode
    {

        private  Map<Character, TrieNode> node = new HashMap<>();
        private  boolean end = false;

        void addNode(Character key, TrieNode node)
        {
            this.node.put(key, node);
        }

        TrieNode getSubNode(Character key)
        {
            return this.node.get(key);
        }

        public void setEnd(boolean end)
        {
            this.end = end;
        }

        public boolean isEnd()
        {
            return this.end;
        }

    }

    private static TrieNode root = new TrieNode();

    private static void addWord(String word)
    {
        TrieNode tmp = root;
        for (int i = 0; i < word.length(); i++)
        {
            char c = word.charAt(i);
            if (tmp.getSubNode(c) == null)
            {
                tmp.addNode(c, new TrieNode());
            }
            tmp = tmp.getSubNode(c);

            if (i == word.length() - 1)
            {
                tmp.setEnd(true);
            }
        }
    }

    public static void InitFilter(String filePath)
    {
        ArrayList<String> arrayListWords = FileTool.readFileByLine(filePath);
        if (arrayListWords == null)
        {
            //TODO 配置文件加载错误
            return;
        }

        for (String word : arrayListWords)
        {
            addWord(word);
        }
    }

    public static boolean ChkFilter(String word)
    {
        if (word == null) return false;

        TrieNode tmp = root;
        int begin = 0;
        int position = 0;

        while(position < word.length())
        {
            Character c = word.charAt(position);
            tmp = tmp.getSubNode(c);

            if (tmp == null)
            {
                begin++;
                position = begin;
                tmp = root;
            }
            else if (tmp.isEnd())
            {
                return true;
            }
            else
            {
                position++;
            }
        }

        return false;
    }

    public static String FilterWord(String word, String req)
    {
        if (word == null || req == null) return word;
        if (word.equals(req)) return word;

        StringBuilder sb = new StringBuilder();
        TrieNode tmp = root;
        int begin = 0;
        int position = 0;

        while (position < word.length())
        {
            Character c = word.charAt(position);
            tmp = tmp.getSubNode(c);

            if (tmp == null)
            {
                sb.append(word.charAt(begin));
                begin++;
                position = begin;
                tmp = root;
            }
            else if (tmp.isEnd())
            {
                sb.append(req);
                position++;
                begin = position;
                tmp = root;
            }
            else
            {
                position++;
            }
        }

        return sb.append(word.substring(begin)).toString();

    }
}
