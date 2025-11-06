package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import kotlin.math.max
import kotlin.math.min

class Jiki(var pos: Vec2D) {

    val ookisa = 100
    val iro = Paint()


    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)

    var isJump = false

    fun idoSyori(controller: Controller, map: Map) {
        val u0 = Ugoki(sekaipos, sokudo, kasokudo)
        val u1CandA_M = u0.copy(kasokudo = Vec2DF(kasokudoDush(controller.houkou), kasokudoJump()))
//        val u1CandA = u0.copy(kasokudo = Vec2DF(kasokudo.x, kasokudoJump()))

        val u1CandB_M = u1CandA_M.copy(
            sokudo = Vec2DF(
                u1CandA_M.sokudo.x + u1CandA_M.kasokudo.x,
                u1CandA_M.sokudo.y + u1CandA_M.kasokudo.y
            )
        )
//        val u1CandB =
  //          u1CandA_M.copy(sokudo = Vec2DF(u1CandA_M.sokudo.x, u1CandA_M.sokudo.y + u1CandA_M.kasokudo.y))


        val u1CandC_M =
            u1CandB_M.copy(pos = u1CandB_M.pos.copy(x = u1CandB_M.pos.x + u1CandB_M.sokudo.x.toInt()))
        val u1CandD_M =
            u1CandC_M.copy(pos = Vec2D(min(max(0, u1CandC_M.pos.x), 1500), u1CandC_M.pos.y))
        val u1CandE_M = if (u1CandD_M.pos.x == 1500 || u1CandD_M.pos.x == 0) {
            u1CandD_M.copy(sokudo = Vec2DF(0f, u1CandD_M.sokudo.y))
        } else {
            u1CandD_M
        }
        val u1CandF_M = u1CandE_M.copy(
            sokudo = Vec2DF(
                min(max(-30f, u1CandE_M.sokudo.x), 30f),
                u1CandE_M.sokudo.y
            )
        )
        val u1CandG_M = if (mapCheck(map, u1CandF_M.pos.x, u1CandF_M.sokudo.x)) {
            u1CandF_M
        } else {
            val xSyougai = (u1CandF_M.pos.x / 32) * 32 //かならず左肩が入る
            val xLimit = if (u1CandF_M.sokudo.x > 0) {
                (xSyougai - ookisa / 2)
            } else if (u1CandF_M.sokudo.x < 0) {
                xSyougai + 32 + (ookisa / 2)
            } else {
                sekaipos.x
            }
            u1CandF_M.copy(
                pos = Vec2D(xLimit, u1CandF_M.pos.y),
                sokudo = Vec2DF(0f, u1CandF_M.sokudo.y)
            )
        }
        sekaipos = u1CandG_M.pos
        sokudo = u1CandG_M.sokudo
        kasokudo = u1CandG_M.kasokudo
        //横移動 x軸

        //zzzzz

        var u1CandC = u1CandB_M
        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -45f))
            }
        }
        val u1CandD =
            u1CandC.copy(pos = Vec2D(u1CandC.pos.x, sekaipos.y + u1CandC.sokudo.y.toInt()))
        //②次の位置を計算する
        val c = mapCheckY(map, u1CandD.pos.y)
        val u1CandE = if (c) {
            u1CandD
        } else {
             isJump = false
            val ySyougai = (u1CandD.pos.y / 32) * 32 //かならず上辺が入る
            val yLimit = (ySyougai)
            u1CandD.copy(pos = Vec2D(u1CandD.pos.x, yLimit), sokudo = Vec2DF(u1CandD.sokudo.x, 0f))
        }
        //世界の端かどうかを補正したy1候補
        val u1CandF = if (isJump && u1CandE.pos.y < 96) {
            u1CandE.copy(pos = Vec2D(u1CandE.pos.x, 96), sokudo = Vec2DF(u1CandE.sokudo.x, 0f))
        } else {
            u1CandE
        }
        sokudo = sokudo.copy(y=u1CandF.sokudo.y)
        kasokudo = kasokudo.copy(y=u1CandF.kasokudo.y)
        pos = pos.copy(y = u1CandF.pos.y)
        sekaipos = sekaipos.copy(y=u1CandF.pos.y)
        //縦移動　y軸
    }


    fun mapCheckY(map:Map,y1CandA:Int):Boolean{
        val checkPointY = y1CandA
        val yBlock = ( checkPointY/ 32)
        val xBlock = (sekaipos.x/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun kasokudoJump(): Float {
        return 5.0f
    }

    fun mapCheck(map:Map,x1CandB:Int,xPlus1: Float):Boolean{
        //mapcheckでxPlusによる左右判定をしていないと予想
        val checkPoint = x1CandB
        val checkBlock = if(xPlus1>0){
            //右方向だったら
            ( checkPoint/ 32)
        }else if(xPlus1<0){
            //左方向だったら
            ( checkPoint/ 32)-1
        }else{( checkPoint/ 32)
        }
        val masu = map.masu
        //println("cb=$checkBlock,x1cand=$x1CandB,mas[9]=${masu[13][9]},mas[10]=${masu[13][10]},mas[11]=${masu[13][11]}")
        val yBlock = (sekaipos.y / 32)-2
        println("yBlock=$yBlock,sekaipos.y${sekaipos.y},isJump=$isJump")
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
        canvas.drawCircle(pos.x.toFloat(),(pos.y).toFloat(),(ookisa/2).toFloat(),iro)
    }

}

