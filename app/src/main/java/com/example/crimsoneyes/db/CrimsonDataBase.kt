package com.example.crimsoneyes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crimsoneyes.dao.RecetaDAO
import com.example.crimsoneyes.dao.UsuarioDAO
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.model.Usuario


@Database(entities = [Usuario::class, Receta::class],
    version= 2 ,
    exportSchema = false
)
abstract class CrimsonDataBase: RoomDatabase() {
    abstract fun UsuarioDao(): UsuarioDAO
    abstract fun RecetaDao(): RecetaDAO

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