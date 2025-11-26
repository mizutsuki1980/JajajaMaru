package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect


//そもそも、なんかまとめらそうだよなぁ、、、、。データクラス？かな？
class Controller {

    val hidari = Button(30,920,150,150,Paint())
    val migi = Button(30+170+170+170,920,150,150,Paint())
    val jump = Button(30+170,920,300,150,Paint())

    var houkou = "nashi"

    //ここにボタンが増えただけ色の設定も置かなきゃいけないのかー、面倒だなぁ
    var hidariButtonIro = Paint()
    var migiButtonIro = Paint()
    var jumpButtonIro = Paint()
    var buttonIro = Paint()
    var buttonPushIro = Paint()
    var hyoujiIro =  Paint()

    val buttonHidariX = 30
    val buttonHidariY = 920
    val buttonHidariOokisa = 150

    val buttonMigiX = 30+170+170+170
    val buttonMigiY = 920
    val buttonMigiOokisa = 150

    val buttonJumpX = 30+170
    val buttonJumpY = 920
    val buttonJumpOokisa = 300

    val buttonHidariRect = buttonMake(hidari.x.toInt() ,hidari.y.toInt(),buttonHidariOokisa)
    val buttonMigiRect = buttonMake(buttonMigiX.toInt() ,buttonMigiY.toInt(),buttonMigiOokisa)
    val buttonJumpRect = buttonMake(buttonJumpX.toInt() ,buttonJumpY.toInt(),buttonJumpOokisa)


    init{
        buttonSyokika()
        buttonIroSettei()
    }


    private fun buttonIroSettei() {
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


    private fun buttonSyokika(){
        hidariButtonIro.style = Paint.Style.FILL
        hidariButtonIro.color = argb(200, 0, 0, 150)

        migiButtonIro.style = Paint.Style.FILL
        migiButtonIro.color = argb(200, 0, 0, 150)

        jumpButtonIro.style = Paint.Style.FILL
        jumpButtonIro.color = argb(200, 0, 0, 150)
    }


    fun clickPointCheck(clickX:Int,clickY:Int,clickState:String){
        houkou = "nashi"
         if (clickState == "ACTION_UP"){houkou = "nashi"}

        if(clickX > 50 && clickX <150){
            if(clickY > 920 && clickY <1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "hidari"
                }
            }
        }
        if(clickX > (30+170+170+170) && clickX <(30+170+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "migi"
                }
            }
        }
        if(clickX > (30+170) && clickX <(30+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "jump"
                }
            }
        }
    }


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



    fun buttonMake(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        //なんだbairituって、、、倍率？
        //よくわかんねぇもんで大きくすんなよ

        //大きさを増やすと縦横等倍に増えてくのか、、、使えねぇなぁ
        val left = xxx
        val right = xxx  + ooookisa
        val top = yyy
        val bottom = yyy + ooookisa
        val m = Rect(left, top, right,bottom)
        return m
    }

}