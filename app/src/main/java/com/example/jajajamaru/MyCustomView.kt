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

    //一応この形で作っておいた方がいいんだろうか？
    //いや、総当たりじゃできないか、これは。１３列目の８行が１かどうか？は
    //最初からマス目がわかってるから、指定できちゃうでしょ。
    //うーん、こういう風にする意味はわからんな。現時点では。
/*    fun mapSyruiCheck(canvas:Canvas){
        for (i in 0 until map.masu.size) {
            for (j in 0 until map.masu[i].size) {
                map.drawMap(canvas,i,j,map.masShurui(i,j),-worldOffsetX)
            }
        }
    }
*/
    fun lowcalCheck():Boolean {
        //とりあえずｘマスだけ
        //ｙはとりあえず１３固定

        val charamasu = worldOffsetCharacterX / map.MASU_SIZE   //キャラの世界位置

        when (controller.houkou) {
            //だいたい４マスくらいあるってことかな。
            "migi" -> {
                //↓この書き方だとジャンプが止まることはない。
                if(jiki.isJump){return true}

                //右おしっぱにすると、障害物の前で、ジャンプ自体が止まってしまう問題、
                //これ、コントローラーの問題？

                //↓この書き方をすると、カクカクとジャンプが止まってしまう。ジャンプが止まるのは変じゃね？
                /*if(jiki.isJump) {
                    if (jiki.jumpFrame >= 3 && jiki.jumpFrame <= 7) {
                        return true
                    } else {
                        return false
                    }
                }
                */
                //jumpFrameのは10,9,8,7,6,5,4,3,2,1,0と減っていくので、
                // jumpFrameの６～３とかって指定すれば、頂点付近はとれる
                //けど、「高さ」ってなってくるとなー

                //jumpFrameの３～０までって指定すれば
                //岩のあるところから強制的に１マス動かせるんじゃないかな。

                //あーここにかくんじゃだめなんだ。
                //落下中は右を押してないかもしれないし。
                //落下してる最中は、最後に「右」か「左」を覚えておいて
                //めり込んだ時に、どちらかの方向へ強制移動する、みたいな、。

                if (map.masu[13][charamasu + 3] == 1) {
                    return false
                } else {
                    return true
                }

            }

            //リストにない[-1]とか取り出そうとすると、強制終了をくらう。
            "hidari" -> {
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
        clickPointCheck()
        if(lowcalCheck()) {  ido() }
        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
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
        if(clickX > (30+170+170+170) && clickX <(30+170+170+170+150)){
            if(clickY > 920 && clickY < 1070) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "migi"
                }
            }
        }
        if(clickX > (30+170) && clickX <(30+170+170+150)){
            if(clickY > 920+170 && clickY < 1070+170) {
                if (clickState == "ACTION_DOWN" || clickState == "ACTION_MOVE") {
                    controller.houkou = "jump"
                }
            }
        }
    }




    override fun onDraw(canvas: Canvas) {
        val bgPaint = Paint()
        bgPaint.color = Color.argb(255, 0, 0, 255)   // 背景色
        bgPaint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
        background.draw(canvas)
        mapCreate(canvas)
        jiki.draw(canvas)
        controller.draw(canvas,clickX,clickY,clickState)
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


    //まず、コントローラーの部分をちゃんと見直す必要があるのかもしれない。
    //もっとリファクタリングする、というか。

    //ジャンプ押しながら右
    //ジャンプ押しながら左
    //右押しながらジャンプ
    //左押しながらジャンプ
    //    の四パターンある

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

