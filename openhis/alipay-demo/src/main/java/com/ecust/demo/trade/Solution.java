package com.ecust.demo.trade;

import java.util.*;

class Solution {
    public static void main(String[] args) {
        List<Integer> list=new ArrayList<>();

        char[][] board = new char[][]
                {{'8', '3', '.', '.', '7', '.', '.', '.', '.'}
                        , {'6', '.', '.', '1', '9', '5', '.', '.', '.'}
                        , {'.', '9', '8', '.', '.', '.', '.', '6', '.'}
                        , {'8', '.', '.', '.', '6', '.', '.', '.', '3'}
                        , {'4', '.', '.', '8', '.', '3', '.', '.', '1'}
                        , {'7', '.', '.', '.', '2', '.', '.', '.', '6'}
                        , {'.', '6', '.', '.', '.', '.', '2', '8', '.'}
                        , {'.', '.', '.', '4', '1', '9', '.', '.', '5'}
                        , {'.', '.', '.', '.', '8', '.', '.', '7', '9'}};
        System.out.println(isValidSudoku(board));
    }

    public static boolean isValidSudoku(char[][] board) {
        Map<Character, Integer>[] row = new HashMap[9];//验证行
        Map<Character, Integer>[] column = new HashMap[9];//验证列
        Map<Character, Integer>[] block = new HashMap[9];//验证块
        for (int i = 0; i < 9; i++) {
            row[i] = new HashMap<Character, Integer>();
            column[i] = new HashMap<Character, Integer>();
            block[i] = new HashMap<Character, Integer>();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int box_index = (i / 3) * 3 + j / 3;
                    row[i].put(board[i][j], row[i].getOrDefault(board[i][j], 0) + 1);
                    column[j].put(board[i][j], column[j].getOrDefault(board[i][j], 0) + 1);
                    block[box_index].put(board[i][j], block[box_index].getOrDefault(board[i][j], 0) + 1);
                    System.out.println("row:" + row[i]);
                    System.out.println("column:" + column[j]);
                    System.out.println("block:" + block[box_index]);
                    if ((row[i].get(board[i][j]) > 1) || (column[j].get(board[i][j]) > 1) || (block[box_index].get(board[i][j]) > 1)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}