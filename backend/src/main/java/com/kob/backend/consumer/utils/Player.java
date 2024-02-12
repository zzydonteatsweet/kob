package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id ;
    private Integer sx, sy ;
    private List<Integer> steps ;

    private boolean check_tail_increasing(int step) { //  检查蛇身体会变长
        if(step <= 10 || step%3 == 1) return true ;
        else return false ;
    }

    public List<Cell> getCells() {
        List<Cell> res = new LinkedList<>();
        int x = sx, y = sy ;
        int []dx = {-1, 0, 1, 0};
        int []dy = {0, 1, 0, -1} ;
        int step = 0 ;
        res.add(new Cell(x, y)) ;
        for(int d : steps) {
            x += dx[d] ; y += dy[d] ;
            res.add(new Cell(x, y)) ;
            if(!check_tail_increasing(++ step)) {
                res.remove(0) ;
            }
        }
        return res ;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder() ;
        for(int d : steps) {
            res.append(d) ;
        }
        return res.toString() ;
    }

}
