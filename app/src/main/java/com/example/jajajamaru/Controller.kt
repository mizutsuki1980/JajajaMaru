package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect

class Controller {
    var houkou = "nashi"
    var isFirstJump :Boolean
    var jumpFrame = 1
    var jumpTakasa = 0

    init{
     isFirstJump = false
    }

    fun draw(canvas: Canvas,clickX:Int,clickY:Int,clickState:String){
        houkou = "nashi"
        val hidariButtonIro = Paint()
        hidariButtonIro.style = Paint.Style.FILL
        hidariButtonIro.color = argb(200, 0, 0, 150)

        val migiButtonIro = Paint()
        migiButtonIro.style = Paint.Style.FILL
        migiButtonIro.color = argb(200, 0, 0, 150)

        val jumpButtonIro = Paint()
        jumpButtonIro.style = Paint.Style.FILL
        jumpButtonIro.color = argb(200, 0, 0, 150)

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

        //xが50～150の間で
        //ｙが920～1070の間で
        //onTouchEventが押してる状態だったら
        //色を変える

        if(clickX > 50 && clickX <150){
            if(clickY > 920 && clickY <1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "hidari"
                    hidariButtonIro.color = argb(100, 100, 100, 200)
                }
            }
        }


        if(clickX > (30+170+170+170) && clickX <(30+170+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    houkou = "migi"
                    migiButtonIro.color = argb(100, 100, 100, 200)
                }
            }
        }

        if(clickX > (170) && clickX <(30+170+320)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    isFirstJump = true
                    jumpButtonIro.color = argb(100, 100, 100, 200)
                }else{
                    isFirstJump = false
                }
                //なるほど、tureになったらAction_UP？になってもtrueになりつづけてるのか

            }
        }
        //ここで一回だけジャンプしてるかチェックしたらいいのでは？
        //んーなんかここの条件付けが失敗している
        if (isFirstJump) { jumpFrame -=5 }
            //          if(jumpFrame == 1){ jumpFrame = 100}
   //     }
    //    jumpFrame -=5
      //  if(jumpFrame>=0) {
        //    isFirstJump = false
          //  jumpFrame = 1
//        }

            canvas.drawRect(shikakuRectButton(30.toInt() ,920.toInt(),150), hidariButtonIro)   //
        canvas.drawText("←",(50).toFloat(),(1035).toFloat(),hyoujiIro)
        //canvas.drawRect(shikakuRectButton(30+170.toInt() ,920.toInt(),150), buttonIro)   //

        canvas.drawRect(shikakuYokonagaRectButton(30+170.toInt() ,920.toInt(),320,150
        ), jumpButtonIro)
        canvas.drawText("Jump!",(50+170).toFloat(),(1035).toFloat(),hyoujiIro)


        canvas.drawRect(shikakuRectButton(30+170+170+170.toInt() ,920.toInt(),150), migiButtonIro)   //
        canvas.drawText("→",(570).toFloat(),(1035).toFloat(),hyoujiIro)
    }

    fun shikakuYokonagaRectButton(xxx:Int,yyy:Int,yokoooookisa:Int,tateooookisa:Int): Rect {
        val left = xxx
        val right = xxx  + yokoooookisa
        val top = yyy
        val bottom = yyy + tateooookisa
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