package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.widget.Toast

class Jiki(var pos: Vec2D) {

    val ookisa = 100
    val iro = Paint()


    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)

    var isJump = false

    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
         idoUeShita(controller,map)        //縦移動　y軸
    }


    fun idoUeShita(controller: Controller, map: Map){
        // val u0 = Ugoki(pos,sokudo,kasokudo)
        val u0 = Ugoki(sekaipos,sokudo,kasokudo)
        val u1CandA = u0.copy(kasokudo= Vec2DF(kasokudo.x,kasokudoJump()))
        val u1CandB = u1CandA.copy(sokudo= Vec2DF(u1CandA.sokudo.x,u1CandA.sokudo.y + u1CandA.kasokudo.y))

        //①最初に、ジャンプをしていなかった場合、ジャンプをする（）

        var u1CandC = u1CandB
        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x,-45f))
            }
        }


        val u1CandD = u1CandC.copy(pos= Vec2D(u1CandC.pos.x,sekaipos.y + u1CandC.sokudo.y.toInt()))
        //②次の位置を計算する
       val c = mapCheckY(map, u1CandD.pos.y)
       val u1CandE =  if (c){
           u1CandD
       }else {
            isJump = false
           val ySyougai =  (u1CandD.pos.y / 32)*32 //かならず上辺が入る
           val yLimit = (ySyougai)
           u1CandD.copy(pos = Vec2D(u1CandD.pos.x,yLimit),sokudo = Vec2DF(u1CandD.sokudo.x,0f))
       }

 /*       val y1CandB =  if (c){
            y1CandA
        }else {
            isJump = false
            val ySyougai =  (y1CandA/ 32)*32 //かならず上辺が入る
            val yLimit = (ySyougai)
            yPlusCand = 0f
            yLimit
        }
*/
        //世界の端かどうかを補正したy1候補
        val u1CandF = if(isJump) {
             if (u1CandE.pos.y < 96) {
                 u1CandE.copy(pos = Vec2D(u1CandE.pos.x,96),sokudo = Vec2DF(u1CandE.sokudo.x,0f))

            } else {
                u1CandE
            }
        }else{
            u1CandE
        }

/*

        val y1CandC = if(isJump) {
             if (y1CandB < 96) {
                 yPlusCand = 0f
                96
            } else {
                y1CandB
            }
        }else{
            y1CandB

        }

 */

        sokudo = u1CandF.sokudo
        kasokudo = u1CandF.kasokudo

        pos = pos.copy(y=u1CandF.pos.y)
        sekaipos = u1CandF.pos

        /*
        sokudo = Vec2DF(sokudo.x,u1CandF.sokudo.y)

        sekaipos = Vec2D(sekaipos.x,u1CandF.pos.y)
        //③計算した位置が障害物かどうかを判定する


        //④結果を反映させる


        //最後に代入
        pos = Vec2D(pos.x,sekaipos.y)

         */

    //四角を表示するときに、変にプラスをしてたから、なんかズレた表示になっていたのか
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

    fun idoMigiHidari(controller: Controller, map: Map){
        kasokudo = Vec2DF(kasokudoDush(controller.houkou),kasokudo.y)

        val xPlus1Cand = sokudo.x + kasokudo.x
        val x1CandA = sekaipos.x + xPlus1Cand.toInt()

        //世界の端かどうかを補正したx1候補
        val x1CandB = if(x1CandA>1500){1500}else if(x1CandA<0){0}else{x1CandA}
        val xPlus1SokudoSeigenCand =  if(x1CandA>1500){0f}else if(x1CandA<0){0f}else{xPlus1Cand}
        //速度制限つけた
        var xPlus1 = if(xPlus1SokudoSeigenCand>= 30){30f}else if(xPlus1SokudoSeigenCand<= -30){-30f}else{xPlus1SokudoSeigenCand}
        //障害物にぶつかっているかどうかを補正したx1候補
        val x1CandC = if(mapCheck(map,x1CandB,xPlus1)){
            x1CandB
        }else{
            val xSyougai =  (x1CandB/ 32)*32 //かならず左肩が入る
            val xLimit = if(xPlus1>0){(xSyougai - ookisa /2)}else if(xPlus1<0){xSyougai + 32 + (ookisa /2)}else{sekaipos.x}
            xPlus1 = 0f
            xLimit
        }
        sekaipos = Vec2D(x1CandC,sekaipos.y)
        sokudo = Vec2DF(xPlus1,sokudo.y)

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

