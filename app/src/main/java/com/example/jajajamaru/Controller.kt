package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class Controller {


    fun draw(canvas: Canvas){
        val buttonIro = Paint()
        buttonIro.style = Paint.Style.FILL
        buttonIro.color = argb(200, 0, 0, 150)

        val buttonPushIro = Paint()
        buttonPushIro.style = Paint.Style.FILL
        buttonPushIro.color = argb(100, 0, 0, 150)
        //plphaを下げると暗くなる


        val hyoujiIro =  Paint()
        hyoujiIro.style = Paint.Style.FILL
        hyoujiIro.color = Color.BLUE
        hyoujiIro.textSize = 100.toFloat()

        canvas.drawRect(shikakuRectButton(30.toInt() ,920.toInt(),150), buttonPushIro)   //
        canvas.drawText("←",(50).toFloat(),(1035).toFloat(),hyoujiIro)

        canvas.drawRect(shikakuRectButton(30+170.toInt() ,920.toInt(),150), buttonIro)   //

        canvas.drawRect(shikakuRectButton(30+170+170+170.toInt() ,920.toInt(),150), buttonIro)   //
        canvas.drawText("→",(570).toFloat(),(1035).toFloat(),hyoujiIro)
    }


    fun shikakuRectButton(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        val left = xxx
        val right = xxx  + ooookisa
        val top = yyy
        val bottom = yyy + ooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }

    //中心をｘｙとしてookisaの四角を描画する
    fun shikakuRectXYSub(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        val left = xxx  - ooookisa / 2
        val right = xxx  + ooookisa / 2
        val top = yyy  - ooookisa / 2
        val bottom = yyy + ooookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }

}