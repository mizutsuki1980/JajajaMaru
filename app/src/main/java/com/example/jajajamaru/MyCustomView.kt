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
    var vec2d = Vec2D(initialJikiX, initialJikiY)
    var jiki = Jiki(vec2d)
    var gameCounter = GameCounter()
    var controller = Controller()
    val map = Map()
    var pointerCount = 0
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
        canvas.drawBitmap(bitmap, jiki.initialPos.x.toFloat()-40, jiki.sekaipos.y.toFloat()-75, null)
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
        //あとでここで2本押された時の処理を描く、というか何本でもいいようにかく
        controller.pushedSayuButton = "nashi"
        controller.pushedJumpButton = false
        for (i in 0 until pointerCount) {
            controller.clickX = event.getX(i).toInt()
            controller.clickY = event.getY(i).toInt()
            controller.pushCheck = controller.clickPointCheckButtonKai()
        }
        return true
    }
}

