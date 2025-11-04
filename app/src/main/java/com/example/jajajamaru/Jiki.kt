package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.widget.Toast

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 360    //360にした。有野指令
    var sekaiy = 400

    var xPlus = 0f
    var isJump = false
    var yPlus = 0f

    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
         idoUeShita(controller,map)        //縦移動　y軸
    }


    fun idoUeShita(controller: Controller, map: Map){
        val kasokudoy = kasokudoJump()
        var yPlusCand = yPlus + kasokudoy

        //①最初に、ジャンプをしていなかった場合、ジャンプをする（）

        if (isJump == false) {
            if (controller.houkou == "jump") {
                isJump = true
                yPlusCand = -45f
            }
        }

       val y1CandA = sekaiy + yPlusCand.toInt()

        //②次の位置を計算する
       val c = mapCheckY(map, y1CandA)
       val y1CandB =  if (c){
           y1CandA
       }else {
            isJump = false
           val ySyougai =  (y1CandA/ 32)*32 //かならず上辺が入る
           val yLimit = (ySyougai)
           yPlusCand = 0f
           yLimit
       }


        //yが０以下にならないように補正したy1Cand
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

        yPlus = yPlusCand
        sekaiy = y1CandC

        //③計算した位置が障害物かどうかを判定する


        //④結果を反映させる


        //最後に代入
        y = sekaiy

        println("y=$y,sekaiy=$sekaiy,y1CandA=$y1CandA,yPlusCand=$yPlusCand,${(y1CandB/32)}")
    //四角を表示するときに、変にプラスをしてたから、なんかズレた表示になっていたのか
    }

    fun mapCheckY(map:Map,y1CandA:Int):Boolean{
        val checkPointY = y1CandA
        val yBlock = ( checkPointY/ 32)
        val xBlock = (sekaix/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun kasokudoJump(): Float {
        return 5.0f
    }

    fun idoMigiHidari(controller: Controller, map: Map){
        val kasokudox = kasokudoX(controller.houkou)
        val xPlus1Cand = xPlus + kasokudox
        val x1CandA = sekaix + xPlus1Cand.toInt()

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
            val xLimit = if(xPlus1>0){(xSyougai - ookisa /2)}else if(xPlus1<0){xSyougai + 32 + (ookisa /2)}else{sekaix}
            xPlus1 = 0f
            xLimit
        }
        sekaix  = x1CandC
        xPlus = xPlus1
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
        val yBlock = (sekaiy / 32)-2
        println("yBlock=$yBlock,sekaiy$sekaiy,isJump=$isJump")
        return if(map.masu[yBlock][checkBlock+1] == 1){ false }else{true}
    }






    fun kasokudoX(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (xPlus == 0f) { return 0.0f }
                if (xPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


    fun draw(canvas: Canvas) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro)
    }

}

