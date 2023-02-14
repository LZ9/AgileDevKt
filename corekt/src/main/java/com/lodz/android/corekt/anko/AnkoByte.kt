package com.lodz.android.corekt.anko

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer

/**
 * Byte扩展类
 * @author zhouL
 * @date 2021/10/26
 */

/** 将数组[other]线性合并到当前数组之后 */
fun ByteArray.merge(other: ByteArray): ByteArray {
    val result = ByteArray(this.size + other.size)
    System.arraycopy(this, 0, result, 0, this.size)
    System.arraycopy(other, 0, result, this.size, other.size)
    return result
}

/** Int转ByteArray（高字节在前） */
@Deprecated("使用toByteArray方法")
fun Int.toByteArrayHF(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[0] = (this and 0xff).toByte()
    byteArray[1] = (this shr 8 and 0xff).toByte()
    byteArray[2] = (this shr 16 and 0xff).toByte()
    byteArray[3] = (this shr 24 and 0xff).toByte()
    return byteArray
}

/** Int转ByteArray（低字节在前） */
@Deprecated("使用toByteArray方法")
fun Int.toByteArrayLF(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[3] = (this and 0xff).toByte()
    byteArray[2] = (this shr 8 and 0xff).toByte()
    byteArray[1] = (this shr 16 and 0xff).toByte()
    byteArray[0] = (this shr 24 and 0xff).toByte()
    return byteArray
}

/** Int转ByteArray */
fun Int.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    val dos = DataOutputStream(baos)
    dos.writeInt(this)
    return baos.toByteArray()
}

/** Short转ByteArray */
fun Short.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    val dos = DataOutputStream(baos)
    dos.writeShort(this.toInt())
    return baos.toByteArray()
}

/** Long转ByteArray */
fun Long.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    val dos = DataOutputStream(baos)
    dos.writeLong(this)
    return baos.toByteArray()
}

/** Long转ByteArray */
fun Float.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    val dos = DataOutputStream(baos)
    dos.writeFloat(this)
    return baos.toByteArray()
}

/** Double转ByteArray */
fun Double.toByteArray(): ByteArray {
    val baos = ByteArrayOutputStream()
    val dos = DataOutputStream(baos)
    dos.writeDouble(this)
    return baos.toByteArray()
}

/** ByteArray转ByteBuffer */
fun ByteArray.toByteBuffer(): ByteBuffer = ByteBuffer.wrap(this)

/** ShortArray转ByteArray */
fun ShortArray.toByteArray(): ByteArray {
    val count = this.size
    val dest = ByteArray(count shl 1)
    for (i in 0 until count) {
        dest[i * 2] = (this[i].toInt() shr 8).toByte()
        dest[i * 2 + 1] = this[i].toByte()
    }
    return dest
}

/** ByteArray转ShortArray */
fun ByteArray.toShortArray(): ShortArray {
    val shorts = ShortArray(this.size / 2)
    ByteBuffer.wrap(this).asShortBuffer().get(shorts)
    return shorts
}

fun UInt.toByteArray(littleEndian: Boolean = true): ByteArray {
    val bytes = ByteArray(4)
    if (littleEndian) {
        for (i in 0..3) bytes[i] = (this shr (i * 8)).toByte()
    } else {
        for (i in 0..3) bytes[3 - i] = (this shr (i * 8)).toByte()
    }
    return bytes
}

fun Short.toByteArray(littleEndian: Boolean = true): ByteArray {
    return if (littleEndian) {
        byteArrayOf((this.toInt() and 0x00FF).toByte(), ((this.toInt() and 0xFF00) shr 8).toByte())
    } else {
        byteArrayOf(((this.toInt() and 0xFF00) shr 8).toByte(), (this.toInt() and 0x00FF).toByte())
    }
}

/** ByteArray转Int */
fun ByteArray.toInt(): Int = ByteBuffer.wrap(this).int

/** ByteArray转Short */
fun ByteArray.toShort(): Short = ByteBuffer.wrap(this).short

/** ByteArray转Long */
fun ByteArray.toLong(): Long = ByteBuffer.wrap(this).long

/** ByteArray转Float */
fun ByteArray.toFloat(): Float = ByteBuffer.wrap(this).float

/** ByteArray转Double */
fun ByteArray.toDouble(): Double = ByteBuffer.wrap(this).double
