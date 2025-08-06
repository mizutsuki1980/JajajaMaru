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
        if (controller.houkou == "migi"){
            jiki.x += 5
            background.x  += 15
        }
    }
    fun hidariIdo(){
        if (controller.houkou == "hidari"){
            jiki.x -= 5
            background.x  -= 15
        }
    }


    fun jumpCheckIdo(){
            if (controller.isJumpButton ) {
                jiki.jumpTakasa += 28 //ずーとマイナスされているから、プラスすると滞空時間が増える
            }
    }



    fun clickPointCheck(){
        //最初にリセット
        controller.houkou = "nashi"
        if(clickX > 50 && clickX <150){
            if(clickY > 920 && clickY <1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "hidari"
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

        if(clickX > (170) && clickX <(30+170+320)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "jump"
                }
                if (clickState == "ACTION_UP") {
                    controller.houkou = "jump"
                }
            }
        }
    }

    fun tsugiNoSyori() {

        clickPointCheck()
        when (controller.houkou) {
            "migi" -> {migiIdo()}
            "hidari" -> {hidariIdo()}
            "jump" -> {jumpCheckIdo()}
        }
        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tosu, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, 50.0F+(background.x.toFloat()), 200.0F, null)
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

