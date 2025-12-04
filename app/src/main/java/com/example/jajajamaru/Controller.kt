package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect
class Controller {
    var hidari = Button(30,820,150,400,Paint(),Rect(0, 0, 0,0))
    var migi = Button(540,820,150,400,Paint(),Rect(0, 0, 0,0))
    var jump = Button(200,820,320,400,Paint(),Rect(0, 0, 0,0))
    val hidariRect = buttonMakeXOokisaYOokisa(hidari.x.toInt() ,hidari.y.toInt(),hidari.xOokisa,hidari.yOokisa)
    val migiRect = buttonMakeXOokisaYOokisa(migi.x.toInt() ,migi.y.toInt(),migi.xOokisa,migi.yOokisa)
    val jumpRect = buttonMakeXOokisaYOokisa(jump.x.toInt() ,jump.y.toInt(),jump.xOokisa,jump.yOokisa)
    var houkou = "nashi"

    var pushCheck = "nashi"

    var pushedButton = "nashi"
    var pushedJumpButton = false

    //ここにボタンが増えただけ色の設定も置かなきゃいけないのかー、面倒だなぁ
    val buttonPushIro = Paint()
    val buttonNormalIro = Paint()
    var hyoujiIro =  Paint()

    var clickX = 0
    var clickY = 0

    init{
        //最初の所で設定できいないのでここでRectを設定している
        hidari.rect =  hidariRect
        migi.rect =  migiRect
        jump.rect =  jumpRect

        //ボタンの初期化、文字の色設定
        buttonIroSettei()
        mozinoIroSettei()
    }


    private fun mozinoIroSettei(){
        hyoujiIro.style = Paint.Style.FILL
        hyoujiIro.color = Color.BLUE
        hyoujiIro.textSize = 100.toFloat()
    }

    private fun buttonIroSettei() {
        buttonPushIro.style = Paint.Style.FILL
        buttonPushIro.color = argb(100, 100, 100, 150)
        buttonNormalIro.style = Paint.Style.FILL
        buttonNormalIro.color = argb(200, 0, 0, 150)
        jump.paint = buttonNormalIro
        migi.paint = buttonNormalIro
        hidari.paint = buttonNormalIro
    }

    fun pushButtonCheck() {
        if(houkou == "hidari"){ hidari.paint = buttonPushIro }
        if(houkou == "migi"){ migi.paint = buttonPushIro }
        if(pushedJumpButton == true){ jump.paint = buttonPushIro }
    }
    //ここでどんな処理をしているんだろうか？

    fun clickPointCheckButtonKai():String{
        var returnString = "nashi"
        if(clickX > hidari.x && clickX <(hidari.x+hidari.xOokisa)){
            if(clickY > hidari.y && clickY <(hidari.y+hidari.yOokisa)) {
                returnString = "hidari"
            }
        }
        if(clickX > (migi.x) && clickX <(migi.x+migi.xOokisa)){
            if(clickY > migi.y && clickY < (migi.y + migi.yOokisa)) {
                returnString = "migi"
            }
        }
        if(clickX > (jump.x) && clickX <(jump.x + jump.xOokisa)){
            if(clickY > jump.y && clickY < (jump.y + jump.yOokisa)) {
                returnString = "jump"
            }
        }

        if(returnString=="migi"){pushedButton = "migi"}
        if(returnString=="hidari"){pushedButton = "hidari"}
        if(returnString=="jump"){pushedJumpButton = true}

        return returnString
    }


    fun clickPointCheck(clickX:Int,clickY:Int,clickState:String){
        houkou = "nashi"
        if (clickState == "ACTION_UP"){houkou = "nashi"}

        if(clickX > hidari.x && clickX <(hidari.x+hidari.xOokisa)){
            if(clickY > hidari.y && clickY <(hidari.y+hidari.yOokisa)) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "hidari"
                }
            }
        }
        if(clickX > (migi.x) && clickX <(migi.x+migi.xOokisa)){
            if(clickY > migi.y && clickY < (migi.y + migi.yOokisa)) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "migi"
                }
            }
        }
        if(clickX > (jump.x) && clickX <(jump.x + jump.xOokisa)){
            if(clickY > jump.y && clickY < (jump.y + jump.yOokisa)) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "jump"
                }
            }
        }
    }

    fun draw(canvas: Canvas){
        buttonIroSettei()
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
}