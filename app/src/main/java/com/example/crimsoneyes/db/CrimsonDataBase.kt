package com.example.crimsoneyes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crimsoneyes.dao.BoleteDAO
import com.example.crimsoneyes.dao.RecetaDAO
import com.example.crimsoneyes.dao.UsuarioDAO
import com.example.crimsoneyes.dao.VentaDAO
import com.example.crimsoneyes.model.Boleta
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.model.Venta


@Database(entities = [Usuario::class, Receta::class, Boleta::class, Venta::class],
    version = 4,
    exportSchema = false
)
abstract class CrimsonDataBase: RoomDatabase() {
    abstract fun UsuarioDao(): UsuarioDAO
    abstract fun RecetaDao(): RecetaDAO
    abstract fun BoleteDao(): BoleteDAO
    abstract fun VentaDao(): VentaDAO

    companion object {
        @Volatile private var INSTANCE: CrimsonDataBase? = null

        fun get (context: Context): CrimsonDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CrimsonDataBase::class.java,
                    "crimson.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it}
            }
    }

}