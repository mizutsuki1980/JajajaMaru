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
    var isJump = false

    fun idoSyori(controller: Controller, map: Map) {

        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
        var u1CandC = sokudoKoushin(u1CandA, controller)

        //posを更新
        val u1CandD = u1CandC.copy(pos = Vec2D(u1CandC.pos.x + u1CandC.sokudo.x.toInt(), u1CandC.pos.y + u1CandC.sokudo.y.toInt()))

        //世界の上下左右端チェック
        val u1CandE = sekaiHashiCheck(map,u1CandD)

        //障害物上下左右チェック
        val u1CandF = shogaibutuJogeSayuu(map, u1CandE,u0)


        sokudo = u1CandF.sokudo
        kasokudo = u1CandF.kasokudo
        sekaipos = u1CandF.pos

    }

    private fun shogaibutuJogeSayuu(map: Map, before: Ugoki,u0: Ugoki): Ugoki {

        //障害物上下処理
        val afterJouge = if (mapCheckY(map, before.pos.x,before.pos.y)) {
            before
        } else {
            val yU0 = u0.pos.y
            val yU1 = before.pos.y
            //境界線上にとまらないように、-1と+1している
            val yLimit =if(yU0>yU1){ //上昇中
                1+32+(yU1 / 32) * 32
            }else{//下降中
                isJump = false
                -1+(yU1 / 32) * 32
            }
            before.copy(pos = Vec2D(before.pos.x, yLimit), sokudo = Vec2DF(before.sokudo.x, 0f))
        }

        //posの値を見て障害物か判定している。posの値を修正している。
        //障害物左右処理
        val afterSayuu = if (mapCheckX(map, afterJouge.pos.x,afterJouge.pos.y)) {
            afterJouge
        } else {
            val xU1 = afterJouge.pos.x
            val xU0 = u0.pos.x
            val xLimit = if(xU0>xU1){//右からきてる
                1+32+(afterJouge.pos.x / 32) * 32
            }else if(xU0<xU1){//左からきてる
                -1+(afterJouge.pos.x / 32) * 32
            }else{
                afterJouge.pos.x
            }

            afterJouge.copy(
                pos = Vec2D(xLimit, afterJouge.pos.y),
                sokudo = Vec2DF(0f, afterJouge.sokudo.y)
            )
        }

        //ここでもう一回判定をする、止まった位置の下には何かあるか？で落下するか決める。
        val afterRakka = if (mapCheckYRakka(map, afterSayuu.pos.x,afterSayuu.pos.y)) {
            //もし静止した位置で下に足場がなかったら
            //ｙは落下処理する、いったん元の値に戻して、加速して速度を足してＰＯＳを更新する
            val yKasokudo = before.kasokudo.y
            val ySokudo = before.sokudo.y
            val yPos = before.pos.y

             afterSayuu.copy(
                pos = Vec2D(afterSayuu.pos.x, (yPos + (ySokudo + yKasokudo)).toInt()),
                sokudo = Vec2DF(0f, (ySokudo + yKasokudo)),
                kasokudo = Vec2DF(afterSayuu.kasokudo.x,yKasokudo)
            )
        } else {
            afterSayuu

        }

        return afterSayuu
    }

    fun mapCheckYRakka(map:Map,x1cand:Int,y1Cand:Int):Boolean{
        val yBlock = ( y1Cand/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock+1][xBlock] == 1){ false }else{true}
    }

    fun mapCheckX(map:Map,x1cand:Int,y1Cand:Int):Boolean{
        val yBlock = ( y1Cand/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun mapCheckY(map:Map,x1Cand:Int,y1Cand:Int):Boolean{
        val checkPointY = y1Cand
        val yBlock = ( checkPointY/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1Cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }



    /**
    速度を更新する、まず加速度から速度を計算して、その後に最大速度制限とジャンプの処理をする
    */
    private fun sokudoKoushin(before: Ugoki, controller: Controller): Ugoki {

        //ここで速度を入れてるのは間違いない、加速度ｙか速度ｙに０が入ってるんじゃないかな？
        //速度の更新
        val u1CandA = before.copy(
            sokudo = Vec2DF(
                before.sokudo.x + before.kasokudo.x,
                before.sokudo.y + before.kasokudo.y
            )
        )


        //速度の上限設定
        val u1CandB = u1CandA.copy(
            sokudo = Vec2DF(
                min(max(-30f, u1CandA.sokudo.x), 30f),
                u1CandA.sokudo.y
            )
        )

        //ジャンプ処理
        var u1CandC = u1CandB
        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -35f))
            }
        }
        return u1CandC
    }

    private fun kasokudoKoushin(u0: Ugoki, controller: Controller): Ugoki {
        return u0.copy(kasokudo = Vec2DF(kasokudoX(controller.houkou,u0), kasokudoY()))
        //左右にキーを入れていると、加速度が2.5か-2.5が返っている
        //だから何？
    }

    fun kasokudoY(): Float {
        return 4.0f
    }




    fun kasokudoX(houkou:String,u0: Ugoki):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> {
                if (u0.sokudo.x == 0f) { return 0.0f }
                if (u0.sokudo.x > 0) {
                    return -2.5f
                } else {
                    return 2.5f
                }
            }

            else -> return 0f
        }
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
        val u1Cand = if (afterSayuu.pos.x == map.migiMax() || afterSayuu.pos.x == 0) {
            afterSayuu.copy(sokudo = Vec2DF(0f, afterSayuu.sokudo.y))
        } else {
            afterSayuu
        }
        return u1Cand
    }

    fun draw(canvas: Canvas) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        canvas.drawCircle(initialPos.x.toFloat(),(sekaipos.y).toFloat(),(ookisa/2).toFloat(),iro)
    }

}

