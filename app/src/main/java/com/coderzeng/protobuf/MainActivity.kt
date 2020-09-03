package com.coderzeng.protobuf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.coderzeng.protobuf.data.BlEData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val newBuilder = BlEData.Protos.newBuilder();
        val newBuilder1 = BlEData.Protos.MessageInfo.newBuilder();
        newBuilder1.setPath("/companion/get_wear_info")
        newBuilder.setCode(1)
        val data = newBuilder.addMessageInfo(newBuilder1.build()).build().toByteString()

        var mageic = arrayOf(0x97,0x91,0x8D,0x89);
        var seq :Short = 0;
        var type :Byte = 100;

        val dataCRC :Short = CRC16Util.calcCrc16(data.toByteArray()).toShort()
        val size = data.size();
        var length:Short = (2+2+size+2).toShort()
        var lengthCRC:Short = 0;
        ByteArray(1).let {
            it[0] = length.toByte()
            lengthCRC = CRC16Util.calcCrc16(it).toShort()
        }

        val msg : ByteArray;
            ByteArray(8).let {
                it[0] = 0x97.toByte()
                it[1] = 0x91.toByte()
                it[2] = 0x8D.toByte()
                it[3] = 0x89.toByte()
                it[4] = length.toByte()
                it[5] = lengthCRC.toByte()
                it[6] = type
                it[7] = seq.toByte()
                it[8] = data.toStringUtf8().toByte()
                it[9] = dataCRC.toByte()
                msg = it;
            }
        Log.i("data",msg.toString())
        DataUtils.bytesToHexString(msg).let {
            Log.i("hex16",it)

        }

    }
}