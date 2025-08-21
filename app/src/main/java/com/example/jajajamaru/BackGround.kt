package com.example.jajajamaru

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

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
        //なんかできないっぽい
        //canvas.drawBitmap(bitmap, 50.0F+(background.x.toFloat()), 200.0F+(background.y.toFloat()), null)
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa).toFloat(),iro) //自機の描画
    }
}