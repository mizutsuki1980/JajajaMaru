package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Teki {
    val ookisa = 100
    val iro = Paint()
    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(1f,0f)
    var x = 100
    var y = 100
    var xx = 100

    fun idoSyori(controller: Controller, map:Map) {

    //Ugokiを使っているのか。じゃぁそれでやるか。
        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新　（コントローラーはない）
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
        var u1CandB = sokudoKoushin(u1CandA, controller)

        //posを更新
        val u1CandC = u1CandB.copy(pos = Vec2D(u1CandB.pos.x + u1CandB.sokudo.x.toInt(), u1CandB.pos.y + u1CandB.sokudo.y.toInt()))

        //自機から離れすぎないように左右端チェック
        val u1CandD = sekaiHashiCheck(map,u1CandC)

        //障害物上下左右チェック
        //val u1CandF = shogaibutuJogeSayuu(map, u1CandE,u0)


        sokudo = u1CandC.sokudo
        kasokudo = u1CandC.kasokudo
        sekaipos = u1CandC.pos
    }



    fun kasokudoKoushin(u0:Ugoki, controller:Controller):Ugoki{
        return u0.copy(kasokudo= Vec2DF(u0.kasokudo.x,u0.sokudo.y))
    }
    fun sekaiHashiCheck(map:Map,u1CandD:Ugoki):Ugoki{
        return u1CandD
    }


    fun sokudoKoushin(u0:Ugoki, controller:Controller):Ugoki{
        //速度制限　20fになったら0fに戻る
        return if(u0.sokudo.x>=20f){
            u0.copy(sokudo = Vec2DF(0f,u0.sokudo.y))
        }else{u0.copy(sokudo = Vec2DF(u0.sokudo.x+u0.kasokudo.x,u0.sokudo.y))}


    }

    fun draw(canvas: Canvas,jiki:Jiki) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        xx = (360-jiki.sekaipos.x) + sekaipos.x

        canvas.drawCircle(xx.toFloat(),(sekaipos.y).toFloat(),(ookisa/5).toFloat(),iro)
    }

    fun tekiTuginoSyori(jiki: Jiki,map: Map){


    }

}