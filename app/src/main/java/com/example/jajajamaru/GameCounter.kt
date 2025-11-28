package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class GameCounter {

    fun draw(canvas: Canvas,frame:Int,jiki:Jiki,map:Map) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        val iroMoji = Paint()
        iroMoji.style = Paint.Style.FILL
        iroMoji.color = argb(255, 255, 255, 255)
        iroMoji.textSize = 30.toFloat()

        canvas.drawText(zerohuyasu(frame.toString()),(9+200).toFloat(),(44).toFloat(),iroMoji)
        canvas.drawText("time",(20+200).toFloat(),(19).toFloat(),iroMoji)

    }





    fun zerohuyasu(text:String):String{
        val ketasuu = 8 //今回は８桁に固定
        val length = text.length
        var newtext = ""
        for (a in 0..<(ketasuu-length)) {
            newtext = "0" + newtext
        }
        newtext = newtext + text
        return newtext
    }

}