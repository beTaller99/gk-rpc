package com.asher.gkrpc.example;

/**
 * @className: CalServiceImpl
 * @Description: TODO
 * @version: openjdk-17.0.2
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