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
import kotlin.coroutines.Continuation

class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var frame = 0
    var clickState = "nashi"
    val initialJikiX = 360 //初期位置
    val initialJikiY = 500 //初期位置

    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置

    var jiki = Jiki(initialJikiX, initialJikiY)
    var controller = Controller()
    var background = BackGround()

    var clickMotionVent1 = ""
    var clickMotionVent2 = ""
    var clickMotionVent3 = ""

    var worldOffsetX = 0
    var worldOffsetY = 0

    val map = Map()

    //見えないけど、これをもとに障害物にあたってるか判定するキャラクターのｘ、ｙ
    var worldOffsetCharacterX = map.MASU_SIZE * 7
    var worldOffsetCharacterY = 0

    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }

    //たぶん、ここに障害物の行き止まりを書く
    //worldOffsetCharaXとｙで、Ｍａｐを元につくる

    //キャラと世界のoffsettは得られた
    //じゃぁどうする？のか　衝突判定？

    //Mapのrow、cal調べて、岩のポジションだったらｘをプラスできない、とか？
    //マスって考え方が重要なのかな。ROWCALも結局マス目だし。ｘ、ｙとかってより
    //自分のキャラはどのマスにいるのか？って考えたらいいのかな？
    //ｘ、ｙだけじゃなくて。

    fun migiIdo() {
        if (worldOffsetX >= (map.MASU_SIZE * 27)) { //右にこれ以上はいけないという制限を付けた　世界の行き止まり
        } else {
            jiki.migiIdo()
            background.migiIdo()
            worldOffsetX += map.MASU_SIZE
            worldOffsetCharacterX += map.MASU_SIZE
        }
    }

    fun hidariIdo() {
        if (worldOffsetX <= -(map.MASU_SIZE * 6)) { //左にこれ以上はいけないという制限を付けた　世界の行き止まり
        } else {
            jiki.hidariIdo()
            background.hidariIdo()
            worldOffsetX -= map.MASU_SIZE
            worldOffsetCharacterX -= map.MASU_SIZE
        }
    }


    fun jumpIdo() {
        jiki.isJump = true
    }

    fun ido(){
        if(jiki.isJump){        //jump状態　右と左だけは行ける
            when (controller.houkou) {
                "migi" -> { migiIdo() }
                "hidari" -> { hidariIdo() }
            }
            jiki.jumpChuSyori()
        }else{
            when (controller.houkou) {
                "migi" -> {migiIdo()}
                "hidari" -> {hidariIdo()}
                "jump" -> {jumpIdo()}
            }
        }

    }

    fun lowcalCheck():Boolean {
        //とりあえずｘマスだけ //ｙはとりあえず１３固定
        val charamasu = worldOffsetCharacterX / map.MASU_SIZE   //キャラの世界位置
        when (controller.houkou) {
            "migi" -> {
                if(jiki.isJump){return true}

                if (map.masu[13][charamasu + 3] == 1) {
                    return false
                } else {
                    return true
                }
            }
            "hidari" -> {            //リストにない[-1]とか取り出そうとすると、強制終了をくらう。
                if(jiki.isJump){return true}
                if (map.masu[13][charamasu - 1] == 1) {
                    return false
                } else {
                    return true
                }
            }
            else -> { return true}
        }
    }

    fun tsugiNoSyori() {
        controller.clickPointCheck(clickX,clickY,clickState)
        if(lowcalCheck()) {  ido() }
        clickNitenCheck()        //2点目のチェック　ここでちゃんと分ける
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
        mapCreate(canvas)
        jiki.draw(canvas)
        controller.draw(canvas)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.kirerusan, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, jiki.x.toFloat()-40, jiki.y.toFloat()-45-jiki.jumpTakasa, null)
    }

    fun mapCreate(canvas:Canvas){
        for (i in 0 until map.masu.size) {
            for (j in 0 until map.masu[i].size) {
                map.drawMap(canvas,i,j,map.masShurui(i,j),-worldOffsetX)
            }
        }
    }

    var clickNitenmeX = 0
    var clickNitenmeY = 0
    var clickNitenmeMotionTyp = ""
    var  pointerCount = 0


    //なんか移動しながらのジャンプは、ちょっと高さが低い気がする。途中で終わってる？

    fun clickNitenCheck(){
        //pointerCountは何点押されているか 2点だったら
        if(pointerCount == 2){
            //２点目のｘｙをどのボタンか知る、だから別の変数にする
            controller.clickPointCheck(clickNitenmeX,clickNitenmeY,clickNitenmeMotionTyp)
            if(lowcalCheck()) {
                val nitenmeButton = controller.clickPointCheckNitenmeYo(clickNitenmeX,clickNitenmeY,clickNitenmeMotionTyp)


                //なんかそれっぽい動きになったが、方向が二回押されているケースがあるっぽい
                //なんか加速している。
                //まぁでも、ボタンのせいなので、ゆくゆくはボタン消すとおもうからいっか。


                //ido()はこれをやっている
                if(jiki.isJump){        //jump状態　右と左だけは行ける
                    when (nitenmeButton) {
                        "migi" -> { migiIdo() }
                        "hidari" -> { hidariIdo() }
                    }
                }else{
                    when (nitenmeButton) {
                        "migi" -> {migiIdo()}
                        "hidari" -> {hidariIdo()}
                        "jump" -> {jumpIdo()}
                    }
                }

            }
        }

//            val nitenmeButton = controller.clickPointCheckNitenmeYo(clickNitenmeX,clickNitenmeY,clickNitenmeMotionTyp)
  //          if (nitenmeButton == "jump"){
    //            if(lowcalCheck()) { ido() }
      //      }else{               controller.houkou //現在、最初に押しっぱなしのボタン            }




    }

    //オンタッチイベントでは、すでに２種類のポイントをとることに成功している。
    //右＆Jump　と　左＆jumpだけは許可するような作りにしたい。
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_MOVE -> {
                // 画面に触れている指の数
                pointerCount = event.pointerCount
                clickMotionVent1 = pointerCount.toString()

                if (event.actionMasked == MotionEvent.ACTION_DOWN) { clickNitenmeMotionTyp = "ACTION_DOWN" }
                if (event.actionMasked == MotionEvent.ACTION_POINTER_DOWN) { clickNitenmeMotionTyp = "ACTION_DOWN" }
                if (event.actionMasked == MotionEvent.ACTION_POINTER_UP) {clickNitenmeMotionTyp = "ACTION_UP" }
                if (event.actionMasked == MotionEvent.ACTION_MOVE) {clickNitenmeMotionTyp = "ACTION_MOVE" }



                if (pointerCount >= 2) {
                    // 2本目の指のインデックスは 1
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)
                    clickMotionVent2 =  x2.toString()
                    clickMotionVent3 =  y2.toString()
                    clickNitenmeX =  x2.toInt()
                    clickNitenmeY =  y2.toInt()
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

