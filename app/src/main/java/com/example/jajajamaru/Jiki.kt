package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import android.widget.Toast

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置
    var worldOffsetX = 0    //いる    消したら何が起きるかわからないが、いる
    var xPlus = 0f
    fun draw(canvas: Canvas) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro)
    }
    var isJump = false
    var vJump = 0f
    fun kasokudoJump(): Float {
        return -5.0f
    }


    fun charaWorldIdoSeigen(): Boolean {
        val checkSekaix = sekaix + xPlus.toInt()
        if (checkSekaix >= 100 && checkSekaix <= (224+32*25)) { //25マスまでしか進めませんよ、みたいな
            return true
        } else {
            xPlus = 0f  //加速はいったん０にする
            return false
        }
    }


    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
        idoUeShita(controller,map)        //縦移動　y軸
    }

    fun idoMigiHidari(controller: Controller, map: Map){
        val kasokudox = kasokudoYoko(controller.houkou)
        val xPlus1Cand = xPlus + kasokudox
        val x1CandA = sekaix + xPlus1Cand.toInt()

        //世界の端かどうかを補正したｘ１候補
        val x1CandB = if(x1CandA>877){877}else if(x1CandA<0){0}else{x1CandA}
        val xPlus1SokudoSeigenCand =  if(x1CandA>877){0f}else if(x1CandA<0){0f}else{xPlus1Cand}

        //速度制限つけた
        var xPlus1 = if(xPlus1SokudoSeigenCand>= 30){30f}else if(xPlus1SokudoSeigenCand<= -30){-30f}else{xPlus1SokudoSeigenCand}

        //障害物にぶつかっているかどうかを補正したｘ１候補
        val x1CandC = if(mapCheck(map,x1CandB,xPlus1)){
            x1CandB
        }else{
            xPlus1 = 0f

  //          val xSyougai =  x1CandB

//            val xLimit = ...
            200
        }

        sekaix  = x1CandC
        xPlus = xPlus1
    }

    fun mapCheck(map:Map,x1CandB:Int,xPlus1: Float):Boolean{
        val checkPoint = x1CandB
        val checkBlock = ( checkPoint/ 32)
        return if(map.masu[13][checkBlock+1] == 1){ false }else{true}
    }





    fun idoUeShita(controller: Controller, map: Map){
        jumpSyori(controller,map)   // ジャンプ処理　落下、障害物に当たるなど　//なんかこの位置にないとダメ
        var syougaiCheckY  = syougaiY(controller, map)
        if (isJump) { if(syougaiCheckY){ isJump = false } }        //縦方向に障害物があった場合、ジャンプを中止する。
    }

    fun jumpSyori(controller: Controller,map:Map) {
        if (controller.houkou == "jump") {  //ジャンプしてなかったらジャンプする
            if (isJump == false) {
                isJump = true
                vJump = 50f
                y -= vJump.toInt()
            }
        }
        if (isJump) {   //ジャンプ中ならジャンプを継続する
            vJump = vJump + kasokudoJump()
            y -= vJump.toInt()
        }
    }




    fun syougaiY( controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32
        var checksekaiy = y - vJump.toInt()
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



    fun kasokudoYoko(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (xPlus == 0f) { return 0.0f }
                if (xPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


}