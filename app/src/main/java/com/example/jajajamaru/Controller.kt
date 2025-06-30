package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class Controller {


    fun draw(canvas: Canvas){
        val iroCont = Paint()
        iroCont.style = Paint.Style.FILL
        iroCont.color = argb(100, 255, 255, 150)

        canvas.drawRect(shikakuRectXYSub(75.toInt() ,1000.toInt(),150), iroCont)   //

        canvas.drawRect(shikakuRectXYSub(75+150+30+180.toInt() ,1000.toInt(),150), iroCont)   //

    }


    fun shikakuRectXYSub(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        val left = xxx  - ooookisa / 2
        val right = xxx  + ooookisa / 2
        val top = yyy  - ooookisa / 2
        val bottom = yyy + ooookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }

}