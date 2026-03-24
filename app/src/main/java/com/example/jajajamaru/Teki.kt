package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Teki {
    //敵がやられたときに点滅するようにしたい。
    val ookisa = 100
    val iro = Paint()
    var sekaipos = Vec2D(500,500)
    var sokudo = Vec2DF(0f,0f)

    val tekipaint = Paint()

    //ここに加速度が右に入っているから右に動いているだけ
    var kasokudo = Vec2DF(0f,0f)

    var ugokiHoukou = "hidari"

    var x = 100
    var y = 100
    var xx = 100
    var yarareHantei = false
    var shibou = false
    var panchiHantei = false
    var mutekiTime = 10


    //敵の状態遷移の準備。
    val TEKI_NASI_STATE = 1
    val TEKI_NORMAL_STATE = 2
    val TEKI_BARETA_MUTEKI_STATE = 3
    val TEKI_BARETA_NORMAL_STATE = 4
    val TEKI_BARETE_HIT_STATE = 5
    val TEKI_BARETE_HIT_YARARE_STATE = 6
    val TEKI_HIT_END_STATE = 7

    var status = TEKI_NASI_STATE // 最初は玉が画面内に無い状態

    init{
        tekipaint.alpha = 255
    }

    fun nextFrame(controller:Controller,map:Map,jiki:Jiki) {

        when(status) {
            TEKI_NASI_STATE -> { status = TEKI_NORMAL_STATE }

            TEKI_NORMAL_STATE -> {
                if(sekaipos.x>700){ugokiHoukou="hidari"}
                if(sekaipos.x<395){ugokiHoukou="migi"}
                val flag = tikazukiCheck(jiki)//近づかれたら、やられた判定
                if (yarareHantei == false) { yarareHantei = flag }   //やられた判定がfalseならtrueへ
                if (yarareHantei) {                //やられた判定がtrueなら
                    status = TEKI_BARETA_MUTEKI_STATE
                }else{
                    idoSyori(controller, map,jiki)
                }
            }

            TEKI_BARETA_MUTEKI_STATE -> {
                //ここでの状態がわかりづらいので、なにか変化をさせたい。
                mutekiTime--    //10フレームだけ無敵    var mutekiTime = 10
                tekipaint.alpha = 128
                if (mutekiTime <= 1) {
                    tekipaint.alpha = 255
                    status = TEKI_BARETA_NORMAL_STATE
                }
            }

            TEKI_BARETA_NORMAL_STATE     -> {
                if (tikazukiCheck(jiki)) {  //もう一度食らった
                    shibou = true
                    status = TEKI_BARETE_HIT_STATE
                }
            }


            TEKI_BARETE_HIT_STATE -> {
                status = TEKI_HIT_END_STATE
            }

            TEKI_HIT_END_STATE -> {
                shibousyori()
                if (sekaipos.y>=1200){
                    syokika()
                    status = TEKI_NASI_STATE
                }
            }
        }
    }


    fun syokika(){
        sekaipos = Vec2D(500,500)
        yarareHantei = false
        shibou = false
        mutekiTime = 10
    }

    fun idoSyori(controller: Controller, map:Map,jiki:Jiki) {

        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新　（コントローラーはない）
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
        var u1CandB = sokudoKoushin(u1CandA, controller)

        //posを更新
        val u1CandC = u1CandB.copy(
            pos = Vec2D(
                u1CandB.pos.x + u1CandB.sokudo.x.toInt(),
                u1CandB.pos.y + u1CandB.sokudo.y.toInt()
            )
        )

        //自機から離れすぎないように左右端チェック //とくになにもやってない
        val u1CandD = sekaiHashiCheck(map, u1CandC)

        //障害物上下左右チェック
        val u1CandF = shogaibutuJogeSayuu(map, u1CandD,u0)


        sokudo = u1CandF.sokudo
        kasokudo = u1CandF.kasokudo
        sekaipos = u1CandF.pos

    }

    fun kasokudoKoushin(u0:Ugoki, controller:Controller):Ugoki{
        val u1cand = if (ugokiHoukou=="migi"){
            u0.copy(kasokudo= Vec2DF(0.5f,u0.sokudo.y))
        }else  if (ugokiHoukou=="hidari") {
            u0.copy(kasokudo= Vec2DF(-0.5f,u0.sokudo.y))
        }else{
            u0.copy(kasokudo= Vec2DF(u0.kasokudo.x,u0.sokudo.y))
        }

        return u1cand
    }
    fun sekaiHashiCheck(map:Map,u1CandD:Ugoki):Ugoki{
        return u1CandD
    }
    private fun shogaibutuJogeSayuu(map:Map, before: Ugoki, u0: Ugoki): Ugoki {
        //障害物上下処理
        val afterJouge = if (mapCheckY(map, before.pos.x, before.pos.y)) {
            before
        } else {
            val yU0 = u0.pos.y
            val yU1 = before.pos.y
            //境界線上にとまらないように、-1と+1している
            val yLimit = if (yU0 > yU1) { //上昇中
                1 + 32 + (yU1 / 32) * 32
            } else {//下降中
                // isJump = false
                -1 + (yU1 / 32) * 32
            }
            before.copy(pos = Vec2D(before.pos.x, yLimit), sokudo = Vec2DF(before.sokudo.x, 0f))
        }

        //posの値を見て障害物か判定している。posの値を修正している。
        //障害物左右処理

        val afterSayuu = if (mapCheckX(map, afterJouge.pos.x, afterJouge.pos.y)) {
            afterJouge
        } else {
            val xU1 = afterJouge.pos.x
            val xU0 = u0.pos.x
            val xLimit = if (xU0 > xU1) {//右からきてる
                1 + 32 + (afterJouge.pos.x / 32) * 32
            } else if (xU0 < xU1) {//左からきてる
                -1 + (afterJouge.pos.x / 32) * 32
            } else {
                afterJouge.pos.x
            }

            afterJouge.copy(
                pos = Vec2D(xLimit, afterJouge.pos.y),
                sokudo = Vec2DF(0f, afterJouge.sokudo.y)
            )
        }
        return afterSayuu
    }


    fun mapCheckX(map: Map, x1cand:Int, y1Cand:Int):Boolean{
        val yBlock = ( y1Cand/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun mapCheckY(map: Map, x1Cand:Int, y1Cand:Int):Boolean{
        val checkPointY = y1Cand
        val yBlock = ( checkPointY/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1Cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun shibousyori(){
        sekaipos = sekaipos.copy(sekaipos.x,sekaipos.y+20)
    }



    fun tikazukiCheck(jiki:Jiki):Boolean{
        //jikiと近かったらtrueを返す
         val xx = sekaipos.x
        val yy = sekaipos.y

            val vx = xx - jiki.sekaipos.x
            val vy = yy - jiki.sekaipos.y

            val kyori = Math.sqrt((vx * vx) + (vy * vy) .toDouble())
            val atarikyori = (jiki.ookisa/4).toDouble()

            val panchikyori = (jiki.ookisa).toDouble()
        if (kyori < panchikyori){
            panchiHantei = true
        }else{
            panchiHantei = false
        }




            if (kyori < atarikyori){
                return true
            }else{
                return false
            }




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
}