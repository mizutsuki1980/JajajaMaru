package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class Controller {

    var houkou = "nashi"
    var isJumpButton = false

    //ここにボタンが増えただけ色の設定も置かなきゃいけないのかー、面倒だなぁ
    var hidariButtonIro = Paint()
    var migiButtonIro = Paint()
    var ueButtonIro = Paint()
    var shitaButtonIro = Paint()
    var jumpButtonIro = Paint()
    var buttonIro = Paint()
    var buttonPushIro = Paint()
    var hyoujiIro =  Paint()

    init{
        buttonSyokika()

        jumpButtonIro.style = Paint.Style.FILL
        jumpButtonIro.color = argb(200, 0, 0, 150)

        buttonIro.style = Paint.Style.FILL
        buttonIro.color = argb(200, 0, 0, 150)

        buttonPushIro.style = Paint.Style.FILL
        buttonPushIro.color = argb(100, 0, 0, 150)


        hyoujiIro.style = Paint.Style.FILL
        hyoujiIro.color = Color.BLUE
        hyoujiIro.textSize = 100.toFloat()

    }
    fun buttonSyokika(){
        hidariButtonIro.style = Paint.Style.FILL
        hidariButtonIro.color = argb(200, 0, 0, 150)

        migiButtonIro.style = Paint.Style.FILL
        migiButtonIro.color = argb(200, 0, 0, 150)

        ueButtonIro.style = Paint.Style.FILL
        ueButtonIro.color = argb(200, 0, 0, 150)

        shitaButtonIro.style = Paint.Style.FILL
        shitaButtonIro.color = argb(200, 0, 0, 150)

        jumpButtonIro.style = Paint.Style.FILL
        jumpButtonIro.color = argb(200, 0, 0, 150)


    }


    fun draw(canvas: Canvas,clickX:Int,clickY:Int,clickState:String){
        buttonSyokika()
        if(houkou == "hidari"){ hidariButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "migi"){migiButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "ue"){ueButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "shita"){shitaButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "jump"){jumpButtonIro.color = argb(100, 100, 100, 200) }

        //ボタンの追加・修正方法、まず範囲を決める。ClickPointCheckでどこを触ったかを調べる。範囲内なら色を変える（押したことにする）
        canvas.drawRect(shikakuRectButton(30.toInt() ,920.toInt(),150), hidariButtonIro)   //
        canvas.drawText("←",(50).toFloat(),(1035).toFloat(),hyoujiIro)

        canvas.drawRect(shikakuRectButton(30+170.toInt() ,920.toInt(),150), ueButtonIro)   //
        canvas.drawText("↑",(50+170).toFloat(),(1035).toFloat(),hyoujiIro)

        canvas.drawRect(shikakuRectButton(30+170+170.toInt() ,920.toInt(),150), shitaButtonIro)   //
        canvas.drawText("↓",(50+170+170).toFloat(),(1035).toFloat(),hyoujiIro)


        canvas.drawRect(shikakuRectButton(30+170+170+170.toInt() ,920.toInt(),150), migiButtonIro)   //
        canvas.drawText("→",(570).toFloat(),(1035).toFloat(),hyoujiIro)

        //jumpボタン
        canvas.drawRect(shikakuRectButtonYokonaga(30+170.toInt() ,920+170.toInt(),150,2), jumpButtonIro)   //
        canvas.drawText("Jump!",(50+170).toFloat(),(1035+170).toFloat(),hyoujiIro)

    }

    fun shikakuYokonagaRectButton(xxx:Int,yyy:Int,yokoooookisa:Int,tateooookisa:Int): Rect {
        val left = xxx
        val right = xxx  + yokoooookisa
        val top = yyy
        val bottom = yyy + tateooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }
    fun shikakuRectButtonYokonaga(xxx:Int,yyy:Int,ooookisa:Int,bairitu:Int): Rect {
        val left = xxx
        val right = xxx*bairitu  + ooookisa
        val top = yyy
        val bottom = yyy + ooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }

    fun shikakuRectButton(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        val left = xxx
        val right = xxx  + ooookisa
        val top = yyy
        val bottom = yyy + ooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }

}