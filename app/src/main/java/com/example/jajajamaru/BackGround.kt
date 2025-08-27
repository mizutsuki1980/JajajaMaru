package com.example.jajajamaru

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class BackGround {
    var x = 100
    var y = 100
var ookisa = 500
    val iro = Paint()
//コメント修正
    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 155, 155, 150)

    }
    fun draw(canvas: Canvas){
        //なんかできないっぽい
        //canvas.drawBitmap(bitmap, 50.0F+(background.x.toFloat()), 200.0F+(background.y.toFloat()), null)
        //canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa).toFloat(),iro) //自機の描画
        var itemList = mutableListOf<Int>(1,2,3,4,5)

        for (i in itemList) {
            iro.color = argb(15*i, 155, 155, 150)
            canvas.drawRect(x+100*i.toFloat(),y.toFloat(),(x+200*i).toFloat(),(y+500).toFloat(),iro)
        }

        canvas.drawRect(x.toFloat(),y.toFloat(),(x+200).toFloat(),(y+500).toFloat(),iro)

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