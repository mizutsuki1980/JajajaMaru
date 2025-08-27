package com.example.jajajamaru

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class BackGround {
    var x = 300
    var y = 300
var ookisa = 500
    val iro = Paint()
//コメント修正
    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 155, 155, 150)

    }
    fun draw(canvas: Canvas){
        var itemList = mutableListOf<Int>(1,2,3,4,5,6,7)

        for (i in itemList) {
        }
            canvas.drawRect(shikakuRectXY(),iro) //自機の描画
    }

    fun shikakuRectXY(): Rect {
        val left = x  - ookisa / 2
        val right = x  + ookisa / 2
        val top = y  - ookisa / 2
        val bottom = y + ookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }
}