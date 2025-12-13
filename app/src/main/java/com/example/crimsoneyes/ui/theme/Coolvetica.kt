package com.example.crimsoneyes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.crimsoneyes.R

// fuente Coolvetica
val Coolvetica = FontFamily(Font(R.font.coolvetica, FontWeight.Normal))

//  Tipografía personalizada
val typography =
        Typography(
                // Display - Títulos grandes y dramáticos
                displayLarge =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 72.sp,
                                lineHeight = 80.sp,
                                letterSpacing = (-0.1).sp
                        ),
                displayMedium =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 60.sp,
                                lineHeight = 68.sp,
                                letterSpacing = 0.sp
                        ),
                displaySmall =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 50.sp,
                                lineHeight = 58.sp,
                                letterSpacing = 0.sp
                        ),

                // Headlines
                headlineLarge =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 42.sp,
                                lineHeight = 50.sp,
                                letterSpacing = 0.sp
                        ),
                headlineMedium =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp,
                                lineHeight = 44.sp,
                                letterSpacing = 0.sp
                        ),
                headlineSmall =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Medium,
                                fontSize = 32.sp,
                                lineHeight = 40.sp,
                                letterSpacing = 0.sp
                        ),

                // Titles
                titleLarge =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                lineHeight = 36.sp,
                                letterSpacing = 0.sp
                        ),
                titleMedium =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Medium,
                                fontSize = 22.sp,
                                lineHeight = 30.sp,
                                letterSpacing = 0.15.sp
                        ),
                titleSmall =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp,
                                lineHeight = 28.sp,
                                letterSpacing = 0.1.sp
                        ),

                // Body
                bodyLarge =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Normal,
                                fontSize = 20.sp,
                                lineHeight = 30.sp,
                                letterSpacing = 0.5.sp
                        ),
                bodyMedium =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Normal,
                                fontSize = 18.sp,
                                lineHeight = 26.sp,
                                letterSpacing = 0.25.sp
                        ),
                bodySmall =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                letterSpacing = 0.4.sp
                        ),

                // Labels
                labelLarge =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                letterSpacing = 0.1.sp
                        ),
                labelMedium =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                letterSpacing = 0.5.sp
                        ),
                labelSmall =
                        TextStyle(
                                fontFamily = Coolvetica,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                letterSpacing = 0.5.sp
                        )
        )
