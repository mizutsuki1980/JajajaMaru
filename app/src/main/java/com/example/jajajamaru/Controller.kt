package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect
class Controller {
    //ボタンの位置を離してみたら、押しっぱなし問題は解決するのだろうか？
    //やってみよう

    //ボタンの位置を決めるのはここで調節できる
    var hidari = Button(30,820,120,200,Paint(),Rect(0, 0, 0,0))
    var migi = Button(570,820,120,200,Paint(),Rect(0, 0, 0,0))
    var jump = Button(200,1020,320,200,Paint(),Rect(0, 0, 0,0))

    //なるほど、まず条件を確認
    //①右を押しっぱなしにする
    //②ジャンプをちょっとだけ押しっぱなしにする。
    //③ジャンプだけはなして、まった指をうごかさないようにする。
    //たぶんこれで再現する。①で押した右をちょっとでも動かすと反応して修正しているように見える。

    val hidariRect = buttonMakeXOokisaYOokisa(hidari.x.toInt() ,hidari.y.toInt(),hidari.xOokisa,hidari.yOokisa)
    val migiRect = buttonMakeXOokisaYOokisa(migi.x.toInt() ,migi.y.toInt(),migi.xOokisa,migi.yOokisa)
    val jumpRect = buttonMakeXOokisaYOokisa(jump.x.toInt() ,jump.y.toInt(),jump.xOokisa,jump.yOokisa)

    var pushCheck = "nashi"
    var pushedSayuButton = "nashi"
    var pushedJumpButton = false
    //コメント修正

    //ここにボタンが増えただけ色の設定も置かなきゃいけないのかー、面倒だなぁ
    val buttonPushIro = Paint()
    val buttonNormalIro = Paint()
    var hyoujiIro =  Paint()

    var clickX = 0
    var clickY = 0

    var nikaimeJump = false
    var zenkaiPushJump = false

    //二段ジャンプの回数を指定できる。
    var nidanJunpKiasuuDefault = 5
    var nidanJumpKaisuu = nidanJunpKiasuuDefault

    fun jumpButtonOsiTuduketeirukaCheck(jiki:Jiki) {
        //直らないものとして諦めるとする

        //①下降中に「一度だけ」もう一回ジャンプする
        //②２回目以降は無視する。

        if(jiki.isJump){
            if(zenkaiPushJump){
            }else {
                if (pushedJumpButton) {
                    if(nidanJumpKaisuu>=1){
                        nikaimeJump = true
                        nidanJumpKaisuu -= 1
                    }
                }
            }

        }else{
            nikaimeJump = false
            nidanJumpKaisuu = nidanJunpKiasuuDefault
        }

        zenkaiPushJump = pushedJumpButton



    }



    init{
        //最初の所で設定できいないのでここでRectを設定している
        hidari.rect =  hidariRect
        migi.rect =  migiRect
        jump.rect =  jumpRect

        //ボタンの初期化、文字の色設定
        buttonIroSettei()
        mozinoIroSettei()
    }


    fun syokika(){
        pushCheck = "nashi"
        pushedSayuButton = "nashi"
        pushedJumpButton = false

        clickX = 0
        clickY = 0

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
        if(pushedSayuButton == "hidari"){ hidari.paint = buttonPushIro }
        if(pushedSayuButton == "migi"){ migi.paint = buttonPushIro }
        if(pushedJumpButton == true){ jump.paint = buttonPushIro }
    }

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

        if(returnString=="migi"){pushedSayuButton = "migi"}
        if(returnString=="hidari"){pushedSayuButton = "hidari"}
        if(returnString=="jump"){pushedJumpButton = true}

        return returnString
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