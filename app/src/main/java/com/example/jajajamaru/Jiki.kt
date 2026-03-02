package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint
import kotlin.contracts.contract
import kotlin.math.max
import kotlin.math.min

class Jiki(val initialPos: Vec2D) {
    val ookisa = 100
    val iro = Paint()
    val iroTestYou = Paint()

    var sekaipos = Vec2D(360,400)
    var sokudo = Vec2DF(0f,0f)
    var kasokudo = Vec2DF(0f,0f)
    var isJump = false
    var zure = 0
    fun idoSyori(controller: Controller, map: Map) {

        val u0 = Ugoki(sekaipos, sokudo, kasokudo)

        //加速度更新
        val u1CandA = kasokudoKoushin(u0, controller)

        //速度更新
        var u1CandC = sokudoKoushin(u1CandA, controller)

        //posを更新
        val u1CandD = u1CandC.copy(pos = Vec2D(u1CandC.pos.x + u1CandC.sokudo.x.toInt(), u1CandC.pos.y + u1CandC.sokudo.y.toInt()))

        //世界の上下左右端チェック
        val u1CandE = sekaiHashiCheck(map,u1CandD)

        //障害物上下左右チェック
        val u1CandF = shogaibutuJogeSayuu(map, u1CandE,u0)


        sokudo = u1CandF.sokudo
        kasokudo = u1CandF.kasokudo
        sekaipos = u1CandF.pos
        zure = initialPos.x - sekaipos.x
    }


    //落下中にすり抜けてしまう。開始位置から上り坂を降りるところのＪ字型の場所には、落下で止まれない。
    //予想、２マス分したに行ってしまっている？加速制限をつけると解決する？
    private fun shogaibutuJogeSayuu(map: Map, before: Ugoki,u0: Ugoki): Ugoki {

        //障害物上下処理
        val afterJouge = if (mapCheckY(map, before.pos.x,before.pos.y)) {
            before
        } else {
            val yU0 = u0.pos.y
            val yU1 = before.pos.y
            //境界線上にとまらないように、-1と+1している
            val yLimit =if(yU0>yU1){ //上昇中
                1+32+(yU1 / 32) * 32
            }else{//下降中
                isJump = false
                -1+(yU1 / 32) * 32
            }
            before.copy(pos = Vec2D(before.pos.x, yLimit), sokudo = Vec2DF(before.sokudo.x, 0f))
        }

        //posの値を見て障害物か判定している。posの値を修正している。
        //障害物左右処理
        val afterSayuu = if (mapCheckX(map, afterJouge.pos.x,afterJouge.pos.y)) {
            afterJouge
        } else {
            val xU1 = afterJouge.pos.x
            val xU0 = u0.pos.x
            val xLimit = if(xU0>xU1){//右からきてる
                1+32+(afterJouge.pos.x / 32) * 32
            }else if(xU0<xU1){//左からきてる
                -1+(afterJouge.pos.x / 32) * 32
            }else{
                afterJouge.pos.x
            }

            afterJouge.copy(
                pos = Vec2D(xLimit, afterJouge.pos.y),
                sokudo = Vec2DF(0f, afterJouge.sokudo.y)
            )
        }



        //落下の判定をする、止まった位置の下には何かあるか？で落下するか決める。
        val afterRakka = if (mapCheckYRakka(map, afterSayuu.pos.x,afterSayuu.pos.y)) {
            val yU0 = u0.pos.y
            val yU1 = before.pos.y


            //もし静止した位置で下に足場がなかったら落下する
            val ySokudoBefore = before.sokudo.y
            val yPosBefore = before.pos.y

            //ここは落下中にしか判定しない。上昇中はぶつかった判定にする為。
            if(yU0<yU1){
            afterSayuu.copy(
                pos = Vec2D(afterSayuu.pos.x, yPosBefore),
                sokudo = Vec2DF(before.sokudo.x, ySokudoBefore)
            )
            }else{
                afterSayuu
            }


        } else {
            //下が「１」、障害物だったならばそのまま
            afterSayuu
        }


        //ぶつかり判定　移動先が「１」だった場合、強制的に元の位置との中間地点にもどす
        //↑みたいな処理をかけたらいいな
        //これって、落下の時しか発生しないのでは？という予測
        val afterGenzaichi = if (mapCheckGenzaichi(map, afterRakka.pos.x,afterRakka.pos.y)) {
            afterRakka
         }else{
            val yU0 = u0.pos.y
            val yU1 = afterRakka.pos.y
            val xU0 = u0.pos.x
            val xU1 = afterRakka.pos.x

            var yHosei = afterRakka.pos.y
            var xHosei = afterRakka.pos.x


            if (yU0<yU1){//下降中
                //戻すのは１つ上で最下辺にしなければいけない
                yHosei = 32-1+(yU0 / 32) * 32
            }

            afterRakka.copy(
                pos = Vec2D(xHosei, yHosei),
            )
        }

        return afterGenzaichi
    }

