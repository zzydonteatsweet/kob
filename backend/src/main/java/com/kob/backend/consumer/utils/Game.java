package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    final private Integer rows;
    final private Integer cols;
    final private Integer inner_walls_count;
    final private int[][] g;
    final private static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    private final Player playerA, playerB ;
    private Integer nextStepA = null ;
    private Integer nextStepB = null ;
    private String status = "playing"; //  playing -> finished
    private String loser = "" ; //  A, B, ALL
    private ReentrantLock lock = new ReentrantLock() ;
    public void setNextStepA(Integer A) {
        lock.lock();
        try {
            System.out.println("A received");
            this.nextStepA = A ;
//            System.out.println(nextStepA);
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer B) {
        lock.lock();
        try {
            System.out.println("B Received");
            this.nextStepB = B ;
//            System.out.println(this.nextStepB);
        } finally {
            lock.unlock();
        }
    }

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA, rows-2, 1, new ArrayList<>()) ;
        playerB = new Player(idB, 1, cols-2, new ArrayList<>()) ;
    }

    public int[][] getG() {
        return g;
    }

    public Player getPlayerA() {
        return playerA ;
    }
    public Player getPlayerB() {
        return playerB ;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i ++ ) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0) {
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;
                    return true;
                }
            }
        }

        g[sx][sy] = 0;
        return false;
    }

    private boolean draw() {  // 画地图
        for (int i = 0; i < this.rows; i ++ ) {
            for (int j = 0; j < this.cols; j ++ ) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }
        for (int c = 0; c < this.cols; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i ++ ) {
            for (int j = 0; j < 1000; j ++ ) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1)
                    continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2)
                    continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i ++ ) {
            if (draw())
                break;
        }
    }

    private boolean nextStep() { //  等待两名玩家下一步操作
        try { //  如果后端接收到了很多的输入只取200ms最后一步操作
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0 ; i < 50 ; i ++) {
            try {
                Thread.sleep(200); //  等待输入的时间
                lock.lock() ;
                try {
                    if(this.nextStepA != null && this.nextStepB != null) {
                        playerA.getSteps().add(this.nextStepA) ;
                        playerB.getSteps().add(this.nextStepB) ;
                        return true ;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false ;

    }

    private void sendResult() {
        JSONObject resp = new JSONObject() ;
        resp.put("event", "result") ;
        resp.put("loser", loser) ;
        sendAllMessage(resp.toJSONString());
        save2DataBase();
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size() ;
        Cell cell = cellsA.get(n-1) ;
        if(g[cell.x][cell.y] == 1) {
            return false ;
        }

        for(int i = 0 ; i < n-1 ; i ++) {
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) return false ;
        }
        for(int i = 0 ; i < n-1 ; i ++) {
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) return false ;
        }
        return true ;
    }

    private void judge() {
        List<Cell> cellsA = playerA.getCells() ;
        List<Cell> cellsB = playerB.getCells() ;

        boolean validA = check_valid(cellsA, cellsB) ;
        boolean validB = check_valid(cellsB, cellsA) ;
        if(!validA || !validB) {
             status = "finished" ;

             if(!validA && !validB) loser = "all" ;
             else if(!validA) loser = "a" ;
             else loser = "b" ;
            System.out.println(loser);
        }
    }

    private void sendMove() {
        lock.lock() ;
        try {
            JSONObject resp = new JSONObject() ;
            resp.put("event", "move") ;
            resp.put("a_direction", nextStepA) ;
            resp.put("b_direction", nextStepB) ;
            nextStepA = nextStepB = null ;
            sendAllMessage(resp.toJSONString());
        } finally {
            lock.unlock();
        }

    }

    private String getMapString() {
        StringBuilder res = new StringBuilder() ;
        for(int i = 0 ; i < rows ; i ++) {
            for(int j = 0 ; j < cols ; j ++) {
                res.append(g[i][j]) ;
            }
        }
        return res.toString() ;
    }
    private void save2DataBase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        ) ;

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    @Override
    public void run() {
//        super.run();
        for(int i = 0 ; i < 1000 ; i ++) {
            if(nextStep()) {
                judge();
                if(status.equals("playing")) {
                    sendMove();
                }else {
                    sendResult();
                    break;
                }
            }else { //  时间结束没有操作的玩家输了
                status = "finished" ;
                System.out.println("Finished");
                lock.lock();
                try {
                    if(nextStepA == null && nextStepB == null) {
                        loser = "all" ;
                    }else if(nextStepA == null) {
                        loser = "a" ;
                    }else loser = "b" ;
                } finally {
                    lock.unlock();
                }
                sendResult();
                break ;
            }
        }
    }
}
