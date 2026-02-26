package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Teki {
    val ookisa = 100
    val iro = Paint()
    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)
    var x = 100
    var y = 100
    var xx = 100

    fun idoSyori(controller: Controller, map:Map) {

    //Ugokiを使っているのか。じゃぁそれでやるか。
        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新　（コントローラーはいらない）
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
      //  var u1CandB = sokudoKoushin(u1CandA, controller)

        //posを更新
    //    val u1CandC = u1CandB.copy(pos = Vec2D(u1CandB.pos.x + u1CandB.sokudo.x.toInt(), u1CandB.pos.y + u1CandB.sokudo.y.toInt()))

        //世界の上下左右端チェック
       // val u1CandE = sekaiHashiCheck(map,u1CandD)

        //障害物上下左右チェック
        //val u1CandF = shogaibutuJogeSayuu(map, u1CandE,u0)


       sokudo = u1CandA.sokudo
        kasokudo = u1CandA.kasokudo
        sekaipos = u1CandA.pos
    }



    fun kasokudoKoushin(u0:Ugoki, controller:Controller):Ugoki{
//        return u0.copy(sekaipos = Vec2D(10,u0.sekaipos.y))
        return u0.copy(sekaipos = Vec2D(10,20))
    }


    fun sokudoKoushin(u0:Ugoki, controller:Controller){}

    fun draw(canvas: Canvas,jiki:Jiki) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        xx = (360-jiki.sekaipos.x) + x

        canvas.drawCircle(xx.toFloat(),(sekaipos.y).toFloat(),(ookisa/5).toFloat(),iro)
    }
    fun tekiTuginoSyori(jiki: Jiki,map: Map){
        x += 10
    }

}