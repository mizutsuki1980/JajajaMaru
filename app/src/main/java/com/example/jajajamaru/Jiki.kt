package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 200
    val iro = Paint()

    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
    }

    fun draw(canvas: Canvas){
        canvas.drawCircle(x.toFloat(),y.toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }
    fun jumpdraw(canvas: Canvas,jumpTakasa:Int){
        canvas.drawCircle(x.toFloat(),(y-jumpTakasa).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }

}