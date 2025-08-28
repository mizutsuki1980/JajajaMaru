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

    var clickMotionVent1 = ""
    var clickMotionVent2 = ""
    var clickMotionVent3 = ""


    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }


    fun migiIdo(){
        jiki.migiIdo()
        background.migiIdo()
    }
    fun hidariIdo(){
        jiki.hidariIdo()
        background.hidariIdo()
    }
    fun ueIdo(){
        jiki.ueIdo()
        background.ueIdo()
    }
    fun shitaIdo(){
        jiki.shitaIdo()
        background.shitaIdo()
    }

    fun jumpIdo(){
        jiki.isJump=true
        //jumpボタンを押すと、一定時間操作を受け付けなくなって、キャラクターだけが浮き上がるような感じにしたい
    }


    fun clickPointCheck(){
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
        if(clickX > (30+170) && clickX <(30+170+150+150)){
            if(clickY > 920+170 && clickY < 1070+170) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "jump"
                }
            }
        }
    }
    //段々をつけてみる
    //落下、というステイツができるのか？

    fun tsugiNoSyori() {

        clickPointCheck()
        if(jiki.isJump){
        //jump状態　右と左だけは行ける
            when (controller.houkou) {
                "migi" -> { migiIdo() }
                "hidari" -> { hidariIdo() }
            }

            jiki.jumpFrame--
            val jumpRyoku = 5
            when (jiki.jumpFrame) {
                9 -> {jiki.jumpTakasa=10 * jumpRyoku}
                8 -> {jiki.jumpTakasa=20 * jumpRyoku}
                7 -> {jiki.jumpTakasa=30 * jumpRyoku}
                6 -> {jiki.jumpTakasa=40 * jumpRyoku}
                5 -> {jiki.jumpTakasa=50 * jumpRyoku}
                4 -> {jiki.jumpTakasa=40 * jumpRyoku}
                3 -> {jiki.jumpTakasa=30 * jumpRyoku}
                2 -> {jiki.jumpTakasa=20 * jumpRyoku}
                1 -> {jiki.jumpTakasa=10 * jumpRyoku}

            }
            if(jiki.jumpFrame==0){
                jiki.isJump = false
                jiki.jumpFrame = 10
                jiki.jumpTakasa = 0

            }

        }else{
        when (controller.houkou) {
            "migi" -> {migiIdo()}
            "hidari" -> {hidariIdo()}
            "ue" -> {ueIdo()}
            "shita" -> {shitaIdo()}
            "jump" -> {jumpIdo()}
            }
        }


        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        val bgPaint = Paint()
        bgPaint.color = Color.argb(255, 0, 0, 255)   // 背景色
        bgPaint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        background.draw(canvas)
        jiki.draw(canvas)
        controller.draw(canvas,clickX,clickY,clickState)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.kirerusan, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, jiki.x.toFloat()-50, jiki.y.toFloat()-50-jiki.jumpTakasa, null)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_MOVE -> {
                // 画面に触れている指の数
                val pointerCount = event.pointerCount
                clickMotionVent1 = pointerCount.toString()
                if (pointerCount >= 2) {
                    // 2本目の指のインデックスは 1
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    clickMotionVent2 =  x2.toString()
                    clickMotionVent3 =  y2.toString()

//                    Log.d("Touch", "2本目の指: x=$x2, y=$y2")
                }
            }
        }

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

