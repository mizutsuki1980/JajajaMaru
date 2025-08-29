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

    //ボタンっていうクラスを作る？
    //三つだけだし、決め打ちでいいかー

    val buttonHidariX = 30
    val buttonHidariY = 920
    val buttonHidariOokisa = 150

    val buttonMigiX = 30+170+170+170
    val buttonMigiY = 920
    val buttonMigiOokisa = 150

    val buttonJumpX = 30+170
    val buttonJumpY = 920+170
    val buttonJumpOokisa = 150

    val buttonHidariRect = buttonRect(buttonHidariX.toInt() ,buttonHidariY.toInt(),buttonHidariOokisa,1)
    val buttonMigiRect = buttonRect(buttonMigiX.toInt() ,buttonMigiY.toInt(),buttonMigiOokisa,1)
    val buttonJumpRect = buttonRect(buttonJumpX.toInt() ,buttonJumpY.toInt(),buttonJumpOokisa,2)


    fun pushButtonCheck() {
        if(houkou == "hidari"){ hidariButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "migi"){migiButtonIro.color = argb(100, 100, 100, 200) }
        if(houkou == "jump"){jumpButtonIro.color = argb(100, 100, 100, 200) }
    }

    fun draw(canvas: Canvas){
        buttonSyokika()
        pushButtonCheck()
        canvas.drawRect( buttonHidariRect, hidariButtonIro)   //
        canvas.drawText("←",(buttonHidariX+20).toFloat(),(buttonHidariY+115).toFloat(),hyoujiIro)
        canvas.drawRect( buttonMigiRect, migiButtonIro)   //
        canvas.drawText("→",(buttonMigiX+20).toFloat(),(buttonMigiY+115).toFloat(),hyoujiIro)
        canvas.drawRect( buttonJumpRect, jumpButtonIro)   //
        canvas.drawText("Jump!",(buttonJumpX+20).toFloat(),(buttonJumpY+115).toFloat(),hyoujiIro)
    }


    fun buttonRect(xxx:Int,yyy:Int,ooookisa:Int,bairitu:Int): Rect {
        val left = xxx
        val right = xxx*bairitu  + ooookisa
        val top = yyy
        val bottom = yyy + ooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }

}