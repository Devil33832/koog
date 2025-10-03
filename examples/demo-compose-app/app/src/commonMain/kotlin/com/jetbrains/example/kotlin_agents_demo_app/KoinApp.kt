package com.jetbrains.example.kotlin_agents_demo_app

import ai.koog.prompt.executor.model.PromptExecutor
import ai.koog.prompt.llm.LLModel
import androidx.compose.runtime.Composable
import com.jetbrains.example.kotlin_agents_demo_app.agents.calculator.CalculatorAgentProvider
import com.jetbrains.example.kotlin_agents_demo_app.agents.chat.ChatAgentProvider
import com.jetbrains.example.kotlin_agents_demo_app.agents.common.AgentProvider
import com.jetbrains.example.kotlin_agents_demo_app.agents.weather.WeatherAgentProvider
import com.jetbrains.example.kotlin_agents_demo_app.screens.agentdemo.AgentDemoViewModel
import com.jetbrains.example.kotlin_agents_demo_app.screens.settings.SettingsViewModel
import com.jetbrains.example.kotlin_agents_demo_app.screens.start.StartViewModel
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.KoinConfiguration
import org.koin.dsl.module

@OptIn(KoinExperimentalAPI::class)
@Composable
fun KoinApp(executor: PromptExecutor? = null, model: LLModel? = null) = KoinMultiplatformApplication(
    config = KoinConfiguration {
        modules(
            appPlatformModule,
            module {
                single<AgentProvider>(named("calculator")) { CalculatorAgentProvider(executor, model) }
                single<AgentProvider>(named("weather")) { WeatherAgentProvider(executor, model) }
                single<AgentProvider>(named("chat")) { ChatAgentProvider(executor, model) }
                factory { SettingsViewModel(appSettings = get()) }
                factory { StartViewModel() }
                factory { params ->
                    AgentDemoViewModel(
                        agentProvider = params.get(),
                        appSettings = get()
                    )
                }
            }
        )
    }
) {
    ComposeApp()
}

expect val appPlatformModule: Module
