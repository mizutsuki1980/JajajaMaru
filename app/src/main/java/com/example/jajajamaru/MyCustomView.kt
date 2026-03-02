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
    var teki = Teki()

    var vec2dMorebou = Vec2D(initialJikiX-100, initialJikiY)
    var morebou = Morebou(vec2dMorebou)

    var gameCounter = GameCounter()
    var controller = Controller()
    val map = Map()
    var pointerCount = 0

    fun syokikaGameReset(){
        frame = 0
        jiki = Jiki(vec2d)
        vec2dMorebou = Vec2D(initialJikiX-100, initialJikiY)
        morebou = Morebou(vec2dMorebou)

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
        controller.jumpButtonOsiTuduketeirukaCheck(jiki)
        jiki.idoSyori(controller,map)
        morebou.idoSyori(map)


        teki.tekiTuginoSyori(jiki,map)
        teki.idoSyori(controller,map)

        if(map.goalCheck(jiki)) {gameCounter.isClear = true}
        if(gameCounter.isClear){}else{gameCounter.time += 1}
        frame += 1  //繰り返し処理はここでやってる
        //30フレーム後にリセットだとすると
        if(frame-gameCounter.time==30){ syokikaGameReset() }
        //ここでフレームとカウンター処理で３０の差がつくと初期化するようにしている
        //ここに面が変わる処理をつければいいのでは

        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        val bgPaint = Paint()
        bgPaint.color = Color.argb(255, 0, 0, 255)   // 背景色
        bgPaint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        mapCreate(canvas)
        jiki.draw(canvas,controller)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.kirerusanpng, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, jiki.initialPos.x.toFloat()-40, jiki.sekaipos.y.toFloat()-75, null)
        //jiki.initialPos.x.toFloat()-40
        //自機の描画でこんな表記がある
        //これが原因だと思う。だってsekaipos.xのはずだからね。
        //たぶん真ん中に描画したいから、常に一定の値になっているんだと思う。
        //なんでsekaipos.xから中心を割り出して
        //そこから動いた分だけ加える
        //みたいにしないとズレてい行くんだと思う。

        morebou.draw(canvas)
        val bitmapMorebou = BitmapFactory.decodeResource(resources, R.drawable.moretyuu, BitmapFactory.Options())
        canvas.drawBitmap(bitmapMorebou, morebou.sekaipos.x.toFloat()-40, morebou.sekaipos.y.toFloat()-75, null)

        teki.draw(canvas,jiki)
        val bitmapTeki = BitmapFactory.decodeResource(resources, R.drawable.teki, BitmapFactory.Options())
        canvas.drawBitmap(bitmapTeki, teki.xx.toFloat()-40, teki.sekaipos.y.toFloat()-75, null)


        controller.draw(canvas)
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

        //二本目の指が離れたら？という判定はどうなるんだろうか？
        if(clickState == "ACTION_UP"){
            controller.syokika()
        }

        return true
    }
}

