package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.widget.Toast

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 360    //360にした。有野指令
    var sekaiy = 500
    var xPlus = 0f
    var isJump = false
    var yPlus = 0f

    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
        idoUeShita(controller,map)        //縦移動　y軸
    }


    fun idoUeShita(controller: Controller, map: Map){
        val kasokudoy = kasokudoJump()

        //①最初に、ジャンプをしていなかった場合、ジャンプをする（）
        val yPlusCand = if (isJump == false) {if (controller.houkou == "jump"){
            isJump = true
            50f
            }else{
            (yPlus + kasokudoy).toFloat()
            }
        }else{
            (yPlus + kasokudoy).toFloat()
        }




        //②次の位置を計算する
        val y1CandA = sekaiy + yPlusCand.toInt()

        //③計算した位置が障害物かどうかを判定する
        val c = true //mapCheckY(map,y1CandA,yPlusCand)


        //④結果を反映させる
        var syougaiCheckY  = syougaiY(controller, map)
        if (isJump) { if(syougaiCheckY){ isJump = false } }


        //最後に代入
        sekaiy  = y1CandA
        yPlus = yPlusCand
        y = 500
    }

    fun mapCheckY(map:Map,y1CandA:Int,yPlusCand: Float):Boolean{
        //チェックする場所のポイント
        val checkPointY = y1CandA
        // チェックするブロック
        val checkBlockY = ( checkPointY/ 32)


        //横方向も必要
        val checkPointX = sekaix
        val checkBlockX = if(xPlus>0){
            //右方向だったら
            ( checkPointX/ 32)
        }else if(xPlus<0){
            //左方向だったら
            ( checkPointX/ 32)-1
        }else{( checkPointX/ 32)
        }

        val masu = map.masu
        println("cb=$checkBlockX,x1cand=$y1CandA,mas[9]=${masu[13][9]},mas[10]=${masu[13][10]},mas[11]=${masu[13][11]}")
        return if(map.masu[13+checkPointY][checkBlockX+1] == 1){ false }else{true}
    }




    fun jumpSyori(controller: Controller,map:Map) {

        if (controller.houkou == "jump") {  //ジャンプしてなかったらジャンプする
            if (isJump == false) {
                isJump = true
                yPlus = 50f
                y -= yPlus.toInt()
            }
        }
        if (isJump) {   //ジャンプ中ならジャンプを継続する
            yPlus = yPlus + kasokudoJump()
            y -= yPlus.toInt()
        }


    }



    fun syougaiY( controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32
        var checksekaiy = y - yPlus.toInt()
        var yBlock = 0
        if(checksekaiy<=550 && checksekaiy >=501){yBlock = 14}
        if(checksekaiy<=500 && checksekaiy >=468){yBlock = 13}
        if(checksekaiy<=467 && checksekaiy >=436){yBlock = 12}
        if(checksekaiy<=435 && checksekaiy >=404){yBlock = 11}
        val checkMasuSyuruiJump = map.masu[yBlock][checkBlock+1]
        var checkKekka = false
        when(checkMasuSyuruiJump){
            0 -> { checkKekka = false }
            1 -> { checkKekka = true }
            else ->{checkKekka = false }
        }
        return checkKekka
    }


    fun kasokudoJump(): Float {
        return -5.0f
    }

    fun idoMigiHidari(controller: Controller, map: Map){
        val kasokudox = kasokudoX(controller.houkou)
        val xPlus1Cand = xPlus + kasokudox
        val x1CandA = sekaix + xPlus1Cand.toInt()

        //世界の端かどうかを補正したx1候補
        val x1CandB = if(x1CandA>877){877}else if(x1CandA<0){0}else{x1CandA}
        val xPlus1SokudoSeigenCand =  if(x1CandA>877){0f}else if(x1CandA<0){0f}else{xPlus1Cand}

        //速度制限つけた
        var xPlus1 = if(xPlus1SokudoSeigenCand>= 30){30f}else if(xPlus1SokudoSeigenCand<= -30){-30f}else{xPlus1SokudoSeigenCand}


        println("sekaix:$sekaix,x1CandB:$x1CandB")
        //障害物にぶつかっているかどうかを補正したx1候補
        val x1CandC = if(mapCheck(map,x1CandB,xPlus1)){
            x1CandB
        }else{
            val xSyougai =  (x1CandB/ 32)*32 //かならず左肩が入る
            val xLimit = if(xPlus1>0){(xSyougai - ookisa /2)}else if(xPlus1<0){xSyougai + 32 + (ookisa /2)}else{500}
            xPlus1 = 0f
            xLimit
        }
        sekaix  = x1CandC
        xPlus = xPlus1
        //ｘは不変だが、設定するならここか
        // x = x1CandA

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
        println("cb=$checkBlock,x1cand=$x1CandB,mas[9]=${masu[13][9]},mas[10]=${masu[13][10]},mas[11]=${masu[13][11]}")
        return if(map.masu[13][checkBlock+1] == 1){ false }else{true}
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

