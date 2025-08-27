package com.example.jajajamaru

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var frame = 0
    var clickState = "nashi"
    val initialJikiX = 360 //初期位置
    val initialJikiY = 500 //初期位置

    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置

    var jiki =Jiki(initialJikiX, initialJikiY)
    var controller = Controller()
    var background = BackGround()



    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }


    fun migiIdo(){
        jiki.migiIdo()
        background.x  += 15
    }
    fun hidariIdo(){
        jiki.hidariIdo()
        background.x  -= 15
    }
    fun ueIdo(){
        jiki.ueIdo()
        background.y  -= 15
    }
    fun shitaIdo(){
        jiki.shitaIdo()
        background.y  += 15
    }




    fun clickPointCheck(){
    // 押しっぱなししにして、右から左とかボタンが移った場合に、右ボタンのままになってしまう問題
    //なるほど"ACTION_DOWN"が続いてるときは、クリックｘ、ｙは動いてないのかな
        // いや、違うか、左を押しっぱなしにしても、上とか下はできる
        //なんか「引きずる」みたいなのがあるんじゃないかな"ACTION_DOWN"みたいなので。onTouchEventで。

        //        if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
        //     この部分だけとってみたけどダメだった。

        //if (clickState == "ACTION_UP"){controller.houkou = "nashi"}
        //アクションアップになったら方向をなしにした。気休め程度

        //上下、左右がとくに機敏に反応しないとアクションゲームではだめだろう。
        //右おしっぱですぐ左おして右、みたいな。

        //最初にリセット


        controller.houkou = "nashi"

        if (clickState == "ACTION_UP"){controller.houkou = "nashi"}

        if(clickX > 50 && clickX <150){
            if(clickY > 920 && clickY <1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "hidari"
                }
            }
        }
        if(clickX > (30+170) && clickX <(30+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "ue"
                }
            }
        }
        if(clickX > (30+170+170) && clickX <(30+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "shita"
                }
            }
        }
        if(clickX > (30+170+170+170) && clickX <(30+170+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "migi"
                }
            }
        }




    }

    fun tsugiNoSyori() {

        clickPointCheck()
        when (controller.houkou) {
            "migi" -> {migiIdo()}
            "hidari" -> {hidariIdo()}
            "ue" -> {ueIdo()}
            "shita" -> {shitaIdo()}
        }
        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        //resourcesは移せない、よってこのまま↓↓↓↓
        //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tosu, BitmapFactory.Options())
        //canvas.drawBitmap(bitmap, 100.0F+(background.x.toFloat()), 100.0F+(background.y.toFloat()), null)
        //え、これって動かせないの？↑↑↑↑　drawBitmapが赤線になってしまう

        background.draw(canvas)
        jiki.jikiJumpDraw(canvas)
        controller.draw(canvas,clickX,clickY,clickState)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            clickState = "ACTION_DOWN"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_UP) {
            clickState = "ACTION_UP"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            clickState = "ACTION_MOVE"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }
        return super.onTouchEvent(event)
    }
}

