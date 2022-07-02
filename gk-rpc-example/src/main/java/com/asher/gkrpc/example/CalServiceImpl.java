package com.asher.gkrpc.example;

/**
 * @className: CalServiceImpl
 * @Description: TODO
 * @version: jdk1.8
 * @author: asher
 * @date: 2022/5/3 18:14
 */
public class CalServiceImpl implements CalService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int minus(int a, int b) {
        return a - b;
    }
}
