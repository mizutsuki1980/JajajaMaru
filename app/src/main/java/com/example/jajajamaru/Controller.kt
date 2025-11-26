package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect


//そもそも、なんかまとめらそうだよなぁ、、、、。データクラス？かな？
class Controller {

    var hidari = Button(30,920,150,150,Paint(),Rect(0, 0, 0,0))
    var migi = Button(30+170+170+170,920,150,150,Paint(),Rect(0, 0, 0,0))
    var jump = Button(30+170,920,300,150,Paint(),Rect(0, 0, 0,0))

    val hidariRect = buttonMakeXOokisaYOokisa(hidari.x.toInt() ,hidari.y.toInt(),hidari.xOokisa,hidari.yOokisa)
    val migiRect = buttonMakeXOokisaYOokisa(migi.x.toInt() ,migi.y.toInt(),migi.xOokisa,migi.yOokisa)
    val jumpRect = buttonMakeXOokisaYOokisa(jump.x.toInt() ,jump.y.toInt(),jump.xOokisa,jump.yOokisa)

    var houkou = "nashi"

    //ここにボタンが増えただけ色の設定も置かなきゃいけないのかー、面倒だなぁ
    var hidariButtonIro = Paint()
    var migiButtonIro = Paint()
    var jumpButtonIro = Paint()
    var buttonIro = Paint()
    var buttonPushIro = Paint()
    var hyoujiIro =  Paint()



    init{
        buttonSyokika()
        buttonIroSettei()

        hidari.rect =  hidariRect
        migi.rect =  migiRect
        jump.rect =  jumpRect

    }


    private fun buttonIroSettei() {

        jump.paint.style = Paint.Style.FILL
        jump.paint.color = argb(200, 0, 0, 150)

        migi.paint.style = Paint.Style.FILL
        migi.paint.color = argb(200, 0, 0, 150)

        hidari.paint.style = Paint.Style.FILL
        hidari.paint.color = argb(200, 0, 0, 150)

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



    fun pushButtonCheck() {
        if(houkou == "hidari"){
            hidariButtonIro.color = argb(100, 100, 100, 200)
            hidari.paint.color = argb(100, 100, 100, 200)
        }
        if(houkou == "migi"){
            migiButtonIro.color = argb(100, 100, 100, 200)
            migi.paint.color = argb(100, 100, 100, 200)
        }
        if(houkou == "jump"){
            jumpButtonIro.color = argb(100, 100, 100, 200)
            jump.paint.color = argb(100, 100, 100, 200)
        }
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

    fun draw(canvas: Canvas){
        buttonSyokika()
        pushButtonCheck()
        canvas.drawRect( hidariRect, hidari.paint)   //
        canvas.drawText("←",(hidari.x+20).toFloat(),(hidari.y+115).toFloat(),hyoujiIro)
        canvas.drawRect( migiRect, migi.paint)   //
        canvas.drawText("→",(migi.x+20).toFloat(),(migi.y+115).toFloat(),hyoujiIro)
        canvas.drawRect( jumpRect, jump.paint)   //
        canvas.drawText("Jump!",(jump.x+20).toFloat(),(jump.y+115).toFloat(),hyoujiIro)
    }


    fun buttonMakeXOokisaYOokisa(xxx:Int,yyy:Int,xOkisa:Int,yOokisa:Int): Rect {
        val left = xxx
        val right = xxx  + xOkisa
        val top = yyy
        val bottom = yyy + yOokisa
        val m = Rect(left, top, right,bottom)
        return m
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