    fun mapCheckGenzaichi(map:Map,x1cand:Int,y1Cand:Int):Boolean{
        //現在のブロックを判定する
        val yBlock = ( y1Cand/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun mapCheckYRakka(map:Map,x1cand:Int,y1Cand:Int):Boolean{
        //ひとつ下のブロックを判定する
        val yBlock = ( y1Cand/ 32) + 1
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun mapCheckX(map:Map,x1cand:Int,y1Cand:Int):Boolean{
        val yBlock = ( y1Cand/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }

    fun mapCheckY(map:Map,x1Cand:Int,y1Cand:Int):Boolean{
        val checkPointY = y1Cand
        val yBlock = ( checkPointY/ 32)
        if  (yBlock >= map.masu.size) return false
        val xBlock = (x1Cand/32)
        return if(map.masu[yBlock][xBlock] == 1){ false }else{true}
    }



    /**
    速度を更新する、まず加速度から速度を計算して、その後に最大速度制限とジャンプの処理をする
    */
    private fun sokudoKoushin(before: Ugoki, controller: Controller): Ugoki {

        //速度の更新
        val u1CandA = before.copy(
            sokudo = Vec2DF(
                before.sokudo.x + before.kasokudo.x,
                before.sokudo.y + before.kasokudo.y
            )
        )


        //速度の上限設定
        val u1CandB = u1CandA.copy(
            sokudo = Vec2DF(
                min(max(-30f, u1CandA.sokudo.x), 30f),
                min(max(-30f, u1CandA.sokudo.y), 45f)
            )
        )

        //ジャンプ処理
        var u1CandC = u1CandB
        if (isJump == false) {
            if (controller.pushedJumpButton == true) {
                isJump = true
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -60f))
            }
        }else{
            //多段ジャンプの場合
            if(controller.nikaimeJump){
                controller.nikaimeJump = false
                u1CandC = u1CandC.copy(sokudo = Vec2DF(u1CandC.sokudo.x, -60f))
            }
        }


        return u1CandC
    }

    private fun kasokudoKoushin(u0: Ugoki, controller: Controller): Ugoki {
        return u0.copy(kasokudo = Vec2DF(kasokudoX(controller.pushedSayuButton,u0), kasokudoY()))
        //左右にキーを入れていると、加速度が2.5か-2.5が返っている
    }

    //ジャンプの加速度
    fun kasokudoY(): Float {
        return 10.0f
    }




    fun kasokudoX(houkou:String,u0: Ugoki):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> {
                if (u0.sokudo.x == 0f) { return 0.0f }
                if (u0.sokudo.x > 0) {
                    return -2.5f
                } else {
                    return 2.5f
                }
            }

            else -> return 0f
        }
    }

    private fun sekaiHashiCheck(map:Map,before: Ugoki): Ugoki {
        //世界の上下チェック
        val afterJouge = if (isJump && before.pos.y < 96) {
            before.copy(pos = Vec2D(before.pos.x, 96), sokudo = Vec2DF(before.sokudo.x, 0f))
        } else {
            before
        }

        //横方向の補正　世界の端？
        //世界の左右チェック
        val afterSayuu =  afterJouge.copy(pos = Vec2D(min(max(0, afterJouge.pos.x), map.migiMax()), afterJouge.pos.y))

        //posの値を見て世界の端だったら速度を０にしている。
        //画面端だったら速度を０に
        val u1Cand = if (afterSayuu.pos.x == map.migiMax() || afterSayuu.pos.x == 0) {
            afterSayuu.copy(sokudo = Vec2DF(0f, afterSayuu.sokudo.y))
        } else {
            afterSayuu
        }
        return u1Cand
    }

    fun draw(canvas: Canvas,controller: Controller) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iroTestYou.style = Paint.Style.FILL

        if(isJump) {
            if(sokudo.y<0f) {
                iro.color = argb(255, 255, 255, 150) //飛んでる,上昇中
            }else if (sokudo.y>0f) {
                iro.color = argb(255, 150, 150, 150) //飛んでる、下降中
            }else{
                iro.color = argb(255, 150, 150, 150) //飛んでる、ちょうど０
            }
        }else{

            iro.color = argb(255, 255, 150, 150)//飛んでないとき赤
        }

        if(controller.pushedJumpButton) {
            iroTestYou.color = argb(255, 255, 255, 255)//飛んでる時　白
        }else{
            iroTestYou.color = argb(255, 220, 220, 220)//飛んでないとき　黒
        }
        //canvas.drawCircle(initialPos.x.toFloat(),(sekaipos.y).toFloat(),(ookisa/5).toFloat(),iro)

        //テスト用　頭の上に色付きの〇を表示する
        canvas.drawCircle(initialPos.x.toFloat()+5,(sekaipos.y-50).toFloat(),(15).toFloat(),iroTestYou)

    }

}

