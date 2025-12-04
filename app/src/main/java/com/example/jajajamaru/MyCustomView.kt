package com.example.jajajamaru

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var frame = 0
    var clickState = ""
    val initialJikiX = 360 //初期位置
    val initialJikiY = 400 //初期位置
    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var vec2d = Vec2D(initialJikiX, initialJikiY)
    var jiki = Jiki(vec2d)
    var gameCounter = GameCounter()
    var controller = Controller()
    val map = Map()

    var  pointerCount = 0
    var nitenmeButton = "nashi"

    fun syokikaGameReset(){
        frame = 0
        jiki = Jiki(vec2d)
        gameCounter = GameCounter()
    }


    fun debug() : String{
        val y = jiki.sekaipos.y
        val x = jiki.sekaipos.x
        val masuY = y /32
        val masuX = x /32
        val genzaichi = map.masu[masuY][masuX]
        val masuMigiPlus1 = map.masu[masuY][masuX+1]



        return "y=${y},masuY=${masuY},x=${x},masuX=${masuX}\n" +
                "genzaichi=${genzaichi},masuPlus1=${masuMigiPlus1},"

    }
    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }

    fun tsugiNoSyori() {
        controller.clickPointCheck(clickX,clickY,clickState)
        jiki.idoSyori(controller,map)
        if(map.goalCheck(jiki)) {gameCounter.isClear = true}
        if(gameCounter.isClear){}else{gameCounter.time += 1}
        frame += 1  //繰り返し処理はここでやってる

        //一回クリアした後に、もう一回クリアすると、へんな風になっている気がする。
        //フレームがリセットされていないので、二回目以降はクリア後に自動ですすまない

        //100フレーム後にリセットだとすると
        if(frame-gameCounter.time==15){
            syokikaGameReset()
        }

        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        val bgPaint = Paint()
        bgPaint.color = Color.argb(255, 0, 0, 255)   // 背景色
        bgPaint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        mapCreate(canvas)
        jiki.draw(canvas)
        controller.draw(canvas)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.kirerusanpng, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, jiki.initialPos.x.toFloat()-40, jiki.sekaipos.y.toFloat()-45, null)
        gameCounter.draw(canvas,frame,jiki,map)
    }




    fun mapCreate(canvas:Canvas){
        for (i in 0 until map.masu.size) {
            for (j in 0 until map.masu[i].size) {
                map.drawMap(canvas,i,j,map.masShurui(i,j),jiki)
            }
        }
        map.drawMapGoal(canvas,0,0,jiki)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        pointerCount = event.pointerCount

        if (event.action == MotionEvent.ACTION_DOWN) { clickState = "ACTION_DOWN"}
        if (event.action == MotionEvent.ACTION_UP) { clickState = "ACTION_UP"}
        if (event.action == MotionEvent.ACTION_MOVE) { clickState = "ACTION_MOVE" }

        clickX = event.x.toInt()
        clickY = event.y.toInt()

        return true

        /*
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                // 画面に触れている指の数
                pointerCount = event.pointerCount
                if (event.actionMasked == MotionEvent.ACTION_DOWN) { clickNitenmeMotionTyp = "ACTION_DOWN" }
                if (event.actionMasked == MotionEvent.ACTION_POINTER_DOWN) { clickNitenmeMotionTyp = "ACTION_DOWN" }
                if (event.actionMasked == MotionEvent.ACTION_POINTER_UP) {clickNitenmeMotionTyp = "ACTION_UP" }
                if (event.actionMasked == MotionEvent.ACTION_MOVE) {clickNitenmeMotionTyp = "ACTION_MOVE" }
                if (pointerCount >= 2) {
                    val pointerIndex = 1  // 2本目の指
                    // 2本目の指のインデックスは 1
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    clickNitenmeX =  x2.toInt()
                    clickNitenmeY =  y2.toInt()
                }
            }
        }
        */
    }
}

