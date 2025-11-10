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
        val u1CandA = u0.copy(kasokudo = Vec2DF(kasokudoDush(controller.houkou), kasokudoJump()))
        val u1CandB = u1CandA.copy(
            sokudo = Vec2DF(
                u1CandA.sokudo.x + u1CandA.kasokudo.x,
                u1CandA.sokudo.y + u1CandA.kasokudo.y
            )
        )
        var u1CandC = u1CandB
        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -45f))
            }
        }

        //横方向の補正　1500と０　世界の端？
        val u1CandC0 =
            u1CandC.copy(pos = Vec2D(min(max(0, u1CandC.pos.x), 1500), u1CandC.pos.y))
        val u1CandC1 =
            u1CandC0.copy(pos = Vec2D(u1CandC0.pos.x + u1CandC0.sokudo.x.toInt(), sekaipos.y + u1CandC0.sokudo.y.toInt()))
        val u1CandC2 = if (mapCheckY(map, u1CandC1.pos.y)) {
            u1CandC1
        } else {
            isJump = false
            val ySyougai = (u1CandC1.pos.y / 32) * 32 //かならず上辺が入る
            u1CandC1.copy(pos = Vec2D(u1CandC1.pos.x, ySyougai), sokudo = Vec2DF(u1CandC1.sokudo.x, 0f))
        }

        val u1CandD = if (isJump && u1CandC2.pos.y < 96) {
            u1CandC2.copy(pos = Vec2D(u1CandC2.pos.x, 96), sokudo = Vec2DF(u1CandC2.sokudo.x, 0f))
        } else {
            u1CandC2
        }



        val u1CandE = if (u1CandD.pos.x == 1500 || u1CandD.pos.x == 0) {
            u1CandD.copy(sokudo = Vec2DF(0f, u1CandD.sokudo.y))
        } else {
            u1CandD
        }


        val u1CandF = u1CandE.copy(
            sokudo = Vec2DF(
                min(max(-30f, u1CandE.sokudo.x), 30f),
                u1CandE.sokudo.y
            )
        )

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



        //こっから下が問題だとは思う、なんでかはわからん


        sokudo = u1CandG.sokudo
        kasokudo = u1CandG.kasokudo
        sekaipos = u1CandG.pos

        //横移動 x軸

        //zzzzz

        //世界の端かどうかを補正したy1候補
        pos = pos.copy(y = u1CandF.pos.y)

        sokudo = sokudo.copy(y=u1CandF.sokudo.y)
        kasokudo = kasokudo.copy(y=u1CandF.kasokudo.y)
        sekaipos = sekaipos.copy(y=u1CandF.pos.y)
        //縦移動　y軸
    }


    fun mapCheckY(map:Map,y1Cand:Int):Boolean{
        val checkPointY = y1Cand
        val yBlock = ( checkPointY/ 32)
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
        canvas.drawCircle(pos.x.toFloat(),(pos.y).toFloat(),(ookisa/2).toFloat(),iro)
    }

}

