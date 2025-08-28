package com.example.jajajamaru

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class BackGround {
    var x = 100
    var y = 500
    var ookisa = 100
    val iro = Paint()
//コメント修正
    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 155, 155, 150)

    }
    fun draw(canvas: Canvas){
//        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        var itemList = mutableListOf<Int>(1,2,3,4,5,6,7)

        for (i in itemList) {
            iro.color = argb((155-(i*20)), 155, 155, 150)
            canvas.drawRect(shikakuRectKakezanXY(i,0),iro) //自機の描画
        }
        for (i in itemList) {
            iro.color = argb(155, (155-(i*20)), 155, 150)
            canvas.drawRect(shikakuRectKakezanXY(i,1),iro) //自機の描画
        }

        for (i in itemList) {
            iro.color = argb(155, 155, (155-(i*20)), 150)
            canvas.drawRect(shikakuRectKakezanXY(i,2),iro) //自機の描画
        }
        for (i in itemList) {
            iro.color = argb(155, 155, 155, (155-(i*20)))
            canvas.drawRect(shikakuRectKakezanXY(i,3),iro) //自機の描画
        }


    }

    fun shikakuRectKakezanXY(kakezan:Int,gyoukan:Int): Rect {
        val left = x+ookisa*kakezan  - ookisa / 2
        val right = x+ookisa*kakezan  + ookisa / 2
        val top = y+ookisa*gyoukan  - ookisa / 2
        val bottom = y+ookisa*gyoukan + ookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }
    fun shikakuRectXY(): Rect {
        val left = x  - ookisa / 2
        val right = x  + ookisa / 2
        val top = y  - ookisa / 2
        val bottom = y + ookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }

    fun migiIdo(){
        x += 100
    }

    fun hidariIdo(){
        x -= 100
    }
    fun ueIdo(){
        y-= 100
    }
    fun shitaIdo(){
        y+=100
    }


}