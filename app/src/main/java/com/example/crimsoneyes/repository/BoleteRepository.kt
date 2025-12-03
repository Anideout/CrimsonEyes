package com.example.crimsoneyes.repository

import com.example.crimsoneyes.dao.BoleteDAO
import com.example.crimsoneyes.model.Boleta
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class BoleteRepository(private val boleteDao: BoleteDAO) {

    fun getAllBoletas(): Flow<List<Boleta>> = boleteDao.getAllBoletas()

    fun getBoletesByUsuario(email: String): Flow<List<Boleta>> =
        boleteDao.getBoletesByUsuario(email)

    suspend fun insertBoleta(
        usuarioEmail: String,
        metodoPago: String,
        subtotal: Int,
        envio: Int,
        total: Int,
        cantidadProductos: Int,
        detallesProductos: String
    ): Long {
        val boleta = Boleta(
            numeroVenta = generarNumeroVenta(),
            usuarioEmail = usuarioEmail,
            fechaCompra = obtenerFechaActual(),
            metodoPago = metodoPago,
            subtotal = subtotal,
            envio = envio,
            total = total,
            cantidadProductos = cantidadProductos,
            detallesProductos = detallesProductos,
            estado = "completada"
        )
        return boleteDao.insert(boleta)
    }

    suspend fun getBoleteById(id: Long): Boleta? = boleteDao.getBoleteById(id)

    suspend fun deleteBoleta(id: Long) = boleteDao.deleteById(id)

    private fun generarNumeroVenta(): String {
        val timestamp = System.currentTimeMillis()
        val random = (1000..9999).random()
        return "BOL-${timestamp}-$random"
    }

    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}

