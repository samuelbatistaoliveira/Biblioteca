package com.example.teste3.BotaoNav

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import com.example.teste3.R
import com.example.teste3.home_aluno.HomeActivity
import com.example.teste3.login.ChatbotActivity
import com.example.teste3.perfil.PrincipalPerfil as PerfilActivity

object BottomNavHelper {

    enum class NavItem { CHAT, HOME, CALENDAR, CATEGORIES, PROFILE }

    fun setup(
        context: Context,
        navChat: LinearLayout,       navChatBg: LinearLayout,
        navHome: LinearLayout,       navHomeBg: LinearLayout,
        navCalendar: LinearLayout,   navCalendarBg: LinearLayout,
        navCategories: LinearLayout, navCategoriesBg: LinearLayout,
        navProfile: LinearLayout,    navProfileBg: LinearLayout,
        activeItem: NavItem
    ) {
        val allBgs = listOf(navChatBg, navHomeBg, navCalendarBg, navCategoriesBg, navProfileBg)
        val activeMap = mapOf(
            NavItem.CHAT       to navChatBg,
            NavItem.HOME       to navHomeBg,
            NavItem.CALENDAR   to navCalendarBg,
            NavItem.CATEGORIES to navCategoriesBg,
            NavItem.PROFILE    to navProfileBg,
        )

        allBgs.forEach { it.setBackgroundResource(0) }
        activeMap[activeItem]?.setBackgroundResource(R.drawable.bg_nav_active)

        navChat.setOnClickListener {
            allBgs.forEach { it.setBackgroundResource(0) }
            navChatBg.setBackgroundResource(R.drawable.bg_nav_active)
            navigateTo(context, NavItem.CHAT)
        }
        navHome.setOnClickListener {
            allBgs.forEach { it.setBackgroundResource(0) }
            navHomeBg.setBackgroundResource(R.drawable.bg_nav_active)
            navigateTo(context, NavItem.HOME)
        }
        navCalendar.setOnClickListener {
            allBgs.forEach { it.setBackgroundResource(0) }
            navCalendarBg.setBackgroundResource(R.drawable.bg_nav_active)
            navigateTo(context, NavItem.CALENDAR)
        }
        navCategories.setOnClickListener {
            allBgs.forEach { it.setBackgroundResource(0) }
            navCategoriesBg.setBackgroundResource(R.drawable.bg_nav_active)
            navigateTo(context, NavItem.CATEGORIES)
        }
        navProfile.setOnClickListener {
            allBgs.forEach { it.setBackgroundResource(0) }
            navProfileBg.setBackgroundResource(R.drawable.bg_nav_active)
            navigateTo(context, NavItem.PROFILE)
        }
    }

    private fun navigateTo(context: Context, item: NavItem) {
        when (item) {
            NavItem.CHAT -> {
                context.startActivity(Intent(context, ChatbotActivity::class.java))
            }
            NavItem.HOME -> {
                context.startActivity(Intent(context, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
            NavItem.PROFILE -> {
                context.startActivity(Intent(context, PerfilActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
            }
            else -> {}
        }
    }
}