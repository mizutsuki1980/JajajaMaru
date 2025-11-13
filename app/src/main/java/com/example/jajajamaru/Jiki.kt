package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import kotlin.math.max
import kotlin.math.min

class Jiki(val initialPos: Vec2D) {
    //posがなんなのか調べたい
    //まず呼ばれるときに作ってる
    //    var vec2d = Vec2D(360, 400)
    //    var jiki = Jiki(vec2d)
    //こんな感じで初期設定位置が入って作られる


    val ookisa = 100
    val iro = Paint()
    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)

    var isJump = false

    fun idoSyori(controller: Controller, map: Map) {
        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新
        val u1CandA = u0.copy(kasokudo = Vec2DF(kasokudoDush(controller.houkou), kasokudoJump()))


        //速度更新
        val u1CandB = u1CandA.copy(
            sokudo = Vec2DF(
                u1CandA.sokudo.x + u1CandA.kasokudo.x,
                u1CandA.sokudo.y + u1CandA.kasokudo.y
            )
        )


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


        //posを更新
        val u1CandC0 =
            u1CandC.copy(pos = Vec2D(u1CandC.pos.x + u1CandC.sokudo.x.toInt(), sekaipos.y + u1CandC.sokudo.y.toInt()))



        //横方向の補正　1500と０　世界の端？
        //世界の境界チェック
        val u1CandC1 =
            u1CandC0.copy(pos = Vec2D(min(max(0, u1CandC0.pos.x), 1500), u1CandC0.pos.y))

        //val u1CandE =　でposの値を見て世界の端だったら速度を０にしている。

        //画面端だったら速度を０に
        val u1CandC1A = if (u1CandC1.pos.x == 1500 || u1CandC1.pos.x == 0) {
            u1CandC1.copy(sokudo = Vec2DF(0f, u1CandC1.sokudo.y))
        } else {
            u1CandC1
        }




        //障害物上下処理
        val u1CandC2 = if (mapCheckY(map, u1CandC1A.pos.y)) {
            u1CandC1A
        } else {
            isJump = false
            val ySyougai = (u1CandC1A.pos.y / 32) * 32 //かならず上辺が入る
            u1CandC1A.copy(pos = Vec2D(u1CandC1A.pos.x, ySyougai), sokudo = Vec2DF(u1CandC1A.sokudo.x, 0f))
        }


        //世界の上端チェック
        val u1CandF = if (isJump && u1CandC2.pos.y < 96) {
            u1CandC2.copy(pos = Vec2D(u1CandC2.pos.x, 96), sokudo = Vec2DF(u1CandC2.sokudo.x, 0f))
        } else {
            u1CandC2
        }



        //val u1CandG =　でposの値を見て障害物か判定している。posの値を修正している。

        //障害物左右処理
        val u1CandG = if (mapCheck(map, u1CandF.pos.x, u1CandF.sokudo.x)) {
            u1CandF
        } else {
            val xSyougai = (u1CandF.pos.x / 32) * 32 //かならず左肩が入る
            val xLimit = if (u1CandF.sokudo.x > 0) {
                (xSyougai - ookisa / 2)
            } else if (u1CandF.sokudo.x < 0) {
                xSyougai + 32 + (ookisa / 2)
            } else {
                u1CandF.pos.x
            }
            u1CandF.copy(
                pos = Vec2D(xLimit, u1CandF.pos.y),
                sokudo = Vec2DF(0f, u1CandF.sokudo.y)
            )
        }

        sokudo = u1CandG.sokudo
        kasokudo = u1CandG.kasokudo
        sekaipos = u1CandG.pos

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

