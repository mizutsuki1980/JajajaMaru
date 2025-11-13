package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import kotlin.math.max
import kotlin.math.min

class Jiki(val initialPos: Vec2D) {
    val ookisa = 100
    val iro = Paint()
    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)
    //ここでマスのサイズとマスの個数で自動でいれたらいいんじゃね
//    val sekainoHashi = 3500



    var isJump = false

    fun idoSyori(controller: Controller, map: Map) {

        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
        var u1CandC = sokudoKoushin(u1CandA, controller)

        //posを更新
        val u1CandC0 = u1CandC.copy(pos = Vec2D(u1CandC.pos.x + u1CandC.sokudo.x.toInt(), sekaipos.y + u1CandC.sokudo.y.toInt()))

        //世界の上下左右端チェック
        val u1CandC1A = sekaiHashiCheck(map,u1CandC0)

        //障害物上下左右チェック
        val u1CandG = shogaibutuJogeSayuu(map, u1CandC1A)


        sokudo = u1CandG.sokudo
        kasokudo = u1CandG.kasokudo
        sekaipos = u1CandG.pos

    }

    private fun shogaibutuJogeSayuu(map: Map, before: Ugoki): Ugoki {
        //障害物上下処理
        val afterJouge = if (mapCheckY(map, before.pos.y)) {
            before
        } else {
            isJump = false
            val ySyougai = (before.pos.y / 32) * 32 //かならず上辺が入る
            before.copy(pos = Vec2D(before.pos.x, ySyougai), sokudo = Vec2DF(before.sokudo.x, 0f))
        }

        //posの値を見て障害物か判定している。posの値を修正している。
        //障害物左右処理
        val afterSayuu = if (mapCheck(map, afterJouge.pos.x, afterJouge.sokudo.x)) {
            afterJouge
        } else {
            val xSyougai = (afterJouge.pos.x / 32) * 32 //かならず左肩が入る
            val xLimit = if (afterJouge.sokudo.x > 0) {
                (xSyougai - ookisa / 2)
            } else if (afterJouge.sokudo.x < 0) {
                xSyougai + 32 + (ookisa / 2)
            } else {
                afterJouge.pos.x
            }
            afterJouge.copy(
                pos = Vec2D(xLimit, afterJouge.pos.y),
                sokudo = Vec2DF(0f, afterJouge.sokudo.y)
            )
        }
        return afterSayuu
    }

    private fun sekaiHashiCheck(map:Map,before: Ugoki): Ugoki {
        //世界の上下チェック
        val afterJouge = if (isJump && before.pos.y < 96) {
            before.copy(pos = Vec2D(before.pos.x, 96), sokudo = Vec2DF(before.sokudo.x, 0f))
        } else {
            before
        }

        //横方向の補正　世界の端？
        //世界の左右チェック
        val afterSayuu =  afterJouge.copy(pos = Vec2D(min(max(0, afterJouge.pos.x), map.migiMax()), afterJouge.pos.y))

        //posの値を見て世界の端だったら速度を０にしている。
        //画面端だったら速度を０に
        val u1CandC1A = if (afterSayuu.pos.x == map.migiMax() || afterSayuu.pos.x == 0) {
            afterSayuu.copy(sokudo = Vec2DF(0f, afterSayuu.sokudo.y))
        } else {
            afterSayuu
        }
        return u1CandC1A
    }

    /**
    速度を更新する、まず加速度から速度を計算して、その後に最大速度制限とジャンプの処理をする
    */
    private fun sokudoKoushin(
        before: Ugoki,
        controller: Controller
    ): Ugoki {
        val u1CandB = sokudoKoushin0(before)


        //速度の上限設定
        val u1CandB0 = u1CandB.copy(
            sokudo = Vec2DF(
                min(max(-30f, u1CandB.sokudo.x), 30f),
                u1CandB.sokudo.y
            )
        )


        //ジャンプ処理
        var u1CandC = u1CandB0
        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -45f))
            }
        }
        return u1CandC
    }

    private fun kasokudoKoushin(u0: Ugoki, controller: Controller): Ugoki {
        return u0.copy(kasokudo = Vec2DF(kasokudoDush(controller.houkou), kasokudoJump()))
    }

    private fun sokudoKoushin0(before:Ugoki):Ugoki{
        return before.copy(
            sokudo = Vec2DF(
                before.sokudo.x + before.kasokudo.x,
                before.sokudo.y + before.kasokudo.y
            )
        )
    }

    fun mapCheckY(map:Map,y1Cand:Int):Boolean{
        val checkPointY = y1Cand
        val yBlock = ( checkPointY/ 32)
        if(yBlock >= map.masu.size) return false
        val xBlock = (sekaipos.x/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun kasokudoJump(): Float {
        return 5.0f
    }

    fun mapCheck(map:Map,x1Cand:Int,xPlus1: Float):Boolean{
        //mapcheckでxPlusによる左右判定をしていないと予想
        val checkPoint = x1Cand
        val checkBlock = if(xPlus1>0){
            //右方向だったら
            ( checkPoint/ 32)
        }else if(xPlus1<0){
            //左方向だったら
            ( checkPoint/ 32)-1
        }else{( checkPoint/ 32)
        }
        val masu = map.masu
        val yBlock = (sekaipos.y / 32)-2
        return if(map.masu[yBlock][checkBlock+1] == 1){ false }else{true}
    }






    fun kasokudoDush(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (sokudo.x == 0f) { return 0.0f }
                if (sokudo.x > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


    fun draw(canvas: Canvas) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        canvas.drawCircle(initialPos.x.toFloat(),(sekaipos.y).toFloat(),(ookisa/2).toFloat(),iro)
    }

}

