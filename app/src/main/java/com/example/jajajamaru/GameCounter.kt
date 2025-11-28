package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class GameCounter {
    var isClear = false
    var time = 0
    fun clearCheck(){

    }

    fun draw(canvas: Canvas,frame:Int,jiki:Jiki,map:Map) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        val iroMoji = Paint()
        iroMoji.style = Paint.Style.FILL
        iroMoji.color = argb(255, 255, 255, 255)
        iroMoji.textSize = 30.toFloat()
        if(isClear){
            iroMoji.color = argb(255, 255, 100, 100)
            goalHyouzi(canvas)
        }

        canvas.drawText("Time",(210).toFloat(),(24).toFloat(),iroMoji)
        canvas.drawText(zerohuyasu(time.toString()),(285).toFloat(),(24).toFloat(),iroMoji)



    }

    fun goalHyouzi(canvas: Canvas) {
        val iroMoji = Paint()
        iroMoji.style = Paint.Style.FILL
        iroMoji.color = argb(255, 255, 255, 255)
        iroMoji.textSize = 62.toFloat()
        canvas.drawText("Game Claer", (220).toFloat(), (400).toFloat(), iroMoji)
    }

    private fun zerohuyasu(text:String):String{
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