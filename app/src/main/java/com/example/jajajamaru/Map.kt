package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class Map {
    val TYPE_ISHI = 1
    val TYPE_SORA = 0
    val MASU_SIZE = 32
    val listrow = listOf<Int>(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)
    val listcol = listOf<Int>(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30)

    fun draw(canvas: Canvas){
        val iro = Paint()
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 30, 255, 255)
        canvas.drawRect(100f,200f,200f,500f,iro)
    }


    fun drawMap(canvas: Canvas,row:Int,col:Int,masushurui:Int){
        val iro = Paint()
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 30, 30, 30)
        when (masushurui) {
            1 -> { iro.color = argb(255, 30, 30, 30)}
            0 -> { iro.color = argb(255, 155, 155, 250) }
         }
        canvas.drawRect(shikakuRectXY(100+MASU_SIZE*col,100+MASU_SIZE*row,MASU_SIZE),iro) //自機の描画
    }

    fun shikakuRectXY(x:Int,y:Int,ookisa:Int): Rect {
        val left = x  - ookisa / 2
        val right = x  + ookisa / 2
        val top = y  - ookisa / 2
        val bottom = y + ookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }



    fun masShurui(row:Int,col:Int): Int {
        if ((row > 0  && row < 4)&&(col > 3  && col < 8)) { return TYPE_ISHI}
        if ((row > 5  && row < 10)&&(col > 11  && col < 15)) { return TYPE_ISHI}
        if ((row > 17  && row < 20)&&(col > 15  && col < 20)) { return TYPE_ISHI}

            return TYPE_SORA
    }

    /*
            if (row > 0  || row < 2) {
            return TYPE_ISHI
        }else{
            return TYPE_SORA
        }
     */

    //   var m = 0
    // if (row == 10){m=1}
    // if (col == 2){m=1}
    // return m


}