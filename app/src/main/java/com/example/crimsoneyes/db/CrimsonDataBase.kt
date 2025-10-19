package com.example.crimsoneyes.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.crimsoneyes.dao.UsuarioDAO
import com.example.crimsoneyes.model.Usuario


@Database(entities = [Usuario::class], version= 1)
abstract class CrimsonDataBase: RoomDatabase() {
    abstract fun UsuarioDao(): UsuarioDAO

    companion object {
        @Volatile private var INSTANCE: CrimsonDataBase? = null

        fun get (context: Context): CrimsonDataBase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CrimsonDataBase::class.java,
                    "crimson.db"
                ).build().also { INSTANCE = it}
            }
    }

}