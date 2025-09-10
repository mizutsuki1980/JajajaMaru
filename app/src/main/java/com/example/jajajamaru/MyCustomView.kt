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
    var clickState = "nashi"
    val initialJikiX = 360 //初期位置
    val initialJikiY = 500 //初期位置

    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置

    var jiki = Jiki(initialJikiX, initialJikiY)
    var controller = Controller()

    var clickMotionVent1 = ""
    var clickMotionVent2 = ""
    var clickMotionVent3 = ""

    var worldOffsetX = 0
    var worldOffsetY = 0

    val map = Map()


    var worldOffsetCharacterX = map.MASU_SIZE * 7   //マスサイズ32
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置

    var worldOffsetCharacterY = 0


    fun syokikaGameReset(){
        isJump = false
        jiki = Jiki(initialJikiX, initialJikiY)
        sekaix = 224

    }

    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }



    //毎フレームで自分がどこのマスにいるのか？の計算がいる
    //ではワールドマップを動かしてみよう
    //自分のマスが２こ動いたら、ワールドマップも２こ動くようにしよう

    var migiGamenGotoIdo = false
    var hidariGamenGotoIdo = false
    var vYokoPlus = 0f
    fun jikiNoIchiYoko() {
        var kasokudo = kasokudoYoko(controller.houkou)
        //速度制限
        if (kasokudo > 3f) {
            kasokudo = 3f
        }
        if (kasokudo < -3f) {
            kasokudo = -3f
        }
        //画面による制限

        //〇〇〇〇〇ここに画面が左端、右端だったら、というように書く

        //移動制限
        if (sekaix <= 1 || sekaix >= 800) {
            //まず、ワールド内であるか確認
        } else {
            //ここはワールド内
            vYokoPlus = vYokoPlus + kasokudo
            jiki.x += vYokoPlus.toInt()
            sekaix += vYokoPlus.toInt()
        }

        if (sekaix <= 1) {//左端なら
            jiki.x += 10
            sekaix += 10
            vYokoPlus = 0f
        }
        if (sekaix >= 800) {//右端なら
            jiki.x -= 10
            sekaix -= 10
            vYokoPlus = 0f
        }
    }


    fun kasokudoYoko(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (vYokoPlus == 0f) { return 0.0f }
                if (vYokoPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


    var isJump = false
    var vJump = 0f
    fun kasokudoJump():Float {
        return -5.0f
    }
    fun jikiNoIchiJump(){
        if(controller.houkou=="jump"){
            if(isJump==false){
                isJump=true
                vJump = 50f
                jiki.y -= vJump.toInt()
            }
        }
        if(isJump) {
            vJump = vJump + kasokudoJump()
            jiki.y -= vJump.toInt()
        }
    }

    fun tsugiNoSyori() {
        controller.clickPointCheck(clickX,clickY,clickState)
        jikiNoIchiYoko()    //横方向　課題用
        jikiNoIchiJump()    //縦方向  ジャンプ
        frame += 1  //繰り返し処理はここでやってる
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

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.kirerusan, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, jiki.x.toFloat()-40, jiki.y.toFloat()-45, null)
    }

   fun mapCreate(canvas:Canvas){
        val jikinomasu = sekaix /32 //初期値は7
        if(jikinomasu>=9){
            worldOffsetX = (jikinomasu-7)*32
        //なんとなくはできた
        //これで、自機をとめる。どうやって？
        }

        for (i in 0 until map.masu.size) {
            for (j in 0 until map.masu[i].size) {
                map.drawMap(canvas,i,j,map.masShurui(i,j),-worldOffsetX)
            }
        }
    }

    /*
    fun lowcalCheck(houkou:String):Boolean {
        //とりあえずｘマスだけ //ｙはとりあえず１３固定
        val charamasu = worldOffsetCharacterX / map.MASU_SIZE   //キャラの世界位置
        when (houkou) {
            "migi" -> {
                //if(jiki.isJump){return true}

                if (map.masu[13][charamasu + 3] == 1) {
                    return false
                } else {
                    return true
                }
            }
            "hidari" -> {            //リストにない[-1]とか取り出そうとすると、強制終了をくらう。
                //if(jiki.isJump){return true}
                if (map.masu[13][charamasu - 1] == 1) {
                    return false
                } else {
                    return true
                }
            }
            else -> { return true}
        }
    }
    */

    var clickNitenmeX = 0
    var clickNitenmeY = 0
    var clickNitenmeMotionTyp = ""
    var  pointerCount = 0
    var nitenmeButton = "nashi"
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

